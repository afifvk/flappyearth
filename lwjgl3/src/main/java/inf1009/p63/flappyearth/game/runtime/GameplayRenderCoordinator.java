package inf1009.p63.flappyearth.game.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.StageConfig;
import inf1009.p63.flappyearth.game.controllers.CameraController;
import inf1009.p63.flappyearth.game.controllers.EndingController;
import inf1009.p63.flappyearth.game.controllers.HelpOverlayController;
import inf1009.p63.flappyearth.game.controllers.PauseOverlayController;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;

public class GameplayRenderCoordinator {

    private final GameplayRenderer gameplayRenderer = new GameplayRenderer();
    private final GameplayOverlayRenderer gameplayOverlayRenderer = new GameplayOverlayRenderer();

    public void render(StageConfig stageConfig,
                       GameplayDimensions dimensions,
                       EngineContext context,
                       GameSession gameSession,
                       GameplaySceneResources resources,
                       Viewport worldViewport,
                       CameraController cameraController,
                       Player player,
                       ActiveEffects activeEffects,
                       SmokeEffect smokeEffect,
                       GameplayHudRenderer hudRenderer,
                       com.badlogic.gdx.graphics.Texture heartFullTexture,
                       com.badlogic.gdx.graphics.Texture heartEmptyTexture,
                       PauseOverlayController pauseOverlayController,
                       HelpOverlayController helpOverlayController,
                       EndingController endingSceneController,
                       GameplayUpdateCoordinator gameplayUpdateCoordinator) {
        if (worldViewport == null || cameraController == null || hudRenderer == null) {
            return;
        }

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        resources.updateViewportIfNeeded(worldViewport, (int) screenW, (int) screenH);

        Gdx.gl.glClearColor(stageConfig.getClearR(), stageConfig.getClearG(), stageConfig.getClearB(), 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (player != null) {
            cameraController.apply(player);
        }
        worldViewport.apply(false);

        gameplayRenderer.renderWorld(
                resources.renderManager(),
                resources.entityStore(),
                cameraController,
                dimensions,
                activeEffects,
                context.getRandomManager(),
                resources.renderablesScratch(),
                resources.stageBackgroundTexture(),
                smokeEffect
        );

        gameplayRenderer.setupHudProjection(resources.renderManager(), screenW, screenH);
        SpriteBatch hudBatch = resources.renderManager().getBatch();
        hudRenderer.render(resources.renderManager().getShapeRenderer(), hudBatch, screenW, screenH);

        resources.effectsOverlayRenderer().renderOilOverlay(
                resources.renderManager(),
                screenW,
                screenH,
                activeEffects,
                context.getRandomManager()
        );

        float hudScale = screenH / 1080f;
        resources.introFont().getData().setScale(1.7f * hudScale);

        hudBatch.begin();

        gameplayOverlayRenderer.renderHearts(
                hudBatch,
                context.getRandomManager(),
                gameSession.getGameState(),
                player,
                heartFullTexture,
                heartEmptyTexture,
                screenH,
                hudScale
        );

        resources.effectsOverlayRenderer().renderDebuffCountdownText(
                resources.renderManager(),
                hudBatch,
                resources.introFont(),
                resources.introLayout(),
                screenW,
                screenH,
                hudScale,
                player,
                activeEffects
        );

        gameplayOverlayRenderer.renderHudTextOverlays(
                hudBatch,
                resources.introFont(),
                resources.introLayout(),
                screenW,
                screenH,
                hudScale,
                gameplayUpdateCoordinator.isShowIntroText(),
                gameplayUpdateCoordinator.getIntroTimer(),
                stageConfig.getIntroLines(),
                gameplayUpdateCoordinator.isPaused(),
                gameplayUpdateCoordinator.isShowingInstructionsOverlay(),
                endingSceneController
        );

        hudBatch.end();

        gameplayOverlayRenderer.renderModalAndFinalOverlays(
                resources.renderManager(),
                gameplayRenderer,
                resources.brightnessOverlayRenderer(),
                pauseOverlayController,
                helpOverlayController,
                screenW,
                screenH,
                gameplayUpdateCoordinator.isPaused(),
                gameplayUpdateCoordinator.isShowingInstructionsOverlay(),
                context.getBrightness(),
                gameplayUpdateCoordinator.getTransitionAlpha()
        );
    }
}
