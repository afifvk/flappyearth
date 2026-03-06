package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.RendererManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.factories.EntityFactory;
import inf1009.p63.flappyearth.game.loop.CleanupStep;
import inf1009.p63.flappyearth.game.loop.CollisionStep;
import inf1009.p63.flappyearth.game.loop.EffectStep;
import inf1009.p63.flappyearth.game.loop.GameLoop;
import inf1009.p63.flappyearth.game.loop.GameOverStep;
import inf1009.p63.flappyearth.game.loop.InputStep;
import inf1009.p63.flappyearth.game.loop.MovementStep;
import inf1009.p63.flappyearth.game.loop.ScoreStep;
import inf1009.p63.flappyearth.game.loop.SpawnStep;
import inf1009.p63.flappyearth.game.loop.UpdateStep;
import inf1009.p63.flappyearth.game.managers.EcoFactPopupManager;
import inf1009.p63.flappyearth.game.managers.HudManager;
import inf1009.p63.flappyearth.game.managers.PlayerManager;
import inf1009.p63.flappyearth.game.managers.ScoreManager;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameState;

public class GameScene extends Scene {

    private final SceneManager      sceneManager;
    private final GameContextManager context;

    // GameScene-specific managers (not in base Scene)
    private EntityManager      entityManager;
    private RendererManager    rendererManager;

    private GameState  gameState;
    private GameConfig         config;
    private ActiveEffects      activeEffects;

    private PlayerManager      playerManager;
    private ScoreManager       scoreManager;
    private HudManager         hudManager;
    private EcoFactPopupManager   factPopupManager;

    private EntityFactory entityFactory;

    private GameLoop gameLoopManager;

    private OrthographicCamera worldCamera;

    public GameScene(SceneManager sceneManager, GameContextManager context) {
        this.sceneManager = sceneManager;
        this.context      = context;

        this.entityManager   = new EntityManager();
        this.rendererManager = new RendererManager();
        this.rendererManager.setAssetManager(context.getAssetManager());
    }

    @Override
    public void onEnter() {
        // Clean up from previous game session (scene is reused)
        if (hudManager != null) {
            hudManager.dispose();
        }
        if (factPopupManager != null) {
            factPopupManager.dispose();
        }
        if (entityManager != null) {
            entityManager.clear();
        }
        
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        worldCamera = new OrthographicCamera(screenW, screenH);
        worldCamera.position.set(screenW / 2f, screenH / 2f, 0);
        worldCamera.update();

        gameState     = new GameState();
        config        = GameConfig.defaultConfig();
        scoreManager  = new ScoreManager();
        activeEffects = new ActiveEffects();

        playerManager      = new PlayerManager(entityManager,
                                               context.getEventManager(), config);
        hudManager         = new HudManager(scoreManager, activeEffects);
        factPopupManager   = new EcoFactPopupManager(context.getEventManager());

        entityFactory = new EntityFactory(context.getRandomManager());

        playerManager.spawnPlayer(80f, screenH / 2f, config);

        gameLoopManager = new GameLoop();
        gameLoopManager.addStep(new InputStep(
                context.getInputOutputManager(), context.getEventManager(), gameState));
        gameLoopManager.addStep(new SpawnStep(
                entityManager,
                entityFactory.getObstacleFactory(),
                entityFactory.getCollectibleFactory(),
                context.getRandomManager(), gameState, config));
        gameLoopManager.addStep(new UpdateStep(
                entityManager, context.getTimeManager(), gameState));
        gameLoopManager.addStep(new MovementStep(
                entityManager, context.getMovementManager(),
            context.getTimeManager(), gameState));
        gameLoopManager.addStep(new CollisionStep(
                entityManager, context.getCollisionManager(),
                context.getEventManager(), gameState, activeEffects));
        gameLoopManager.addStep(new ScoreStep(
                entityManager, gameState, scoreManager));
        gameLoopManager.addStep(new EffectStep(
            activeEffects,
            context.getTimeManager()));
        gameLoopManager.addStep(new CleanupStep(entityManager));
        gameLoopManager.addStep(new GameOverStep(
                entityManager, gameState, context.getEventManager()));

        // Flush initial queueAdd (player)
        entityManager.flush();
    }

    @Override
    public void update(float delta) {
        gameLoopManager.update(delta);
        factPopupManager.update(delta);

        GameState.SceneRequest req = gameState.getRequestedScene();
        if (req == GameState.SceneRequest.GAME_OVER) {
            // Update score in pre-created GameOverScene, then transition
            GameOverScene gameOverScene = (GameOverScene) sceneManager.getScene(GameSceneId.GAME_OVER.id());
            gameOverScene.setScore(scoreManager.getCurrentScore());
            sceneManager.switchTo(GameSceneId.GAME_OVER.id());
        }
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // Update camera dimensions if window was resized
        if (Math.abs(worldCamera.viewportWidth - screenW) > 0.1f ||
            Math.abs(worldCamera.viewportHeight - screenH) > 0.1f) {
            worldCamera.setToOrtho(false, screenW, screenH);
        }

        if (playerManager.getPlayer() != null) {
            float playerCx = playerManager.getPlayer().getBounds().x + screenW / 2f;
            worldCamera.position.set(playerCx, screenH / 2f, 0);
            worldCamera.update();
        }

        rendererManager.getShapeRenderer().setProjectionMatrix(worldCamera.combined);
        rendererManager.getBatch().setProjectionMatrix(worldCamera.combined);
        rendererManager.render(entityManager.getRenderables());

        SpriteBatch hudBatch = rendererManager.getBatch();
        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        hudBatch.begin();
        hudManager.render(hudBatch, screenH);
        factPopupManager.render(hudBatch, screenW, screenH);
        hudBatch.end();
    }

    @Override
    public void onExit() {
        context.getEventManager().clearAll();
    }

    @Override
    public void disposeResources() {
        super.disposeResources();
        if (rendererManager != null) rendererManager.dispose();
        if (entityManager   != null) entityManager.dispose();
        if (hudManager      != null) hudManager.dispose();
        if (factPopupManager != null) factPopupManager.dispose();
    }
}
