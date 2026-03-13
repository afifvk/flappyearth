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

    private static final float HUD_PADDING = 20f;
    private static final float HEADER_Y_OFFSET = 20f;
    private static final float LINE_GAP = 30f;
    private static final float BAR_WIDTH = 300f;
    private static final float BAR_HEIGHT = 22f; // Thicker game bar

    private final BitmapFont    font;
    private final GlyphLayout   layout;
    private final ActiveEffects activeEffects;
    private final GameState     gameState;
    private final ScoreManager  scoreManager;
    private final EnvironmentProgress environmentProgress;
    private final List<Integer> checkpointTargets;
    private String stageTitle;

    public HudRenderer(ActiveEffects activeEffects,
                       GameState gameState,
                       ScoreManager scoreManager,
                       EnvironmentProgress environmentProgress,
                       List<Integer> checkpointTargets,
                       String stageTitle) {
        this.activeEffects = activeEffects;
        this.gameState     = gameState;
        this.scoreManager = scoreManager;
        this.environmentProgress = environmentProgress;
        this.checkpointTargets = new ArrayList<>(checkpointTargets);
        this.stageTitle = stageTitle;
        
        this.font = new BitmapFont();
        this.font.getData().setScale(1.6f); 
        enableFontSmoothing(this.font);
        this.layout = new GlyphLayout();
    }

    public void setStageTitle(String stageTitle) {
        this.stageTitle = stageTitle;
    }

    public void render(ShapeRenderer shapeRenderer,
                       SpriteBatch batch,
                       float screenW,
                       float screenH) {

        float s = screenH / 1080f;
        float padding   = HUD_PADDING     * s;
        float yOffset   = HEADER_Y_OFFSET * s;
        float lineGap   = LINE_GAP        * s;
        float barWidth  = BAR_WIDTH       * s;
        float barHeight = BAR_HEIGHT      * s;
        font.getData().setScale(1.6f * s);

        float topY = screenH - yOffset;

        // Position the progress bar topcenter
        float barX = (screenW - barWidth) / 2f;
        float barY = topY - barHeight;

        float fillWidth = barWidth * environmentProgress.getProgressRatio();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX - 4f * s, barY - 4f * s, barWidth + 8f * s, barHeight + 8f * s);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        float progressRatio = environmentProgress.getProgressRatio();
        shapeRenderer.setColor(1f - progressRatio, progressRatio + 0.3f, 0.2f, 1f);
        shapeRenderer.rect(barX, barY, fillWidth, barHeight);
        int checkpointCount = checkpointTargets.size();
        for (int i = 0; i < checkpointCount; i++) {
            int checkpointTarget = checkpointTargets.get(i);
            float checkpointX = barX + (barWidth * ((i + 1f) / (checkpointCount + 1f)));

            boolean isPassed = environmentProgress.getGoodCollectiblesCollected() >= checkpointTarget;

            if (isPassed) {
                shapeRenderer.setColor(Color.GOLD);
            } else {
                shapeRenderer.setColor(Color.WHITE);
            }
            shapeRenderer.rect(checkpointX - 2f * s, barY - 6f * s, 4f * s, barHeight + 12f * s);
        }
        shapeRenderer.end();

        batch.begin();
        layout.setText(font, stageTitle);
        float titleX = (screenW - layout.width) / 2f;
        float titleY = barY - 15f * s;
        drawTextWithShadow(batch, stageTitle, titleX, titleY, s);
        String distanceText = "SCORE: " + scoreManager.getCurrentScore();
        layout.setText(font, distanceText);
        float distanceX = screenW - padding - layout.width;
        drawTextWithShadow(batch, distanceText, distanceX, topY, s);
        float statusY = topY - 80f * s;
        if (activeEffects.isShieldActive()) {
            drawTextWithShadow(batch, "SHIELD: " + String.format("%.1f", activeEffects.getShieldTimer()) + "s",
                      padding, statusY, s);
            statusY -= lineGap;
        }
        if (activeEffects.isSlowTimeActive()) {
            drawTextWithShadow(batch, "SLOW: " + String.format("%.1f", activeEffects.getSlowTimeTimer()) + "s",
                      padding, statusY, s);
        }

        batch.end();
    }
    private void drawTextWithShadow(SpriteBatch batch, String text, float x, float y, float s) {
        font.setColor(Color.BLACK);
        font.draw(batch, text, x + 2f * s, y - 2f * s);
        font.setColor(Color.WHITE);
        font.draw(batch, text, x, y);
    }

    public void dispose() {
        if (font != null) font.dispose();
    }

    private void enableFontSmoothing(BitmapFont bitmapFont) {
        for (TextureRegion region : bitmapFont.getRegions()) {
            region.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}
