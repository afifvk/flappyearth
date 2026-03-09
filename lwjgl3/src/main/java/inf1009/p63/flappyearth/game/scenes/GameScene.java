package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.engine.managers.RendererManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.controllers.DeathController;
import inf1009.p63.flappyearth.game.controllers.EndingSceneController;
import inf1009.p63.flappyearth.game.controllers.GameCameraController;
import inf1009.p63.flappyearth.game.controllers.StageController;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.BadHitEvent;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.factories.EntityFactory;
import inf1009.p63.flappyearth.game.loop.CollisionSystem;
import inf1009.p63.flappyearth.game.loop.DespawnSystem;
import inf1009.p63.flappyearth.game.loop.DistanceScoreSystem;
import inf1009.p63.flappyearth.game.loop.EffectStep;
import inf1009.p63.flappyearth.game.loop.GameLoop;
import inf1009.p63.flappyearth.game.loop.InputStep;
import inf1009.p63.flappyearth.game.loop.MovementStep;
import inf1009.p63.flappyearth.game.loop.SpawnSystem;
import inf1009.p63.flappyearth.game.loop.UpdateStep;
import inf1009.p63.flappyearth.game.managers.EcoFactPopupRenderer;
import inf1009.p63.flappyearth.game.managers.HudRenderer;
import inf1009.p63.flappyearth.game.managers.PlayerManager;
import inf1009.p63.flappyearth.game.managers.SmokeEffect;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.state.GameState;

public class GameScene extends Scene {

    private final SceneManager sceneManager;
    private final GameContextManager context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;
    private final StageConfig stageConfig;

    private EntityManager entityManager;
    private RendererManager rendererManager;

    private PlayerManager playerManager;
    private HudRenderer hudRenderer;
    private EcoFactPopupRenderer factPopupRenderer;
    private SmokeEffect smokeEffect;

    private EntityFactory entityFactory;
    private GameLoop gameLoopManager;
    private OrthographicCamera worldCamera;
    private StageController stageController;
    private DeathController deathController;
    private GameCameraController cameraController;
    private EndingSceneController endingSceneController;

    private EventManager.EventListener goodCollectedProgressListener;
    private EventManager.EventListener badHitListener;
    private EventManager.EventListener obstaclePassedListener;
    private ActiveEffects activeEffects;

    private final BitmapFont introFont;
    private final GlyphLayout introLayout;
    private static final float INTRO_DURATION_SECONDS = 3.0f;
    private static final float STAGE_OVERLAY_DURATION_SECONDS = 1.5f;
    private float introTimer;
    private boolean showIntroText;
    private float stageOverlayTimer;
    private boolean showStageOverlay;

    public GameScene(SceneManager sceneManager,
                     GameContextManager context,
                     GameSession gameSession,
                     StagePlan stagePlan,
                     StageConfig stageConfig) {
        this.sceneManager = sceneManager;
        this.context = context;
        this.gameSession = gameSession;
        this.stagePlan = stagePlan;
        this.stageConfig = stageConfig;

        this.entityManager = new EntityManager();
        this.rendererManager = new RendererManager();
        this.rendererManager.setAssetManager(context.getAssetManager());

        this.introFont = new BitmapFont();
        this.introFont.getData().setScale(1.7f);
        this.introLayout = new GlyphLayout();
    }

    @Override
    public void onEnter() {
        gameSession.prepareForStageEntry();

        if (hudRenderer != null) {
            hudRenderer.dispose();
        }
        if (factPopupRenderer != null) {
            factPopupRenderer.dispose();
        }
        if (entityManager != null) {
            entityManager.clear();
        }
        goodCollectedProgressListener = data -> gameSession.getEnvironmentProgress().addGoodCollectible();

        context.getEventManager().subscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);
        badHitListener = data -> {
            context.getSoundManager().playHitBad();

            // Bring the player forward and flicker
            Player p = playerManager != null ? playerManager.getPlayer() : null;
            if (p != null) {
                entityManager.bringToFront(p);
                p.flicker(0.6f);
            }

            // Parse event and start pipe crash + spawn debris
            if (data instanceof BadHitEvent) {
                BadHitEvent ev = (BadHitEvent) data;
                int hitId = ev.entityId;
                Obstacle hitObs = null;
                for (Obstacle o : entityManager.getByType(Obstacle.class)) {
                    if (o.getId() == hitId) { hitObs = o; break; }
                }
                if (hitObs != null) {
                    // start obstacle crash with some random velocity
                    float vX = context.getRandomManager().range(-120f, 120f);
                    float vY = context.getRandomManager().range(150f, 300f);
                    hitObs.startCrash(vX, vY, 1.2f);
                    // spawn debris pieces via factory
                    if (entityFactory != null) {
                        entityFactory.spawnDebrisForObstacle(entityManager, hitObs);
                    }
                }
            }

            activeEffects.activateScreenShake(0.35f, 12f);
            if (gameSession.getGameState().isDead()) {
                context.getSoundManager().playGameOver();
            }
        };
        context.getEventManager().subscribe(GameEvents.BAD_HIT, badHitListener);
        // Play point sound when obstacle is passed
        obstaclePassedListener = data -> context.getSoundManager().playPoint();
        context.getEventManager().subscribe(GameEvents.OBSTACLE_PASSED, obstaclePassedListener);

        showIntroText = stageConfig.getSceneId().equals(stagePlan.getInitialStageId());
        introTimer = showIntroText ? INTRO_DURATION_SECONDS : 0f;

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        worldCamera = new OrthographicCamera(screenW, screenH);
        worldCamera.position.set(screenW / 2f, screenH / 2f, 0);
        worldCamera.update();

        stageController = new StageController(
            sceneManager,
            stagePlan,
            stageConfig,
            gameSession.getEnvironmentProgress());
        deathController = new DeathController(sceneManager, gameSession);
        cameraController = new GameCameraController(worldCamera);
        endingSceneController = new EndingSceneController();

        stageController.onEnter();
        deathController.onEnter();
        cameraController.onEnter();
        endingSceneController.onEnter(stagePlan.isFinalStage(stageConfig.getSceneId()), gameSession.getGameState());

        showStageOverlay = !stageConfig.getSceneId().equals(stagePlan.getInitialStageId())
            && !endingSceneController.isActive();
        stageOverlayTimer = showStageOverlay ? STAGE_OVERLAY_DURATION_SECONDS : 0f;

        GameState gameState = gameSession.getGameState();
        this.activeEffects = gameSession.getActiveEffects();

        GameConfig config = GameConfig.defaultConfig();

        playerManager = new PlayerManager(entityManager, context.getEventManager(), config, context.getSoundManager());
        hudRenderer = new HudRenderer(
                activeEffects,
                gameState,
                gameSession.getScoreManager(),
                gameSession.getEnvironmentProgress(),
            stagePlan.getCheckpointTargets(),
                stageConfig.getTitle());
        factPopupRenderer = new EcoFactPopupRenderer(context.getEventManager());
        smokeEffect = new SmokeEffect(
            context.getAssetManager(),
            stageConfig.getSmokeOverlayAlpha());

        entityFactory = new EntityFactory(context.getRandomManager());
        playerManager.spawnPlayer(80f, screenH / 2f, config);

        gameLoopManager = new GameLoop();
        gameLoopManager.addStep(new InputStep(
                context.getInputOutputManager(), context.getEventManager(), gameState));
        gameLoopManager.addStep(new SpawnSystem(
                entityManager,
                entityFactory.getObstacleFactory(),
                entityFactory.getCollectibleFactory(),
            context.getRandomManager(), gameState, config,
            endingSceneController.isActive() ? 0f : 3f));
        gameLoopManager.addStep(new UpdateStep(
                entityManager, context.getTimeManager(), gameState));
        gameLoopManager.addStep(new MovementStep(
                entityManager, context.getMovementManager(),
                context.getTimeManager(), gameState));
        gameLoopManager.addStep(new CollisionSystem(
                entityManager, context.getCollisionManager(),
                context.getEventManager(), gameState, activeEffects));
        gameLoopManager.addStep(new DistanceScoreSystem(
            gameState, gameSession.getScoreManager()));
        gameLoopManager.addStep(new EffectStep(
                activeEffects,
                context.getTimeManager()));
        gameLoopManager.addStep(new DespawnSystem(entityManager));
        entityManager.flush();
    }

    @Override
    public void update(float delta) {
        if (introTimer > 0f) {
            introTimer = Math.max(0f, introTimer - delta);
        }
        if (stageOverlayTimer > 0f) {
            stageOverlayTimer = Math.max(0f, stageOverlayTimer - delta);
        }

        GameState gameState = gameSession.getGameState();
        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);

        if (endingSceneController.update(
                delta,
                player,
                context.getInputOutputManager(),
                sceneManager,
                gameState)) {
            return;
        }

        if (!gameState.isDeathSequenceActive()) {
            gameLoopManager.update(delta);
            factPopupRenderer.update(delta);
        }

        player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        deathController.update(
                delta,
                player,
                cameraController,
                endingSceneController.isSafeEndingWindow());

        if (deathController.routeToGameOverIfRequested()) {
            return;
        }

        if (stageController.isTransitioning()) {
            return;
        }

        stageController.update();
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        Gdx.gl.glClearColor(stageConfig.getClearR(), stageConfig.getClearG(), stageConfig.getClearB(), 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Player player = playerManager != null ? playerManager.getPlayer() : null;
        cameraController.apply(screenW, screenH, player);

        rendererManager.getBatch().setProjectionMatrix(cameraController.getCamera().combined);
        rendererManager.getShapeRenderer().setProjectionMatrix(cameraController.getCamera().combined);

        // Apply screen shake if active
        if (activeEffects != null && activeEffects.isScreenShaking()) {
            float mag = activeEffects.getScreenShakeMagnitude();
            float ox = context.getRandomManager().range(-mag, mag);
            float oy = context.getRandomManager().range(-mag, mag);
            cameraController.getCamera().position.add(ox, oy, 0f);
            cameraController.getCamera().update();
            rendererManager.getBatch().setProjectionMatrix(cameraController.getCamera().combined);
            rendererManager.getShapeRenderer().setProjectionMatrix(cameraController.getCamera().combined);
        }

        rendererManager.render(entityManager.getRenderables());

        SpriteBatch hudBatch = rendererManager.getBatch();
        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        rendererManager.getShapeRenderer().setProjectionMatrix(hudBatch.getProjectionMatrix());
        hudRenderer.render(rendererManager.getShapeRenderer(), hudBatch, screenW, screenH);

        hudBatch.begin();
        if (smokeEffect != null) {
            smokeEffect.render(hudBatch, screenW, screenH);
        }
        factPopupRenderer.render(hudBatch, screenW, screenH);

        if (showIntroText && introTimer > 0f) {
            String lineOne = "Restore the world.";
            String lineTwo = "Collect clean pickups.";
            String lineThree = "Avoid hazards.";

            introLayout.setText(introFont, lineOne);
            float lineOneX = (screenW - introLayout.width) / 2f;
            float lineOneY = screenH * 0.62f;
            introFont.draw(hudBatch, introLayout, lineOneX, lineOneY);

            introLayout.setText(introFont, lineTwo);
            float lineTwoX = (screenW - introLayout.width) / 2f;
            float lineTwoY = screenH * 0.54f;
            introFont.draw(hudBatch, introLayout, lineTwoX, lineTwoY);

            introLayout.setText(introFont, lineThree);
            float lineThreeX = (screenW - introLayout.width) / 2f;
            float lineThreeY = screenH * 0.46f;
            introFont.draw(hudBatch, introLayout, lineThreeX, lineThreeY);
        }

        if (showStageOverlay && stageOverlayTimer > 0f) {
            String header = stagePlan.isFinalStage(stageConfig.getSceneId())
                    ? "Cleanup Complete"
                    : "Checkpoint Reached";
            String title = stageConfig.getTitle();
            String subtitle = stageConfig.getSubtitle();

            introLayout.setText(introFont, header);
            float headerX = (screenW - introLayout.width) / 2f;
            float headerY = screenH * 0.76f;
            introFont.draw(hudBatch, introLayout, headerX, headerY);

            introLayout.setText(introFont, title);
            float titleX = (screenW - introLayout.width) / 2f;
            float titleY = screenH * 0.68f;
            introFont.draw(hudBatch, introLayout, titleX, titleY);

            introLayout.setText(introFont, subtitle);
            float subtitleX = (screenW - introLayout.width) / 2f;
            float subtitleY = screenH * 0.61f;
            introFont.draw(hudBatch, introLayout, subtitleX, subtitleY);
        }

        if (endingSceneController.isActive() && endingSceneController.isAwaitingContinue()) {
            String lineOne = "You have saved the world.";
            String lineTwo = "Let us all do our part to keep Earth clean.";
            String lineThree = "Press SPACE to continue playing";
            String lineFour = "or ESC to quit to main menu";

            introLayout.setText(introFont, lineOne);
            float y1 = screenH * 0.62f;
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, y1);

            introLayout.setText(introFont, lineTwo);
            float y2 = screenH * 0.54f;
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, y2);

            introLayout.setText(introFont, lineThree);
            float y3 = screenH * 0.44f;
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, y3);

            introLayout.setText(introFont, lineFour);
            float y4 = screenH * 0.38f;
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, y4);
        }

        if (endingSceneController.isActive() && endingSceneController.isSpawnWarmup()) {
            String warmupText = "Take control in "
                + (int) Math.ceil(endingSceneController.getSpawnWarmupTimer()) + "...";
            introLayout.setText(introFont, warmupText);
            introFont.draw(hudBatch, introLayout,
                    (screenW - introLayout.width) / 2f,
                    screenH * 0.44f);
        }

        hudBatch.end();
    }

    @Override
    public void onExit() {
        if (goodCollectedProgressListener != null) {
            context.getEventManager().unsubscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);
        }
        if (badHitListener != null) {
            context.getEventManager().unsubscribe(GameEvents.BAD_HIT, badHitListener);
        }
        if (obstaclePassedListener != null) {
            context.getEventManager().unsubscribe(GameEvents.OBSTACLE_PASSED, obstaclePassedListener);
        }
    }

    @Override
    public void disposeResources() {
        super.disposeResources();
        if (rendererManager != null) {
            rendererManager.dispose();
        }
        if (entityManager != null) {
            entityManager.dispose();
        }
        if (hudRenderer != null) {
            hudRenderer.dispose();
        }
        if (factPopupRenderer != null) {
            factPopupRenderer.dispose();
        }
        if (introFont != null) {
            introFont.dispose();
        }
    }
}
