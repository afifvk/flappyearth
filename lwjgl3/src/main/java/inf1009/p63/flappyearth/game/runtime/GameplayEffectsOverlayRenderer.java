package inf1009.p63.flappyearth.game.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import inf1009.p63.flappyearth.engine.services.RandomManager;
import inf1009.p63.flappyearth.engine.services.RenderManager;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.ActiveEffects;

import java.util.ArrayList;
import java.util.List;

public class GameplayEffectsOverlayRenderer {

    private static final float PLASTIC_BOTTLE_DURATION = 5f;
    private static final float OIL_BLOT_DURATION = 5f;
    private static final float FACTORY_SMOKE_DURATION = 5f;
    private static final float TRASH_PILE_SHAKE_DURATION = 5f;
    private static final float DEBUFF_POPUP_MAX_SECONDS = 5f;
    private static final int OIL_SPLOTCH_COUNT = 10;

    private final float[] oilSplotchXNorm = new float[OIL_SPLOTCH_COUNT];
    private final float[] oilSplotchYNorm = new float[OIL_SPLOTCH_COUNT];
    private final float[] oilSplotchSizeNorm = new float[OIL_SPLOTCH_COUNT];
    private final float[] oilSplotchRotation = new float[OIL_SPLOTCH_COUNT];

    private final List<DebuffOverlayInfo> debuffOverlayScratch = new ArrayList<>();
    private boolean oilSplotchPatternReady;
    private float oilSplotchAnimTime;

    public void reset() {
        oilSplotchPatternReady = false;
        oilSplotchAnimTime = 0f;
        debuffOverlayScratch.clear();
    }

    public void renderOilOverlay(RenderManager renderManager,
                                 float screenW,
                                 float screenH,
                                 ActiveEffects activeEffects,
                                 RandomManager randomManager) {
        if (activeEffects == null || !activeEffects.isOilBlotActive()) {
            oilSplotchPatternReady = false;
            oilSplotchAnimTime = 0f;
            return;
        }

        if (!oilSplotchPatternReady) {
            generateOilSplotchPattern(randomManager);
            oilSplotchPatternReady = true;
        }

        oilSplotchAnimTime += Gdx.graphics.getDeltaTime();

        ShapeRenderer shapeRenderer = renderManager.getShapeRenderer();
        float intensity = activeEffects.getOilBlotIntensity();
        float alpha = 0.18f + (0.26f * intensity);
        float minScreenDim = Math.min(screenW, screenH);

        float[] lobeDistance = {0.62f, 0.74f, 0.58f, 0.70f, 0.64f};
        float[] lobeSize = {0.58f, 0.44f, 0.52f, 0.40f, 0.47f};

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.04f, 0.04f, 0.04f, alpha);
        for (int i = 0; i < OIL_SPLOTCH_COUNT; i++) {
            float centerX = oilSplotchXNorm[i] * screenW;
            float centerY = oilSplotchYNorm[i] * screenH;
            float baseRadius = oilSplotchSizeNorm[i] * minScreenDim;
            float rotation = oilSplotchRotation[i];
            float phase = oilSplotchAnimTime * 3.2f + (i * 0.9f);
            float wobble = 1f + (0.06f * MathUtils.sin(phase));
            float wobbleX = MathUtils.cos(phase * 0.8f) * baseRadius * 0.07f;
            float wobbleY = MathUtils.sin(phase * 1.1f) * baseRadius * 0.05f;

            centerX += wobbleX;
            centerY += wobbleY;
            baseRadius *= wobble;

            shapeRenderer.circle(centerX, centerY, baseRadius);
            for (int l = 0; l < lobeDistance.length; l++) {
                float angle = rotation + (l * (360f / lobeDistance.length));
                float offset = baseRadius * lobeDistance[l];
                float lobeRadius = baseRadius * lobeSize[l];
                float lx = centerX + MathUtils.cosDeg(angle) * offset;
                float ly = centerY + MathUtils.sinDeg(angle) * offset;
                shapeRenderer.circle(lx, ly, lobeRadius);
            }

            float dripX = centerX + MathUtils.cosDeg(rotation + 230f) * baseRadius * 0.55f;
            float dripY = centerY + MathUtils.sinDeg(rotation + 230f) * baseRadius * 0.55f;
            float dripDrop = (0.35f + 0.65f * ((MathUtils.sin(phase * 1.7f) + 1f) * 0.5f)) * baseRadius;
            shapeRenderer.circle(dripX, dripY, baseRadius * 0.24f);
            shapeRenderer.circle(dripX, dripY - dripDrop, baseRadius * 0.17f);
        }
        shapeRenderer.end();
    }

    public void renderDebuffCountdownText(RenderManager renderManager,
                                          SpriteBatch hudBatch,
                                          BitmapFont font,
                                          GlyphLayout layout,
                                          float screenW,
                                          float screenH,
                                          float hudScale,
                                          Player player,
                                          ActiveEffects activeEffects) {
        List<DebuffOverlayInfo> debuffs = buildDebuffOverlayList(player, activeEffects);
        if (debuffs.isEmpty()) {
            return;
        }

        float scale = hudScale;
        float pad = 16f * scale;
        float pillW = 300f * scale;
        float pillH = 64f * scale;
        float pillGap = 10f * scale;
        float pillX = (screenW - pillW) * 0.5f;

        float barY = screenH - pad - (18f * scale);
        float stageTitleY = barY - (24f * scale);
        float firstPillY = stageTitleY - (52f * scale) - pillH;

        hudBatch.end();

        ShapeRenderer shapeRenderer = renderManager.getShapeRenderer();
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < debuffs.size(); i++) {
            DebuffOverlayInfo info = debuffs.get(i);
            float pillY = firstPillY - i * (pillH + pillGap);
            float progress = info.durationSeconds > 0f
                    ? Math.min(1f, info.timerSeconds / info.durationSeconds) : 0f;
            shapeRenderer.setColor(0f, 0f, 0f, 0.55f);
            shapeRenderer.rect(pillX, pillY, pillW, pillH);
            shapeRenderer.setColor(1f, 0.45f + 0.35f * progress, 0f, 0.85f);
            shapeRenderer.rect(pillX, pillY, pillW * progress, pillH * 0.22f);
        }
        shapeRenderer.end();

        hudBatch.begin();
        float textScale = 1.5f * scale;
        font.getData().setScale(textScale);

        for (int i = 0; i < debuffs.size(); i++) {
            DebuffOverlayInfo info = debuffs.get(i);
            float pillY = firstPillY - i * (pillH + pillGap);
            int countdown = Math.max(1, (int) Math.ceil(Math.min(DEBUFF_POPUP_MAX_SECONDS, info.timerSeconds)));
            String label = info.label + "  " + countdown + "s";
            layout.setText(font, label);
            float textX = pillX + (pillW - layout.width) / 2f;
            float textY = pillY + pillH * 0.75f;

            font.setColor(0f, 0f, 0f, 0.8f);
            font.draw(hudBatch, label, textX + 1.5f * scale, textY - 1.5f * scale);
            font.setColor(1f, 0.9f, 0.5f, 1f);
            font.draw(hudBatch, label, textX, textY);
        }

        font.setColor(Color.WHITE);
        hudBatch.end();
        font.getData().setScale(1.7f * scale);
        hudBatch.begin();
    }

    private List<DebuffOverlayInfo> buildDebuffOverlayList(Player player, ActiveEffects activeEffects) {
        debuffOverlayScratch.clear();

        if (player != null) {
            if (player.getReversedFlightTimer() > 0f) {
                debuffOverlayScratch.add(new DebuffOverlayInfo(
                        "Reversed Flight",
                        player.getReversedFlightTimer(),
                        PLASTIC_BOTTLE_DURATION
                ));
            }
            if (player.getJumpIntervalDebuffTimer() > 0f) {
                debuffOverlayScratch.add(new DebuffOverlayInfo(
                        "Slow Jumps",
                        player.getJumpIntervalDebuffTimer(),
                        FACTORY_SMOKE_DURATION
                ));
            }
        }

        if (activeEffects != null) {
            if (activeEffects.getOilBlotTimer() > 0f) {
                debuffOverlayScratch.add(new DebuffOverlayInfo(
                        "Obstructed Vision",
                        activeEffects.getOilBlotTimer(),
                        OIL_BLOT_DURATION
                ));
            }
            if (activeEffects.getTrashRattleTimer() > 0f) {
                debuffOverlayScratch.add(new DebuffOverlayInfo(
                        "Screen Shake",
                        activeEffects.getTrashRattleTimer(),
                        TRASH_PILE_SHAKE_DURATION
                ));
            }
        }

        return debuffOverlayScratch;
    }

    private void generateOilSplotchPattern(RandomManager randomManager) {
        for (int i = 0; i < OIL_SPLOTCH_COUNT; i++) {
            oilSplotchXNorm[i] = randomManager.range(0.05f, 0.95f);
            oilSplotchYNorm[i] = randomManager.range(0.05f, 0.95f);
            oilSplotchSizeNorm[i] = randomManager.range(0.08f, 0.13f);
            oilSplotchRotation[i] = randomManager.range(0f, 360f);
        }
    }

    private static final class DebuffOverlayInfo {
        private final String label;
        private final float timerSeconds;
        private final float durationSeconds;

        private DebuffOverlayInfo(String label, float timerSeconds, float durationSeconds) {
            this.label = label;
            this.timerSeconds = timerSeconds;
            this.durationSeconds = durationSeconds;
        }
    }
}
