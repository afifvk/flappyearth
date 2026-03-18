package inf1009.p63.flappyearth.engine.services;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.model.RenderData;

import java.util.List;

public class RenderManager {

    public interface SourceRegionResolver {
        int[] resolve(RenderData renderData, Texture texture);
    }

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private AssetManager assetManager;
    private SourceRegionResolver sourceRegionResolver;

    public RenderManager() {
        batch         = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setSourceRegionResolver(SourceRegionResolver sourceRegionResolver) {
        this.sourceRegionResolver = sourceRegionResolver;
    }

    public void render(List<Renderable> renderables) {
        batch.begin();
        for (Renderable r : renderables) {
            RenderData d = r.getRenderData();
            if (d == null) continue;
            if (assetManager != null && assetManager.isLoaded(d.assetKey)) {
                Texture tex = assetManager.getTexture(d.assetKey);
                int srcX = 0;
                int srcY = 0;
                int srcW = tex.getWidth();
                int srcH = tex.getHeight();
                if (sourceRegionResolver != null) {
                    int[] bounds = sourceRegionResolver.resolve(d, tex);
                    if (bounds != null && bounds.length == 4) {
                    srcX = bounds[0];
                    srcY = bounds[1];
                    srcW = bounds[2];
                    srcH = bounds[3];
                    }
                }
                batch.draw(tex,
                           d.x, d.y,
                           d.width / 2f, d.height / 2f,
                           d.width, d.height,
                           1f, 1f,
                           d.rotationDegrees,
                           srcX, srcY, srcW, srcH,
                           false, d.isFlipped);
            }
        }
        batch.end();

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
            int srcX = 0;
            int srcY = 0;
            int srcW = tex.getWidth();
            int srcH = tex.getHeight();
            if (sourceRegionResolver != null) {
                int[] bounds = sourceRegionResolver.resolve(d, tex);
                if (bounds != null && bounds.length == 4) {
                srcX = bounds[0];
                srcY = bounds[1];
                srcW = bounds[2];
                srcH = bounds[3];
                }
            }
            batch.begin();
            batch.draw(tex,
                       d.x, d.y,
                       d.width / 2f, d.height / 2f,
                       d.width, d.height,
                       1f, 1f,
                       d.rotationDegrees,
                       srcX, srcY, srcW, srcH,
                       false, d.isFlipped);
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
