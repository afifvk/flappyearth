package inf1009.p63.flappyearth.game.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.EnvironmentProgress;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.ArrayList;
import java.util.List;

public class HudRenderer {

    private static final float BAR_WIDTH  = 280f;
    private static final float BAR_HEIGHT = 18f;
    private static final float PADDING    = 16f;

    private final BitmapFont        font;
    private final BitmapFont        smallFont;
    private final GlyphLayout       layout;
    private final ActiveEffects     activeEffects;
    private final GameState         gameState;
    private final ScoreManager      scoreManager;
    private final EnvironmentProgress environmentProgress;
    private final List<Integer>     checkpointTargets;
    private String stageTitle;

    public HudRenderer(ActiveEffects activeEffects,
                       GameState gameState,
                       ScoreManager scoreManager,
                       EnvironmentProgress environmentProgress,
                       List<Integer> checkpointTargets,
                       String stageTitle) {
        this.activeEffects        = activeEffects;
        this.gameState            = gameState;
        this.scoreManager         = scoreManager;
        this.environmentProgress  = environmentProgress;
        this.checkpointTargets    = new ArrayList<>(checkpointTargets);
        this.stageTitle           = stageTitle;

        this.font = new BitmapFont();
        this.font.getData().setScale(2.6f);
        this.font.setUseIntegerPositions(false);
        enableSmoothing(this.font);

        this.smallFont = new BitmapFont();
        this.smallFont.getData().setScale(1.8f);
        this.smallFont.setUseIntegerPositions(false);
        enableSmoothing(this.smallFont);

        this.layout = new GlyphLayout();
    }

    public void setStageTitle(String t) { this.stageTitle = t; }

    public void render(ShapeRenderer sr, SpriteBatch batch,
                       float screenW, float screenH) {

        float s = screenH / 1080f;

        font.getData().setScale(2.6f * s);
        smallFont.getData().setScale(1.8f * s);

        float pad  = PADDING * s;
        float barW = BAR_WIDTH * s;
        float barH = BAR_HEIGHT * s;

        float barX = (screenW - barW) / 2f;
        float barY = screenH - pad - barH;

        float fill  = barW * environmentProgress.getProgressRatio();
        float ratio = environmentProgress.getProgressRatio();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.setColor(0f, 0f, 0f, 0.7f);
        sr.rect(barX - 3f * s, barY - 3f * s, barW + 6f * s, barH + 6f * s);

        sr.setColor(0.15f, 0.15f, 0.15f, 1f);
        sr.rect(barX, barY, barW, barH);

        sr.setColor(1f - ratio, 0.3f + ratio * 0.7f, 0.1f, 1f);
        if (fill > 0) sr.rect(barX, barY, fill, barH);

        int cpCount = checkpointTargets.size();
        for (int i = 0; i < cpCount; i++) {
            float tickX = barX + barW * ((i + 1f) / (cpCount + 1f));
            boolean passed = environmentProgress.getGoodCollectiblesCollected()
                    >= checkpointTargets.get(i);
            sr.setColor(passed ? Color.GOLD : Color.WHITE);
            sr.rect(tickX - 2f * s, barY - 5f * s, 4f * s, barH + 10f * s);
        }

        sr.end();

        batch.begin();

        layout.setText(smallFont, stageTitle);
        float titleX = (screenW - layout.width) / 2f;
        float titleY = barY - 24f * s;
        drawShadowed(batch, smallFont, stageTitle, titleX, titleY, s);

        String scoreText = "SCORE: " + scoreManager.getCurrentScore();
        layout.setText(font, scoreText);
        float scoreX = screenW - pad - layout.width;
        float scoreY = screenH - pad;
        drawShadowed(batch, font, scoreText, scoreX, scoreY, s);

        float statusY = screenH - 100f * s;
        if (activeEffects.isShieldActive()) {
            drawShadowed(batch, smallFont,
                    "SHIELD  " + String.format("%.1fs", activeEffects.getShieldTimer()),
                    pad, statusY, s);
            statusY -= 32f * s;
        }
        if (activeEffects.isSlowTimeActive()) {
            drawShadowed(batch, smallFont,
                    "SLOW  " + String.format("%.1fs", activeEffects.getSlowTimeTimer()),
                    pad, statusY, s);
        }

        batch.end();
    }

    private void drawShadowed(SpriteBatch b, BitmapFont f,
                               String text, float x, float y, float s) {
        f.setColor(0f, 0f, 0f, 0.75f);
        f.draw(b, text, x + 2f * s, y - 2f * s);
        f.setColor(Color.WHITE);
        f.draw(b, text, x, y);
    }

    public void dispose() {
        if (font      != null) font.dispose();
        if (smallFont != null) smallFont.dispose();
    }

    private void enableSmoothing(BitmapFont f) {
        for (TextureRegion r : f.getRegions()) {
            r.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}