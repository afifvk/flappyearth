package inf1009.p63.flappyearth.game.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.game.config.AssetKeys;
import inf1009.p63.flappyearth.game.config.GameConfig;
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
import inf1009.p63.flappyearth.game.factories.EntityFactory;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.systems.CollisionStep;
import inf1009.p63.flappyearth.game.systems.DespawnStep;
import inf1009.p63.flappyearth.game.systems.EffectStep;
import inf1009.p63.flappyearth.game.systems.GameplayLoop;
import inf1009.p63.flappyearth.game.systems.InputStep;
import inf1009.p63.flappyearth.game.systems.MovementStep;
import inf1009.p63.flappyearth.game.systems.SpawnStep;
import inf1009.p63.flappyearth.game.systems.UpdateStep;

public class GameplayBootstrapper {

    public static final class Result {
        public OrthographicCamera worldCamera;
        public Viewport worldViewport;
        public StageProgressController stageController;
        public DeathController deathController;
        public CameraController cameraController;
        public EndingController endingController;
        public GameplayHudRenderer hudRenderer;
        public SmokeEffect smokeEffect;
        public Texture heartFullTexture;
        public Texture heartEmptyTexture;
        public PauseOverlayController pauseOverlayController;
        public HelpOverlayController helpOverlayController;
        public GameplayAudioController gameplayAudioController;
        public EntityFactory entityFactory;
        public Player player;
        public GameplayLoop gameplayLoop;
        public GameplayRuntimeContext runtimeContext;
    }

    public Result bootstrap(SceneManager sceneManager,
                            EngineContext context,
                            GameSession gameSession,
                            StagePlan stagePlan,
                            StageConfig stageConfig,
                            GameplayDimensions dimensions,
                            EntityStore entityStore,
                            ActiveEffects activeEffects) {
        Result result = new Result();

        result.worldCamera = new OrthographicCamera();
        result.worldViewport = new StretchViewport(dimensions.getWorldWidth(), dimensions.getWorldHeight(), result.worldCamera);
        result.worldViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        result.worldCamera.update();

        result.stageController = new StageProgressController(stagePlan, stageConfig, gameSession.getEnvironmentProgress(), context.getAudioManager());
        result.deathController = new DeathController(sceneManager, gameSession, context.getAudioManager());
        result.cameraController = new CameraController(result.worldCamera, dimensions);
        result.endingController = new EndingController(dimensions);

        result.stageController.onEnter();
        result.deathController.onEnter();
        result.cameraController.onEnter();
        result.endingController.onEnter(stagePlan.isFinalStage(stageConfig.getSceneId()), gameSession.getGameState());

        GameConfig config = GameConfig.defaultConfig()
                .withPlayerSpeedMultiplier(stageConfig.getPlayerSpeedMultiplier());

        result.gameplayAudioController = new GameplayAudioController(context.getEventBus(), context.getAudioManager());
        result.hudRenderer = new GameplayHudRenderer(gameSession.getScoreManager(),
                gameSession.getEnvironmentProgress(),
                stagePlan.getCheckpointTargets(),
                stageConfig.getTitle());
        result.smokeEffect = new SmokeEffect(context.getAssetManager(), stageConfig.getSmokeOverlayAlpha());

        result.heartFullTexture = context.getAssetManager().get("textures/ui/hearts/heart_full.png", Texture.class);
        result.heartEmptyTexture = context.getAssetManager().get("textures/ui/hearts/heart_empty.png", Texture.class);

        result.pauseOverlayController = new PauseOverlayController(
                320f,
                90f,
                context.getAssetManager().get("textures/backgrounds/pause.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/resume_1.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/resume_2.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/help_1.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/help_2.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/restart_1.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/restart_2.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/quit_1.png", Texture.class),
                context.getAssetManager().get("textures/ui/buttons/quit_2.png", Texture.class)
        );
        result.helpOverlayController = new HelpOverlayController(
                context.getAssetManager().get(AssetKeys.INSTRUCTIONS_BG, Texture.class)
        );

        result.entityFactory = new EntityFactory(context.getRandomManager());
        result.player = new Player(80f, dimensions.getWorldHeight() / 2f,
                config.playerSpeed, config.gravity, config.jumpImpulse);
        result.runtimeContext = new GameplayRuntimeContext(entityStore, gameSession, dimensions);
        result.runtimeContext.setPlayer(result.player);
        entityStore.queueAdd(result.player);

        result.gameplayLoop = new GameplayLoop();
        result.gameplayLoop.addStep(new InputStep(context.getInputManager(), context.getEventBus(), result.runtimeContext.gameState()));
        result.gameplayLoop.addStep(new SpawnStep(result.runtimeContext,
                result.entityFactory.getObstacleFactory(), result.entityFactory.getCollectibleFactory(),
                context.getRandomManager(), config,
                result.endingController.isActive() ? 0f : 3f));
        result.gameplayLoop.addStep(new UpdateStep(entityStore, context.getTimeManager(), result.runtimeContext.gameState()));
        result.gameplayLoop.addStep(new MovementStep(result.runtimeContext, context.getMovementManager(), context.getTimeManager()));
        result.gameplayLoop.addStep(new CollisionStep(result.runtimeContext, context.getCollisionManager(), context.getEventBus()));
        result.gameplayLoop.addStep(new EffectStep(activeEffects, context.getTimeManager()));
        result.gameplayLoop.addStep(new DespawnStep(result.runtimeContext));

        entityStore.flush();
        return result;
    }
}
