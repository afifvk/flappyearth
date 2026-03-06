package inf1009.p63.flappyearth.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.game.state.ActiveEffects;

public class HudManager {

    private final BitmapFont   font;
    private final ScoreManager scoreManager;
    private final ActiveEffects activeEffects;

    public HudManager(ScoreManager scoreManager, ActiveEffects activeEffects) {
        this.scoreManager  = scoreManager;
        this.activeEffects = activeEffects;
        this.font          = new BitmapFont();
    }

    public void render(SpriteBatch batch, float screenH) {
        // Display current score
        font.draw(batch, scoreManager.getScoreText(), 10f, screenH - 10f);

        // Show shield timer if active
        if (activeEffects.isShieldActive()) {
            font.draw(batch, "SHIELD: " + String.format("%.1f", activeEffects.getShieldTimer()) + "s",
                      10f, screenH - 30f);
        }
        // Show slow-motion timer if active
        if (activeEffects.isSlowTimeActive()) {
            font.draw(batch, "SLOW: " + String.format("%.1f", activeEffects.getSlowTimeTimer()) + "s",
                      10f, screenH - 50f);
        }
    }

    public void dispose() {
        if (font != null) font.dispose();
    }
}
