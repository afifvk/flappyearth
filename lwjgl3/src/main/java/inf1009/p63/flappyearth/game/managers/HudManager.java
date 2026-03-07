package inf1009.p63.flappyearth.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.EnvironmentProgress;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.ArrayList;
import java.util.List;

public class HudManager {

    private static final float HUD_PADDING = 12f;
    private static final float HEADER_Y_OFFSET = 12f;
    private static final float LINE_GAP = 22f;
    private static final float BAR_WIDTH = 260f;
    private static final float BAR_HEIGHT = 14f;

    private final BitmapFont    font;
    private final GlyphLayout   layout;
    private final ActiveEffects activeEffects;
    private final GameState     gameState;
    private final ScoreManager  scoreManager;
    private final EnvironmentProgress environmentProgress;
    private final List<Integer> checkpointTargets;
    private String stageTitle;

    public HudManager(ActiveEffects activeEffects,
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
        this.layout = new GlyphLayout();
    }

    public void setStageTitle(String stageTitle) {
        this.stageTitle = stageTitle;
    }

    public void render(ShapeRenderer shapeRenderer,
                       SpriteBatch batch,
                       float screenW,
                       float screenH) {
        float headerY = screenH - HEADER_Y_OFFSET;
        float stageY = headerY;
        float healthY = headerY;
        float distanceY = healthY - LINE_GAP;
        float barY = stageY - LINE_GAP - 18f;
        float statusY = barY - 16f;
        float barX = HUD_PADDING;
        float fillWidth = BAR_WIDTH * environmentProgress.getProgressRatio();

        String healthText = "Health: " + gameState.getHearts();
        String distanceText = "Distance: " + scoreManager.getCurrentScore();

        layout.setText(font, healthText);
        float healthX = screenW - HUD_PADDING - layout.width;
        layout.setText(font, distanceText);
        float distanceX = screenW - HUD_PADDING - layout.width;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.18f, 0.18f, 0.18f, 0.9f);
        shapeRenderer.rect(barX, barY, BAR_WIDTH, BAR_HEIGHT);
        shapeRenderer.setColor(0.25f, 0.78f, 0.38f, 1f);
        shapeRenderer.rect(barX, barY, fillWidth, BAR_HEIGHT);
        for (Integer checkpointTarget : checkpointTargets) {
            if (environmentProgress.getGoodCollectiblesCollected() >= checkpointTarget) {
                shapeRenderer.setColor(0.12f, 0.9f, 0.35f, 1f);
            } else {
                shapeRenderer.setColor(0.95f, 0.95f, 0.95f, 1f);
            }
            float checkpointX = barX + (BAR_WIDTH * checkpointTarget
                    / (float) environmentProgress.getMaxGoodCollectibles());
            shapeRenderer.rect(checkpointX - 1f, barY - 3f, 2f, BAR_HEIGHT + 6f);
        }
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, stageTitle, HUD_PADDING, stageY);
        font.draw(batch, healthText, healthX, healthY);
        font.draw(batch, distanceText, distanceX, distanceY);

        if (activeEffects.isShieldActive()) {
            font.draw(batch, "SHIELD: " + String.format("%.1f", activeEffects.getShieldTimer()) + "s",
                      HUD_PADDING, statusY);
        }
        if (activeEffects.isSlowTimeActive()) {
            font.draw(batch, "SLOW: " + String.format("%.1f", activeEffects.getSlowTimeTimer()) + "s",
                      HUD_PADDING + 120f, statusY);
        }
        batch.end();
    }

    public void dispose() {
        if (font != null) font.dispose();
    }
}