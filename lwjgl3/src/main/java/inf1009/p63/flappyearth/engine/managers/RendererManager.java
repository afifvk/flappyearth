package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.entities.RenderData;

import java.util.List;

public class RendererManager {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private AssetManager assetManager;

    public RendererManager() {
        batch         = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void render(List<Renderable> renderables) {
        // Draw textured assets
        batch.begin();
        for (Renderable r : renderables) {
            RenderData d = r.getRenderData();
            if (d == null) continue;
            if (assetManager != null && assetManager.isLoaded(d.assetKey)) {
                Texture tex = assetManager.getTexture(d.assetKey);
                //flip vertically (check)
                batch.draw(tex, d.x, d.y, d.width, d.height, 0, 0, tex.getWidth(), tex.getHeight(), false, d.isFlipped);
            }
        }
        batch.end();

        // Draw colored rectangles 
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Renderable r : renderables) {
            RenderData d = r.getRenderData();
            if (d == null) continue;
            if (assetManager == null || !assetManager.isLoaded(d.assetKey)) {
                shapeRenderer.setColor(d.r, d.g, d.b, 1f);
                shapeRenderer.rect(d.x, d.y, d.width, d.height);
            }
        }
        shapeRenderer.end();
    }

    public void renderOne(Renderable r) {
        RenderData d = r.getRenderData();
        if (d == null) return;
        if (assetManager != null && assetManager.isLoaded(d.assetKey)) {
            Texture tex = assetManager.getTexture(d.assetKey);
            batch.begin();
            // flip vertically
            batch.draw(tex, d.x, d.y, d.width, d.height, 0, 0, tex.getWidth(), tex.getHeight(), false, d.isFlipped);
            batch.end();
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(d.r, d.g, d.b, 1f);
            shapeRenderer.rect(d.x, d.y, d.width, d.height);
            shapeRenderer.end();
        }
    }

    public SpriteBatch getBatch() { return batch; }

    public ShapeRenderer getShapeRenderer() { return shapeRenderer; }

    public void dispose() {
        if (batch         != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
