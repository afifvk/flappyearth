package inf1009.p63.flappyearth.game.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.RandomManager;
import inf1009.p63.flappyearth.engine.services.RenderManager;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.controllers.CameraController;
import inf1009.p63.flappyearth.game.state.ActiveEffects;

import java.util.List;

public class GameplayRenderer {

    public static final class WorldRenderInfo {
        public float worldW;
        public float worldH;
        public float cameraLeft;
        public float cameraBottom;
    }

    public WorldRenderInfo renderWorld(RenderManager renderManager,
                                       EntityStore entityStore,
                                       CameraController cameraController,
                                       GameplayDimensions dimensions,
                                       ActiveEffects activeEffects,
                                       RandomManager randomManager,
                                       List<Renderable> renderablesScratch,
                                       com.badlogic.gdx.graphics.Texture stageBackgroundTexture,
                                       SmokeEffect smokeEffect) {
        renderManager.getBatch().setProjectionMatrix(cameraController.getCamera().combined);
        renderManager.getShapeRenderer().setProjectionMatrix(cameraController.getCamera().combined);

        if (activeEffects != null && activeEffects.isScreenShaking()) {
            float mag = activeEffects.getScreenShakeMagnitude();
            float ox = randomManager.range(-mag, mag);
            float oy = randomManager.range(-mag, mag);
            cameraController.getCamera().position.add(ox, oy, 0f);
            cameraController.getCamera().update();
            renderManager.getBatch().setProjectionMatrix(cameraController.getCamera().combined);
            renderManager.getShapeRenderer().setProjectionMatrix(cameraController.getCamera().combined);
        }

        WorldRenderInfo info = new WorldRenderInfo();
        info.worldW = dimensions.getWorldWidth();
        info.worldH = dimensions.getWorldHeight();
        info.cameraLeft = cameraController.getCamera().position.x - (info.worldW / 2f);
        info.cameraBottom = cameraController.getCamera().position.y - (info.worldH / 2f);

        if (stageBackgroundTexture != null) {
            SpriteBatch worldBatch = renderManager.getBatch();
            worldBatch.begin();
            worldBatch.draw(stageBackgroundTexture, info.cameraLeft, info.cameraBottom, info.worldW, info.worldH);
            worldBatch.end();
        }

        entityStore.collectRenderables(renderablesScratch);
        renderManager.render(renderablesScratch);

        SpriteBatch worldBatch = renderManager.getBatch();
        worldBatch.begin();
        if (smokeEffect != null) {
            float smokeAlpha = activeEffects != null
                    ? activeEffects.getSmokeOverlayAlpha(smokeEffect.getBaseOverlayAlpha())
                    : smokeEffect.getBaseOverlayAlpha();
            smokeEffect.render(worldBatch, info.cameraLeft, info.cameraBottom, info.worldW, info.worldH, smokeAlpha);
        }
        worldBatch.end();

        return info;
    }

    public void setupHudProjection(RenderManager renderManager, float screenW, float screenH) {
        SpriteBatch hudBatch = renderManager.getBatch();
        Gdx.gl.glViewport(0, 0, (int) screenW, (int) screenH);
        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        renderManager.getShapeRenderer().setProjectionMatrix(hudBatch.getProjectionMatrix());
    }

    public void renderStageTransitionOverlay(RenderManager renderManager, float screenW, float screenH, float alpha) {
        if (alpha <= 0f) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer shapeRenderer = renderManager.getShapeRenderer();
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, alpha);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
