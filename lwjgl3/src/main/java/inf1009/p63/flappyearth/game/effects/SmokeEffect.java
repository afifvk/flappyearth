package inf1009.p63.flappyearth.game.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SmokeEffect {
    private static final float MIN_ALPHA = 0.1f;
    private static final float MAX_ALPHA = 1.0f;

    private Texture smokeTexture;
    private float alpha; // Visibility level

    public SmokeEffect(float startingAlpha) {
        // Ensure you have a 'smoke.png' in your assets folder!
        this.smokeTexture = new Texture(Gdx.files.internal("smoke.png"));
        this.alpha = clampAlpha(startingAlpha);
    }

    public void adjustVisibility(float amount) {
        this.alpha = clampAlpha(this.alpha + amount);
    }

    public void render(SpriteBatch batch, float playerX, float screenW, float screenH) {
    batch.setColor(1, 1, 1, alpha);
    
    // This aligns the smoke center with the camera's current focus
    // Adjust the "offset" (screenW / 4f) to match your camera positioning
    float drawX = playerX - (screenW / 4f); 
    
    batch.draw(smokeTexture, drawX, 0, screenW, screenH);
    
    batch.setColor(1, 1, 1, 1);
    }

    public void dispose() {
        if (smokeTexture != null) smokeTexture.dispose();
    }

    private float clampAlpha(float value) {
        if (value < MIN_ALPHA) return MIN_ALPHA;
        if (value > MAX_ALPHA) return MAX_ALPHA;
        return value;
    }
}