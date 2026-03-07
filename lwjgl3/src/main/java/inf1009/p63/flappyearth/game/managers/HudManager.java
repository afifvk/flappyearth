package inf1009.p63.flappyearth.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameState;

public class HudManager {

    private final BitmapFont    font;
    private final ActiveEffects activeEffects;
    private final GameState     gameState;

    public HudManager(ActiveEffects activeEffects, GameState gameState) {
        this.activeEffects = activeEffects;
        this.gameState     = gameState;
        this.font          = new BitmapFont();
    }

    public void render(SpriteBatch batch, float screenH) {
        StringBuilder hearts = new StringBuilder("Hearts: ");
        for (int i = 0; i < gameState.getMaxHearts(); i++) {
            hearts.append(i < gameState.getHearts() ? "<3 " : "_ ");
        }
        font.draw(batch, hearts.toString(), 10f, screenH - 10f);

        if (activeEffects.isShieldActive()) {
            font.draw(batch, "SHIELD: " + String.format("%.1f", activeEffects.getShieldTimer()) + "s",
                      10f, screenH - 30f);
        }
        if (activeEffects.isSlowTimeActive()) {
            font.draw(batch, "SLOW: " + String.format("%.1f", activeEffects.getSlowTimeTimer()) + "s",
                      10f, screenH - 50f);
        }
    }

    public void dispose() {
        if (font != null) font.dispose();
    }
}