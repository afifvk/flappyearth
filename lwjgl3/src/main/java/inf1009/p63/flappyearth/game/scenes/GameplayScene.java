package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.StageConfig;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.controllers.CameraController;
import inf1009.p63.flappyearth.game.controllers.DeathController;
import inf1009.p63.flappyearth.game.controllers.EndingController;
import inf1009.p63.flappyearth.game.controllers.HelpOverlayController;
import inf1009.p63.flappyearth.game.controllers.PauseOverlayController;
import inf1009.p63.flappyearth.game.controllers.StageProgressController;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.runtime.GameplayAudioController;
import inf1009.p63.flappyearth.game.runtime.GameplayBootstrapper;
import inf1009.p63.flappyearth.game.runtime.GameplayEventHandler;
import inf1009.p63.flappyearth.game.runtime.GameplayHudRenderer;
import inf1009.p63.flappyearth.game.runtime.GameplayRenderCoordinator;
import inf1009.p63.flappyearth.game.runtime.GameplaySceneResources;
import inf1009.p63.flappyearth.game.runtime.GameplayUpdateCoordinator;
import inf1009.p63.flappyearth.game.runtime.GameplayRuntimeContext;
import inf1009.p63.flappyearth.game.runtime.SmokeEffect;
import inf1009.p63.flappyearth.game.runtime.StageTransitionController;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;

public class GameplayScene extends Scene {

    private static final String STAGE_ONE_BACKGROUND_KEY = "textures/backgrounds/stage/stage1.png";
    private static final String STAGE_TWO_BACKGROUND_KEY = "textures/backgrounds/stage/stage2.png";
    private static final String STAGE_THREE_BACKGROUND_KEY = "textures/backgrounds/stage/stage3.png";
    private static final String STAGE_FOUR_BACKGROUND_KEY = "textures/backgrounds/stage/stage4.png";
    private static final float STAGE_TRANSITION_FADE = 0.45f;

    private final SceneManager sceneManager;
    private final EngineContext context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;
    private final StageConfig stageConfig;
    private final GameplayDimensions dimensions;
    private final GameplaySceneResources resources;
    private final GameplayRenderCoordinator renderCoordinator;
    private final GameplayBootstrapper gameplayBootstrapper;
    private final StageTransitionController stageTransitionController;
    private final GameplayUpdateCoordinator gameplayUpdateCoordinator;

    private Player player;
    private GameplayAudioController gameplayAudioController;
    private GameplayHudRenderer hudRenderer;
    private SmokeEffect smokeEffect;
    private inf1009.p63.flappyearth.game.factories.EntityFactory entityFactory;
    private inf1009.p63.flappyearth.game.systems.GameplayLoop gameLoopManager;
    private Viewport worldViewport;
    private StageProgressController stageController;
    private DeathController deathController;
    private CameraController cameraController;
    private EndingController endingSceneController;
    private GameplayEventHandler gameplayEventHandler;
    private ActiveEffects activeEffects;
    private GameplayRuntimeContext runtimeContext;
    private Texture heartFullTexture;
    private Texture heartEmptyTexture;
    private PauseOverlayController pauseOverlayController;
    private HelpOverlayController helpOverlayController;

    public GameplayScene(SceneManager sceneManager,
                         EngineContext context,
                         GameSession gameSession,
                         StagePlan stagePlan,
                         StageConfig stageConfig,
                         GameplayDimensions dimensions) {
        this.sceneManager = sceneManager;
        this.context = context;
        this.gameSession = gameSession;
        this.stagePlan = stagePlan;
        this.stageConfig = stageConfig;
        this.dimensions = dimensions;
        this.resources = new GameplaySceneResources(context);
        this.renderCoordinator = new GameplayRenderCoordinator();
        this.gameplayBootstrapper = new GameplayBootstrapper();
        this.stageTransitionController = new StageTransitionController(STAGE_TRANSITION_FADE);
        this.gameplayUpdateCoordinator = new GameplayUpdateCoordinator(
                sceneManager,
                context,
                gameSession,
                stagePlan,
                stageTransitionController,
                new inf1009.p63.flappyearth.game.runtime.GameplaySession()
        );
    }

    @Override
    public void onEnter() {
        try {
            gameSession.prepareForStageEntry();
            if (!context.getAudioManager().isMusicPlaying()) {
                context.getAudioManager().playMusic(AudioKeys.MUSIC_GAME);
            }

            if (hudRenderer != null) {
                hudRenderer.dispose();
                hudRenderer = null;
            }
            resources.entityStore().clear();

            activeEffects = gameSession.getActiveEffects();
            gameplayEventHandler = new GameplayEventHandler(
                    context,
                    resources.entityStore(),
                    activeEffects,
                    () -> this.player,
                    () -> this.entityFactory
            );
            gameplayEventHandler.subscribe(gameSession);

            GameplayBootstrapper.Result bootstrap = gameplayBootstrapper.bootstrap(
                    sceneManager,
                    context,
                    gameSession,
                    stagePlan,
                    stageConfig,
                    dimensions,
                    resources.entityStore(),
                    activeEffects
            );

            worldViewport = bootstrap.worldViewport;
            stageController = bootstrap.stageController;
            deathController = bootstrap.deathController;
            cameraController = bootstrap.cameraController;
            endingSceneController = bootstrap.endingController;
            gameplayAudioController = bootstrap.gameplayAudioController;
            hudRenderer = bootstrap.hudRenderer;
            smokeEffect = bootstrap.smokeEffect;
            heartFullTexture = bootstrap.heartFullTexture;
            heartEmptyTexture = bootstrap.heartEmptyTexture;
            pauseOverlayController = bootstrap.pauseOverlayController;
            helpOverlayController = bootstrap.helpOverlayController;
            entityFactory = bootstrap.entityFactory;
            player = bootstrap.player;
            runtimeContext = bootstrap.runtimeContext;
            gameLoopManager = bootstrap.gameplayLoop;

            resources.prepareForStage(context, resolveStageBackgroundKey(stageConfig.getSceneId()));
            gameplayUpdateCoordinator.onEnter(gameSession.consumeFadeInRequest(), player);
            gameplayAudioController.onEnter(() -> this.player);
        } catch (Exception ex) {
            Gdx.app.error("GameplayScene", "Failed to enter gameplay scene for " + stageConfig.getSceneId(), ex);
            cleanupAfterFailedEnter();
            throw new IllegalStateException("Failed to initialize gameplay scene: " + stageConfig.getSceneId(), ex);
        }
    }

    @Override
    public void update(float delta) {
        gameplayUpdateCoordinator.update(
                delta,
                runtimeContext,
                endingSceneController,
                helpOverlayController,
                pauseOverlayController,
                gameLoopManager,
                deathController,
                cameraController,
                stageController,
                gameSession
        );
        player = gameplayUpdateCoordinator.getPlayer();
    }

    @Override
    public void render() {
        if (runtimeContext == null) {
            return;
        }
        renderCoordinator.render(
                stageConfig,
                dimensions,
                context,
                gameSession,
                resources,
                worldViewport,
                cameraController,
                player,
                activeEffects,
                smokeEffect,
                hudRenderer,
                heartFullTexture,
                heartEmptyTexture,
                pauseOverlayController,
                helpOverlayController,
                endingSceneController,
                gameplayUpdateCoordinator
        );
    }

    @Override
    public void onExit() {
        if (gameplayEventHandler != null) {
            gameplayEventHandler.unsubscribe();
            gameplayEventHandler = null;
        }
        if (gameplayAudioController != null) {
            gameplayAudioController.onExit();
            gameplayAudioController = null;
        }
    }

    @Override
    public void disposeResources() {
        super.disposeResources();
        if (hudRenderer != null) {
            hudRenderer.dispose();
            hudRenderer = null;
        }
        resources.dispose();
    }

    private String resolveStageBackgroundKey(String sceneId) {
        if (GameSceneId.STAGE_ONE.id().equals(sceneId)) return STAGE_ONE_BACKGROUND_KEY;
        if (GameSceneId.STAGE_TWO.id().equals(sceneId)) return STAGE_TWO_BACKGROUND_KEY;
        if (GameSceneId.STAGE_THREE.id().equals(sceneId)) return STAGE_THREE_BACKGROUND_KEY;
        if (GameSceneId.STAGE_FOUR.id().equals(sceneId)) return STAGE_FOUR_BACKGROUND_KEY;
        return STAGE_ONE_BACKGROUND_KEY;
    }

    private void cleanupAfterFailedEnter() {
        if (gameplayEventHandler != null) {
            gameplayEventHandler.unsubscribe();
            gameplayEventHandler = null;
        }
        if (gameplayAudioController != null) {
            gameplayAudioController.onExit();
            gameplayAudioController = null;
        }
        if (hudRenderer != null) {
            try {
                hudRenderer.dispose();
            } catch (Exception ex) {
                Gdx.app.error("GameplayScene", "Failed to dispose HUD renderer after enter failure", ex);
            }
            hudRenderer = null;
        }
        resources.entityStore().clear();
        runtimeContext = null;
        player = null;
    }
}
