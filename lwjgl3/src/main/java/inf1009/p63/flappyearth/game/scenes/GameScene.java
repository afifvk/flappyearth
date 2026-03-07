package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.factories.EntityFactory;
import inf1009.p63.flappyearth.game.loop.CleanupStep;
import inf1009.p63.flappyearth.game.loop.CollisionStep;
import inf1009.p63.flappyearth.game.loop.EffectStep;
import inf1009.p63.flappyearth.game.loop.GameLoop;
import inf1009.p63.flappyearth.game.loop.InputStep;
import inf1009.p63.flappyearth.game.loop.MovementStep;
import inf1009.p63.flappyearth.game.loop.ScoreStep;
import inf1009.p63.flappyearth.game.loop.SpawnStep;
import inf1009.p63.flappyearth.game.loop.UpdateStep;
import inf1009.p63.flappyearth.game.input.GameInputAction;
import inf1009.p63.flappyearth.game.managers.EcoFactPopupManager;
import inf1009.p63.flappyearth.game.managers.HudManager;
import inf1009.p63.flappyearth.game.managers.PlayerManager;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.EnvironmentProgress;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.state.GameState;

public class GameScene extends Scene {

    private final SceneManager sceneManager;
    private final GameContextManager context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;
    private final StageTemplate stageTemplate;

    private EntityManager entityManager;
    private RendererManager rendererManager;

    private PlayerManager playerManager;
    private HudManager hudManager;
    private EcoFactPopupManager factPopupManager;

    private EntityFactory entityFactory;
    private GameLoop gameLoopManager;
    private OrthographicCamera worldCamera;

    private EventManager.EventListener goodCollectedProgressListener;

    private final BitmapFont introFont;
    private final GlyphLayout introLayout;
    private static final float INTRO_DURATION_SECONDS = 3.0f;
    private static final float GAME_OVER_DELAY_SECONDS = 1.0f;
    private static final float STAGE_OVERLAY_DURATION_SECONDS = 1.5f;
    private static final float ENDING_SPAWN_DELAY_SECONDS = 3.0f;
    private static final float ENDING_TARGET_HEIGHT_RATIO = 0.55f;
    private static final float ENDING_FLAP_WINDOW = 24f;
    private float introTimer;
    private boolean showIntroText;
    private float stageOverlayTimer;
    private boolean showStageOverlay;

    private boolean transitioningToNextStage;
    private boolean deathCameraLocked;
    private float deathCameraX;
    private boolean gameOverDelayActive;
    private float gameOverDelayTimer;
    private boolean endingSceneActive;
    private boolean endingAwaitContinue;
    private boolean endingSpawnWarmup;
    private float endingSpawnWarmupTimer;

    public GameScene(SceneManager sceneManager,
                     GameContextManager context,
                     GameSession gameSession,
                     StagePlan stagePlan,
                     StageTemplate stageTemplate) {
        this.sceneManager = sceneManager;
        this.context = context;
        this.gameSession = gameSession;
        this.stagePlan = stagePlan;
        this.stageTemplate = stageTemplate;

        this.entityManager = new EntityManager();
        this.rendererManager = new RendererManager();
        this.rendererManager.setAssetManager(context.getAssetManager());

        this.introFont = new BitmapFont();
        this.introFont.getData().setScale(1.7f);
        this.introLayout = new GlyphLayout();
    }

    @Override
    public void onEnter() {
        transitioningToNextStage = false;
        deathCameraLocked = false;
        deathCameraX = 0f;
        gameOverDelayActive = false;
        gameOverDelayTimer = 0f;
        endingSceneActive = stagePlan.isFinalStage(stageTemplate.getSceneId());
        endingAwaitContinue = endingSceneActive;
        endingSpawnWarmup = false;
        endingSpawnWarmupTimer = 0f;
        showStageOverlay = !stageTemplate.getSceneId().equals(stagePlan.getInitialStageId()) && !endingSceneActive;
        stageOverlayTimer = showStageOverlay ? STAGE_OVERLAY_DURATION_SECONDS : 0f;
        gameSession.prepareForStageEntry();

        if (hudManager != null) {
            hudManager.dispose();
        }
        if (factPopupManager != null) {
            factPopupManager.dispose();
        }
        if (entityManager != null) {
            entityManager.clear();
        }
        goodCollectedProgressListener = data -> gameSession.getEnvironmentProgress().addGoodCollectible();

        context.getEventManager().subscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);

        showIntroText = stageTemplate.getSceneId().equals(stagePlan.getInitialStageId());
        introTimer = showIntroText ? INTRO_DURATION_SECONDS : 0f;

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        worldCamera = new OrthographicCamera(screenW, screenH);
        worldCamera.position.set(screenW / 2f, screenH / 2f, 0);
        worldCamera.update();

        GameState gameState = gameSession.getGameState();
        ActiveEffects activeEffects = gameSession.getActiveEffects();
        gameState.setControlsEnabled(!endingSceneActive);
        gameState.setSpawningEnabled(!endingSceneActive);

        GameConfig config = GameConfig.defaultConfig();

        playerManager = new PlayerManager(entityManager, context.getEventManager(), config);
        hudManager = new HudManager(
                activeEffects,
                gameState,
                gameSession.getScoreManager(),
                gameSession.getEnvironmentProgress(),
            stagePlan.getCheckpointTargets(),
                stageTemplate.getTitle());
        factPopupManager = new EcoFactPopupManager(context.getEventManager());

        entityFactory = new EntityFactory(context.getRandomManager());
        playerManager.spawnPlayer(80f, screenH / 2f, config);

        gameLoopManager = new GameLoop();
        gameLoopManager.addStep(new InputStep(
                context.getInputOutputManager(), context.getEventManager(), gameState));
        gameLoopManager.addStep(new SpawnStep(
                entityManager,
                entityFactory.getObstacleFactory(),
                entityFactory.getCollectibleFactory(),
            context.getRandomManager(), gameState, config,
            endingSceneActive ? 0f : 3f));
        gameLoopManager.addStep(new UpdateStep(
                entityManager, context.getTimeManager(), gameState));
        gameLoopManager.addStep(new MovementStep(
                entityManager, context.getMovementManager(),
                context.getTimeManager(), gameState));
        gameLoopManager.addStep(new CollisionStep(
                entityManager, context.getCollisionManager(),
                context.getEventManager(), gameState, activeEffects));
        gameLoopManager.addStep(new ScoreStep(
            gameState, gameSession.getScoreManager()));
        gameLoopManager.addStep(new EffectStep(
                activeEffects,
                context.getTimeManager()));
        gameLoopManager.addStep(new CleanupStep(entityManager));
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

        if (endingSceneActive) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                sceneManager.switchTo(GameSceneId.MENU.id());
                return;
            }

            if ((endingAwaitContinue || endingSpawnWarmup) && player != null) {
                updateEndingAutoFlight(player);
            }

            if (endingAwaitContinue && context.getInputOutputManager().isActionJustPressed(GameInputAction.FLAP.id())) {
                endingAwaitContinue = false;
                endingSpawnWarmup = true;
                endingSpawnWarmupTimer = ENDING_SPAWN_DELAY_SECONDS;
                gameState.setControlsEnabled(false);
            }

            if (endingSpawnWarmup) {
                endingSpawnWarmupTimer = Math.max(0f, endingSpawnWarmupTimer - delta);
                if (endingSpawnWarmupTimer <= 0f) {
                    endingSpawnWarmup = false;
                    gameState.setControlsEnabled(true);
                    gameState.setSpawningEnabled(true);
                }
            }
        }

        if (gameState.isDeathSequenceActive()) {
            if (player != null) {
                if (!player.isDeathFallActive()) {
                    startDeathSequence(player);
                }
                player.update(delta);
                player.movement(delta);
                if (!gameOverDelayActive && player.getBounds().y + player.getBounds().height < 0f) {
                    gameOverDelayActive = true;
                    gameOverDelayTimer = GAME_OVER_DELAY_SECONDS;
                }
                if (gameOverDelayActive) {
                    gameOverDelayTimer = Math.max(0f, gameOverDelayTimer - delta);
                }
                if (gameOverDelayActive && gameOverDelayTimer <= 0f) {
                    gameState.finishDeathSequence();
                }
            }
        } else {
            gameLoopManager.update(delta);
            factPopupManager.update(delta);

            player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
            boolean isSafeEndingWindow = endingSceneActive && (endingAwaitContinue || endingSpawnWarmup);
            if (!isSafeEndingWindow && player != null && player.getBounds().y + player.getBounds().height < 0f) {
                startDeathSequence(player, 1f);
            }
        }

        if (gameState.getRequestedScene() == GameState.SceneRequest.GAME_OVER) {
            GameOverScene gameOverScene = (GameOverScene) sceneManager.getScene(GameSceneId.GAME_OVER.id());
            gameOverScene.setScore(gameSession.getScoreManager().getCurrentScore());
            sceneManager.switchTo(GameSceneId.GAME_OVER.id());
            return;
        }

        if (transitioningToNextStage) {
            return;
        }

        EnvironmentProgress progress = gameSession.getEnvironmentProgress();
        int stageTarget = stagePlan.getTargetForStage(stageTemplate.getSceneId());
        if (progress.getGoodCollectiblesCollected() >= stageTarget) {
            String nextSceneId = stagePlan.getNextStageId(stageTemplate.getSceneId());
            if (nextSceneId != null) {
                transitioningToNextStage = true;
                sceneManager.switchTo(nextSceneId);
            }
        }
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        Gdx.gl.glClearColor(stageTemplate.getClearR(), stageTemplate.getClearG(), stageTemplate.getClearB(), 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (deathCameraLocked) {
            worldCamera.position.set(deathCameraX, screenH / 2f, 0f);
            worldCamera.update();
        } else if (playerManager.getPlayer() != null) {
            float playerX = playerManager.getPlayer().getBounds().x;
            worldCamera.position.set(playerX + screenW / 4f, screenH / 2f, 0);
            worldCamera.update();
        }

        rendererManager.getBatch().setProjectionMatrix(worldCamera.combined);
        rendererManager.getShapeRenderer().setProjectionMatrix(worldCamera.combined);

        rendererManager.render(entityManager.getRenderables());

        SpriteBatch hudBatch = rendererManager.getBatch();
        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        rendererManager.getShapeRenderer().setProjectionMatrix(hudBatch.getProjectionMatrix());
        hudManager.render(rendererManager.getShapeRenderer(), hudBatch, screenW, screenH);

        hudBatch.begin();
        factPopupManager.render(hudBatch, screenW, screenH);

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
            String header = stagePlan.isFinalStage(stageTemplate.getSceneId())
                    ? "Cleanup Complete"
                    : "Checkpoint Reached";
            String title = stageTemplate.getTitle();
            String subtitle = stageTemplate.getSubtitle();

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

        if (endingSceneActive && endingAwaitContinue) {
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

        if (endingSceneActive && endingSpawnWarmup) {
            String warmupText = "Take control in " + (int) Math.ceil(endingSpawnWarmupTimer) + "...";
            introLayout.setText(introFont, warmupText);
            introFont.draw(hudBatch, introLayout,
                    (screenW - introLayout.width) / 2f,
                    screenH * 0.44f);
        }

        hudBatch.end();
    }

    private void updateEndingAutoFlight(Player player) {
        float screenH = Gdx.graphics.getHeight();
        float targetY = screenH * ENDING_TARGET_HEIGHT_RATIO;
        float playerCenterY = player.getBounds().y + (player.getBounds().height / 2f);
        if (playerCenterY < targetY - ENDING_FLAP_WINDOW) {
            player.flap();
        }
    }

    @Override
    public void onExit() {
        if (goodCollectedProgressListener != null) {
            context.getEventManager().unsubscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);
        }
    }

    private void startDeathSequence(Player player) {
        startDeathSequence(player, gameSession.getGameState().getDeathFallSpeedMultiplier());
    }

    private void startDeathSequence(Player player, float deathFallSpeedMultiplier) {
        if (player == null) return;

        GameState gameState = gameSession.getGameState();
        if (!gameState.isDeathSequenceActive()) {
            gameState.startDeathSequence(deathFallSpeedMultiplier);
        }
        player.startDeathFall(gameState.getDeathFallSpeedMultiplier());
        deathCameraLocked = true;
        deathCameraX = worldCamera.position.x;
        if (!gameOverDelayActive && player.getBounds().y + player.getBounds().height < 0f) {
            gameOverDelayActive = true;
            gameOverDelayTimer = GAME_OVER_DELAY_SECONDS;
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
        if (hudManager != null) {
            hudManager.dispose();
        }
        if (factPopupManager != null) {
            factPopupManager.dispose();
        }
        if (introFont != null) {
            introFont.dispose();
        }
    }
}
