package inf1009.p63.flappyearth.game.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.managers.AssetManager;

public class SmokeEffect {

    private static final String SMOKE_ASSET_KEY = "smoke.png";

    private final AssetManager assetManager;
    private final float overlayAlpha;

    public SmokeEffect(AssetManager assetManager, float overlayAlpha) {
        this.assetManager = assetManager;
        this.overlayAlpha = Math.max(0f, Math.min(1f, overlayAlpha));
    }

    public void render(SpriteBatch batch, float screenW, float screenH) {
        if (overlayAlpha <= 0f) return;
        if (assetManager == null || !assetManager.isLoaded(SMOKE_ASSET_KEY)) return;

        Texture smokeTexture = assetManager.getTexture(SMOKE_ASSET_KEY);
        batch.setColor(1f, 1f, 1f, overlayAlpha);
        batch.draw(smokeTexture, 0f, 0f, screenW, screenH);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}