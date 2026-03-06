package inf1009.p63.flappyearth.game.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SmokeEffect {
    private Texture smokeTexture;
    private float alpha; // Visibility level

    public SmokeEffect(float startingAlpha) {
        // Ensure you have a 'smoke.png' in your assets folder!
        this.smokeTexture = new Texture(Gdx.files.internal("assets/smoke.png"));
        this.alpha = startingAlpha;
    }

    public void adjustVisibility(float amount) {
        this.alpha += amount;
        // Clamp values between 0.1 (clean) and 1.0 (thick smog)
        if (this.alpha < 0.1f) this.alpha = 0.1f;
        if (this.alpha > 1.0f) this.alpha = 1.0f;
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
}