package inf1009.p63.flappyearth.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.game.config.GameSettings;

public class BrightnessOverlayRenderer {

    private final Texture whitePixel;

    public BrightnessOverlayRenderer() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }

    public void render(SpriteBatch batch, GameSettings settings) {
        float brightness = settings.getBrightness();
        if (brightness == 1.0f) {
            return;
        }

        float overlayAlpha;
        Color overlayColor;
        if (brightness < 1.0f) {
            overlayColor = Color.BLACK;
            overlayAlpha = 1.0f - brightness;
        } else {
            overlayColor = Color.WHITE;
            overlayAlpha = (brightness - 1.0f) * 0.35f;
        }

        batch.setColor(overlayColor.r, overlayColor.g, overlayColor.b, overlayAlpha);
        batch.draw(whitePixel, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);
    }

    public void dispose() {
        whitePixel.dispose();
    }
}
