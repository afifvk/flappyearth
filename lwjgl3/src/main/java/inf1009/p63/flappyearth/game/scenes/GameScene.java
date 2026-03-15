package inf1009.p63.flappyearth.game.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.engine.managers.RendererManager;
import inf1009.p63.flappyearth.game.config.AssetKeys;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.controllers.DeathController;
import inf1009.p63.flappyearth.game.controllers.EndingSceneController;
import inf1009.p63.flappyearth.game.controllers.GameCameraController;
import inf1009.p63.flappyearth.game.controllers.StageController;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.BadCollectedEvent;
import inf1009.p63.flappyearth.game.events.BadHitEvent;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.factories.EntityFactory;
import inf1009.p63.flappyearth.game.loop.CollisionSystem;
import inf1009.p63.flappyearth.game.loop.DespawnSystem;
import inf1009.p63.flappyearth.game.loop.EffectStep;
import inf1009.p63.flappyearth.game.loop.GameLoop;
import inf1009.p63.flappyearth.game.loop.InputStep;
import inf1009.p63.flappyearth.game.loop.MovementStep;
import inf1009.p63.flappyearth.game.loop.SpawnSystem;
import inf1009.p63.flappyearth.game.loop.UpdateStep;
import inf1009.p63.flappyearth.game.managers.BrightnessOverlayRenderer;
import inf1009.p63.flappyearth.game.managers.EcoFactPopupRenderer;
import inf1009.p63.flappyearth.game.managers.HudRenderer;
import inf1009.p63.flappyearth.game.managers.PlayerManager;
import inf1009.p63.flappyearth.game.managers.SmokeEffect;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.state.GameState;

public class GameScene extends Scene {

    private static final float BUTTON_BASE_WIDTH  = 320f;
    private static final float BUTTON_BASE_HEIGHT = 90f;
    private static final String STAGE_ONE_BACKGROUND_KEY   = "backgrounds/stage1_background.png";
    private static final String STAGE_TWO_BACKGROUND_KEY   = "backgrounds/stage2_background.png";
    private static final String STAGE_THREE_BACKGROUND_KEY = "backgrounds/stage3_background.png";
    private static final String STAGE_FOUR_BACKGROUND_KEY  = "backgrounds/stage4_background.png";

    private final SceneManager sceneManager;
    private final GameContextManager context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;
    private final StageConfig stageConfig;
    private final GameplayDimensions dimensions;

    private EntityManager entityManager;
    private RendererManager rendererManager;

    private PlayerManager playerManager;
    private HudRenderer hudRenderer;
    private EcoFactPopupRenderer factPopupRenderer;
    private SmokeEffect smokeEffect;

    private EntityFactory entityFactory;
    private GameLoop gameLoopManager;
    private OrthographicCamera worldCamera;
    private Viewport worldViewport;
    private StageController stageController;
    private DeathController deathController;
    private GameCameraController cameraController;
    private EndingSceneController endingSceneController;

    private EventManager.EventListener goodCollectedProgressListener;
    private EventManager.EventListener badCollectedListener;
    private EventManager.EventListener badHitListener;
    private EventManager.EventListener obstaclePassedListener;
    private ActiveEffects activeEffects;
    private Texture heartFullTexture;
    private Texture heartEmptyTexture;

    // ── background ───────────────────────────────────────────────────────────
    private Texture stageBackgroundTexture;
    // true when WE loaded it directly (not via AssetManager) — must dispose manually
    private boolean stageBackgroundOwned = false;

    // ── pause overlay ────────────────────────────────────────────────────────
    private boolean           paused         = false;
    private SpriteBatch       pauseBatch;
    private OrthographicCamera pauseCamera;
    private Texture           pauseBgTex;
    private Texture           pauseResume1,  pauseResume2;
    private Texture           pauseHelp1,    pauseHelp2;
    private Texture           pauseRestart1, pauseRestart2;
    private Texture           pauseQuit1,    pauseQuit2;
    private Texture           instructionsTexture;
    private boolean           showingInstructionsOverlay = false;

    private String debuffMessage = "";
    private float  debuffMessageTimer = 0f;
    private static final float DEBUFF_MESSAGE_DURATION       = 2.5f;
    private static final float PLASTIC_BOTTLE_DURATION       = 5f;
    private static final float OIL_BLOT_DURATION             = 5f;
    private static final float FACTORY_SMOKE_DURATION        = 5f;
    private static final float FACTORY_SMOKE_ALPHA           = 0.9f;
    private static final float FACTORY_JUMP_INTERVAL_SECONDS = 0.5f;
    private static final float TRASH_PILE_SHAKE_DURATION     = 5f;
    private static final float TRASH_PILE_SHAKE_MAGNITUDE    = 50f;
    private static final float DEBUFF_POPUP_MAX_SECONDS      = 5f;
    private static final float HUD_STAGE_TITLE_OFFSET        = 24f;
    private static final float HUD_STAGE_TO_DEBUFF_GAP       = 52f;
    private static final int   OIL_SPLOTCH_COUNT             = 10;

    private final float[] oilSplotchXNorm    = new float[OIL_SPLOTCH_COUNT];
    private final float[] oilSplotchYNorm    = new float[OIL_SPLOTCH_COUNT];
    private final float[] oilSplotchSizeNorm = new float[OIL_SPLOTCH_COUNT];
    private final float[] oilSplotchRotation = new float[OIL_SPLOTCH_COUNT];
    private boolean oilSplotchPatternReady;
    private float   oilSplotchAnimTime;

    private final BitmapFont              introFont;
    private final GlyphLayout             introLayout;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;
    private static final float INTRO_DURATION_SECONDS        = 3.0f;
    private static final float STAGE_OVERLAY_DURATION_SECONDS = 1.5f;
    private float   introTimer;
    private boolean showIntroText;
    private float   stageOverlayTimer;
    private boolean showStageOverlay;

    public GameScene(SceneManager sceneManager,
                     GameContextManager context,
                     GameSession gameSession,
                     StagePlan stagePlan,
                     StageConfig stageConfig,
                     GameplayDimensions dimensions) {
        this.sceneManager = sceneManager;
        this.context      = context;
        this.gameSession  = gameSession;
        this.stagePlan    = stagePlan;
        this.stageConfig  = stageConfig;
        this.dimensions   = dimensions;

        this.entityManager   = new EntityManager();
        this.rendererManager = new RendererManager();
        this.rendererManager.setAssetManager(context.getAssetManager());

        this.introFont = new BitmapFont();
        this.introFont.getData().setScale(1.7f);
        this.introFont.setUseIntegerPositions(false);
        enableFontSmoothing(this.introFont);
        this.introLayout = new GlyphLayout();

        this.pauseBatch  = new SpriteBatch();
        this.pauseCamera = new OrthographicCamera();
        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();
    }

    // ── background loader ────────────────────────────────────────────────────
    // Priority 1 : AssetManager (already queued & loaded)
    // Priority 2 : Direct Gdx.files.internal() — works even if never queued
    // Priority 3 : Hard fallback to stage1
    private Texture loadStageBackground(String key) {
        // 1. Already managed by AssetManager
        if (context.getAssetManager().isLoaded(key)) {
            stageBackgroundOwned = false;
            Gdx.app.log("GameScene", "BG from AssetManager: " + key);
            return context.getAssetManager().get(key, Texture.class);
        }
        // 2. Load directly from file (fixes stage2/3/4 when never queued)
        if (Gdx.files.internal(key).exists()) {
            stageBackgroundOwned = true;
            Gdx.app.log("GameScene", "BG loaded directly from file: " + key);
            return new Texture(Gdx.files.internal(key));
        }
        // 3. Fallback
        Gdx.app.error("GameScene", "BG not found: " + key + " — falling back to stage1");
        if (context.getAssetManager().isLoaded(STAGE_ONE_BACKGROUND_KEY)) {
            stageBackgroundOwned = false;
            return context.getAssetManager().get(STAGE_ONE_BACKGROUND_KEY, Texture.class);
        }
        stageBackgroundOwned = true;
        return new Texture(Gdx.files.internal(STAGE_ONE_BACKGROUND_KEY));
    }

    private void disposeOwnedBackground() {
        if (stageBackgroundOwned && stageBackgroundTexture != null) {
            stageBackgroundTexture.dispose();
            stageBackgroundTexture = null;
            stageBackgroundOwned = false;
        }
    }

    @Override
    public void onEnter() {
        gameSession.prepareForStageEntry();

        if (hudRenderer != null)     hudRenderer.dispose();
        if (factPopupRenderer != null) factPopupRenderer.dispose();
        if (entityManager != null)   entityManager.clear();

        // Dispose previous background if we owned it
        disposeOwnedBackground();

        oilSplotchPatternReady = false;
        oilSplotchAnimTime     = 0f;

        goodCollectedProgressListener = data -> gameSession.getEnvironmentProgress().addGoodCollectible();

        badCollectedListener = data -> {
            if (data instanceof BadCollectedEvent) {
                BadCollectedEvent event  = (BadCollectedEvent) data;
                Player            player = playerManager != null ? playerManager.getPlayer() : null;
                switch (event.collectibleType) {
                    case "PLASTIC_BOTTLE":
                        if (player != null) player.applyReverseFlightDebuff(PLASTIC_BOTTLE_DURATION);
                        break;
                    case "OIL_SPILL":
                        if (activeEffects != null) activeEffects.activateOilBlot(OIL_BLOT_DURATION);
                        break;
                    case "FACTORY":
                        if (player != null) player.applyJumpIntervalDebuff(FACTORY_SMOKE_DURATION, FACTORY_JUMP_INTERVAL_SECONDS);
                        break;
                    case "TRASH_PILE":
                        if (activeEffects != null) activeEffects.activateTrashRattle(TRASH_PILE_SHAKE_DURATION, TRASH_PILE_SHAKE_MAGNITUDE);
                        break;
                    default:
                        break;
                }
            }
        };

        context.getEventManager().subscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);
        context.getEventManager().subscribe(GameEvents.BAD_COLLECTED,  badCollectedListener);

        badHitListener = data -> {
            context.getSoundManager().playHitBad();
            Player p = playerManager != null ? playerManager.getPlayer() : null;
            if (p != null) {
                entityManager.bringToFront(p);
                p.takeDamage(1);
            }
            if (data instanceof BadHitEvent) {
                BadHitEvent ev    = (BadHitEvent) data;
                int         hitId = ev.entityId;
                Obstacle    hitObs = null;
                for (Obstacle o : entityManager.getByType(Obstacle.class)) {
                    if (o.getId() == hitId) { hitObs = o; break; }
                }
                if (hitObs != null) {
                    float vX = context.getRandomManager().range(-120f, 120f);
                    float vY = context.getRandomManager().range(150f, 300f);
                    hitObs.startCrash(vX, vY, 1.2f);
                    if (entityFactory != null) entityFactory.spawnDebrisForObstacle(entityManager, hitObs);
                }
            }
            activeEffects.activateScreenShake(0.35f, 12f);
        };
        context.getEventManager().subscribe(GameEvents.BAD_HIT, badHitListener);

        obstaclePassedListener = data -> {
            gameSession.getScoreManager().addPoint();
            context.getSoundManager().playPoint();
        };
        context.getEventManager().subscribe(GameEvents.OBSTACLE_PASSED, obstaclePassedListener);

        showIntroText = stageConfig.getSceneId().equals(stagePlan.getInitialStageId());
        introTimer    = showIntroText ? INTRO_DURATION_SECONDS : 0f;

        worldCamera   = new OrthographicCamera();
        worldViewport = new StretchViewport(dimensions.getWorldWidth(), dimensions.getWorldHeight(), worldCamera);
        worldViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        worldCamera.update();

        stageController     = new StageController(sceneManager, stagePlan, stageConfig, gameSession.getEnvironmentProgress(), context.getSoundManager());
        deathController     = new DeathController(sceneManager, gameSession);
        cameraController    = new GameCameraController(worldCamera, dimensions);
        endingSceneController = new EndingSceneController(dimensions);

        stageController.onEnter();
        deathController.onEnter();
        cameraController.onEnter();
        endingSceneController.onEnter(stagePlan.isFinalStage(stageConfig.getSceneId()), gameSession.getGameState());

        showStageOverlay  = !stageConfig.getSceneId().equals(stagePlan.getInitialStageId())
                          && !endingSceneController.isActive();
        stageOverlayTimer = showStageOverlay ? STAGE_OVERLAY_DURATION_SECONDS : 0f;

        GameState  gameState = gameSession.getGameState();
        this.activeEffects   = gameSession.getActiveEffects();

        GameConfig config = GameConfig.defaultConfig()
                .withPlayerSpeedMultiplier(stageConfig.getPlayerSpeedMultiplier());

        playerManager    = new PlayerManager(entityManager, context.getEventManager(), config, context.getSoundManager());
        hudRenderer      = new HudRenderer(activeEffects, gameState,
                                           gameSession.getScoreManager(),
                                           gameSession.getEnvironmentProgress(),
                                           stagePlan.getCheckpointTargets(),
                                           stageConfig.getTitle());
        factPopupRenderer = new EcoFactPopupRenderer(context.getEventManager());
        smokeEffect       = new SmokeEffect(context.getAssetManager(), stageConfig.getSmokeOverlayAlpha());

        context.getAssetManager().load("backgrounds/heart_full.png",  Texture.class);
        context.getAssetManager().load("backgrounds/heart_empty.png", Texture.class);
        context.getAssetManager().load("sound/game_over.mp3", com.badlogic.gdx.audio.Sound.class);
        context.getAssetManager().finishLoading();

        context.getSoundManager().setGameOverSound(
                context.getAssetManager().get("sound/game_over.mp3", com.badlogic.gdx.audio.Sound.class));
        heartFullTexture  = context.getAssetManager().get("backgrounds/heart_full.png",  Texture.class);
        heartEmptyTexture = context.getAssetManager().get("backgrounds/heart_empty.png", Texture.class);

        // ── BACKGROUND FIX ───────────────────────────────────────────────────
        // Old code: set null when not loaded → nothing drawn for stage 2/3/4
        // New code: fall back to direct file load so every stage always renders
        String bgKey = resolveStageBackgroundKey(stageConfig.getSceneId());
        stageBackgroundTexture = loadStageBackground(bgKey);
        // ─────────────────────────────────────────────────────────────────────

        // Pause overlay textures
        pauseBgTex          = context.getAssetManager().get("ui/pause_background.png",  Texture.class);
        instructionsTexture = context.getAssetManager().get(AssetKeys.INSTRUCTIONS_BG,  Texture.class);
        pauseResume1        = context.getAssetManager().get("buttons/A_Resume1.png",     Texture.class);
        pauseResume2        = context.getAssetManager().get("buttons/A_Resume2.png",     Texture.class);
        pauseHelp1          = context.getAssetManager().get("buttons/A_Helps1.png",      Texture.class);
        pauseHelp2          = context.getAssetManager().get("buttons/A_Helps2.png",      Texture.class);
        pauseRestart1       = context.getAssetManager().get("buttons/A_Restart1.png",    Texture.class);
        pauseRestart2       = context.getAssetManager().get("buttons/A_Restart2.png",    Texture.class);
        pauseQuit1          = context.getAssetManager().get("buttons/A_Quit1.png",       Texture.class);
        pauseQuit2          = context.getAssetManager().get("buttons/A_Quit2.png",       Texture.class);
        paused                     = false;
        showingInstructionsOverlay = false;

        entityFactory = new EntityFactory(context.getRandomManager());
        playerManager.spawnPlayer(80f, dimensions.getWorldHeight() / 2f, config);

        gameLoopManager = new GameLoop();
        gameLoopManager.addStep(new InputStep(context.getInputOutputManager(), context.getEventManager(), gameState));
        gameLoopManager.addStep(new SpawnSystem(entityManager,
                entityFactory.getObstacleFactory(), entityFactory.getCollectibleFactory(),
                context.getRandomManager(), gameState, config, dimensions,
                endingSceneController.isActive() ? 0f : 3f));
        gameLoopManager.addStep(new UpdateStep(entityManager, context.getTimeManager(), gameState));
        gameLoopManager.addStep(new MovementStep(entityManager, context.getMovementManager(), context.getTimeManager(), gameState, dimensions));
        gameLoopManager.addStep(new CollisionSystem(entityManager, context.getCollisionManager(), context.getEventManager(), gameState, activeEffects));
        gameLoopManager.addStep(new EffectStep(activeEffects, context.getTimeManager()));
        gameLoopManager.addStep(new DespawnSystem(entityManager, dimensions));
        entityManager.flush();
    }

    @Override
    public void update(float delta) {
        if (introTimer > 0f)       introTimer       = Math.max(0f, introTimer - delta);
        if (stageOverlayTimer > 0f) stageOverlayTimer = Math.max(0f, stageOverlayTimer - delta);

        float     screenW   = Gdx.graphics.getWidth();
        float     screenH   = Gdx.graphics.getHeight();
        GameState gameState = gameSession.getGameState();
        Player    player    = (Player) entityManager.getFirstByTag(Tags.PLAYER);

        // Dismiss instructions overlay
        if (showingInstructionsOverlay) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                    || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                showingInstructionsOverlay = false;
                paused = true;
            }
            return;
        }

        // Toggle pause with P or ESC while actively playing
        if (!gameState.isDeathSequenceActive() && !endingSceneController.isActive()
                && (Gdx.input.isKeyJustPressed(Input.Keys.P)
                 || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))) {
            paused = !paused;
            // ── MUSIC PAUSE FIX ─────────────────────────────────────────────
            if (paused) {
                context.getSoundManager().pauseMusic();
            } else {
                context.getSoundManager().resumeMusic();
            }
            // ─────────────────────────────────────────────────────────────────
        }

        if (paused) {
            handlePauseInput();
            return;
        }

        if (endingSceneController.update(delta, player, context.getInputOutputManager(), sceneManager, gameState, context.getSoundManager())) {
            return;
        }

        if (!gameState.isDeathSequenceActive()) {
            gameLoopManager.update(delta);
            factPopupRenderer.update(delta);
        }

        player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        deathController.update(delta, player, cameraController, endingSceneController.isSafeEndingWindow());

        if (deathController.routeToGameOverIfRequested()) return;
        if (stageController.isTransitioning()) return;

        stageController.update();
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        worldViewport.update((int) screenW, (int) screenH, false);

        Gdx.gl.glClearColor(stageConfig.getClearR(), stageConfig.getClearG(), stageConfig.getClearB(), 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Player player = playerManager != null ? playerManager.getPlayer() : null;
        cameraController.apply(player);
        worldViewport.apply(false);

        rendererManager.getBatch().setProjectionMatrix(cameraController.getCamera().combined);
        rendererManager.getShapeRenderer().setProjectionMatrix(cameraController.getCamera().combined);

        if (activeEffects != null && activeEffects.isScreenShaking()) {
            float mag = activeEffects.getScreenShakeMagnitude();
            float ox  = context.getRandomManager().range(-mag, mag);
            float oy  = context.getRandomManager().range(-mag, mag);
            cameraController.getCamera().position.add(ox, oy, 0f);
            cameraController.getCamera().update();
            rendererManager.getBatch().setProjectionMatrix(cameraController.getCamera().combined);
            rendererManager.getShapeRenderer().setProjectionMatrix(cameraController.getCamera().combined);
        }

        float worldW      = dimensions.getWorldWidth();
        float worldH      = dimensions.getWorldHeight();
        float cameraLeft  = cameraController.getCamera().position.x - (worldW / 2f);
        float cameraBottom = cameraController.getCamera().position.y - (worldH / 2f);

        if (stageBackgroundTexture != null) {
            SpriteBatch worldBatch = rendererManager.getBatch();
            worldBatch.begin();
            worldBatch.draw(stageBackgroundTexture, cameraLeft, cameraBottom, worldW, worldH);
            worldBatch.end();
        }

        rendererManager.render(entityManager.getRenderables());

        SpriteBatch worldBatch = rendererManager.getBatch();
        worldBatch.begin();
        if (smokeEffect != null) {
            float smokeAlpha = activeEffects != null
                    ? activeEffects.getSmokeOverlayAlpha(smokeEffect.getBaseOverlayAlpha())
                    : smokeEffect.getBaseOverlayAlpha();
            smokeEffect.render(worldBatch, cameraLeft, cameraBottom, worldW, worldH, smokeAlpha);
        }
        worldBatch.end();

        SpriteBatch hudBatch = rendererManager.getBatch();
        Gdx.gl.glViewport(0, 0, (int) screenW, (int) screenH);
        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        rendererManager.getShapeRenderer().setProjectionMatrix(hudBatch.getProjectionMatrix());
        hudRenderer.render(rendererManager.getShapeRenderer(), hudBatch, screenW, screenH);

        if (activeEffects != null && activeEffects.isOilBlotActive()) {
            if (!oilSplotchPatternReady) { generateOilSplotchPattern(); oilSplotchPatternReady = true; }
            oilSplotchAnimTime += Gdx.graphics.getDeltaTime();
            renderOilBlotOverlay(screenW, screenH);
        } else {
            oilSplotchPatternReady = false;
            oilSplotchAnimTime     = 0f;
        }

        float hudScale = screenH / 1080f;
        introFont.getData().setScale(1.7f * hudScale);
        List<DebuffOverlayInfo> debuffOverlayInfo = buildDebuffOverlayList(player);

        hudBatch.begin();

        if (player != null && heartFullTexture != null && heartEmptyTexture != null) {
            GameState gameState    = gameSession.getGameState();
            int       maxHealth    = gameState.getMaxHearts();
            int       currentHealth = gameState.getHearts();
            float     shakeTimer   = player.getShakeTimer();
            float     heartSize    = 32f * hudScale;
            float     startX       = 20f * hudScale;
            float     startY       = screenH - 60f * hudScale;
            float     spacing      = 44f * hudScale;

            for (int i = 0; i < maxHealth; i++) {
                float drawX = startX + (i * spacing);
                float drawY = startY;
                if (shakeTimer > 0f) {
                    float shakeIntensity = shakeTimer / 0.5f;
                    if (i == currentHealth) {
                        float maxShake = 10f;
                        drawX += context.getRandomManager().range(-maxShake, maxShake) * shakeIntensity;
                        drawY += context.getRandomManager().range(-maxShake, maxShake) * shakeIntensity;
                    } else if (i < currentHealth) {
                        float minorShake = 2f;
                        drawX += context.getRandomManager().range(-minorShake, minorShake) * shakeIntensity;
                        drawY += context.getRandomManager().range(-minorShake, minorShake) * shakeIntensity;
                    }
                }
                if (i < currentHealth) { hudBatch.draw(heartFullTexture,  drawX, drawY, heartSize, heartSize); }
                else                   { hudBatch.draw(heartEmptyTexture, drawX, drawY, heartSize, heartSize); }
            }
        }

        if (!debuffOverlayInfo.isEmpty()) {
            renderDebuffCountdownText(hudBatch, screenW, screenH, hudScale, debuffOverlayInfo);
        }

        factPopupRenderer.render(hudBatch, screenW, screenH);

        if (showIntroText && introTimer > 0f) {
            introLayout.setText(introFont, "Restore the world.");
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.62f);

            introLayout.setText(introFont, "Collect clean pickups.");
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.54f);

            introLayout.setText(introFont, "Avoid hazards.");
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.46f);
        }

        if (showStageOverlay && stageOverlayTimer > 0f) {
            String header = stagePlan.isFinalStage(stageConfig.getSceneId())
                    ? "Cleanup Complete" : "Checkpoint Reached";
            introLayout.setText(introFont, header);
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.76f);
            introLayout.setText(introFont, stageConfig.getTitle());
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.68f);
            introLayout.setText(introFont, stageConfig.getSubtitle());
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.61f);
        }

        if (endingSceneController.isActive() && endingSceneController.isAwaitingContinue()) {
            introLayout.setText(introFont, "You have saved the world.");
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.62f);
            introLayout.setText(introFont, "Let us all do our part to keep Earth clean.");
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.54f);
            introLayout.setText(introFont, "Press SPACE to continue playing");
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.44f);
            introLayout.setText(introFont, "or ESC to go to game over");
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.38f);
        }

        if (endingSceneController.isActive() && endingSceneController.isSpawnWarmup()) {
            String warmupText = "Take control in "
                    + (int) Math.ceil(endingSceneController.getSpawnWarmupTimer()) + "...";
            introLayout.setText(introFont, warmupText);
            introFont.draw(hudBatch, introLayout, (screenW - introLayout.width) / 2f, screenH * 0.44f);
        }

        if (!paused && !showingInstructionsOverlay && !endingSceneController.isActive()) {
            String pauseHint = "Press P to Pause";
            introLayout.setText(introFont, pauseHint);
            float hintX = screenW - introLayout.width - (20f * hudScale);
            float hintY = 34f * hudScale;
            introFont.draw(hudBatch, introLayout, hintX, hintY);
        }

        hudBatch.end();

        if (showingInstructionsOverlay) {
            renderInstructionsOverlay(screenW, screenH);
        } else if (paused) {
            renderPauseOverlay(screenW, screenH);
        }

        renderBrightnessOverlay(screenW, screenH);
    }

    @Override
    public void onExit() {
        if (goodCollectedProgressListener != null)
            context.getEventManager().unsubscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);
        if (badCollectedListener != null)
            context.getEventManager().unsubscribe(GameEvents.BAD_COLLECTED, badCollectedListener);
        if (badHitListener != null)
            context.getEventManager().unsubscribe(GameEvents.BAD_HIT, badHitListener);
        if (obstaclePassedListener != null)
            context.getEventManager().unsubscribe(GameEvents.OBSTACLE_PASSED, obstaclePassedListener);
    }

    @Override
    public void disposeResources() {
        super.disposeResources();
        disposeOwnedBackground();
        if (rendererManager != null)    rendererManager.dispose();
        if (entityManager != null)      entityManager.dispose();
        if (hudRenderer != null)        hudRenderer.dispose();
        if (factPopupRenderer != null)  factPopupRenderer.dispose();
        if (introFont != null)          introFont.dispose();
        if (pauseBatch != null)         pauseBatch.dispose();
        brightnessOverlayRenderer.dispose();
    }

    private void showDebuffMessage(String message) {
        debuffMessage      = message;
        debuffMessageTimer = DEBUFF_MESSAGE_DURATION;
    }

    // ── pause helpers ────────────────────────────────────────────────────────

    private void handlePauseInput() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float scale   = screenH / 1080f;
        float popupW  = 500f * scale;
        float popupH  = 620f * scale;
        float popupX  = (screenW - popupW) / 2f;
        float popupY  = (screenH - popupH) / 2f;
        float btnW    = BUTTON_BASE_WIDTH  * scale;
        float btnH    = BUTTON_BASE_HEIGHT * scale;
        float btnX    = popupX + (popupW - btnW) / 2f;

        float resumeY  = popupY + popupH * 0.61f;
        float helpY    = popupY + popupH * 0.45f;
        float restartY = popupY + popupH * 0.29f;
        float quitY    = popupY + popupH * 0.13f;

        if (isPauseButtonClicked(btnX, resumeY, btnW, btnH, screenH)) {
            paused = false;
            context.getSoundManager().resumeMusic();       // resume music on Resume
        } else if (isPauseButtonClicked(btnX, helpY, btnW, btnH, screenH)) {
            paused = false;
            showingInstructionsOverlay = true;
            // music stays paused while instructions are open
        } else if (isPauseButtonClicked(btnX, restartY, btnW, btnH, screenH)) {
            paused = false;
            context.getSoundManager().resumeMusic();       // resume before restart
            gameSession.resetForNewRun();
            gameSession.prepareForStageEntry();
            sceneManager.switchTo(stagePlan.getInitialStageId());
        } else if (isPauseButtonClicked(btnX, quitY, btnW, btnH, screenH)) {
            paused = false;
            context.getSoundManager().resumeMusic();       // let menu start fresh
            gameSession.resetForNewRun();
            sceneManager.switchTo(GameSceneId.MENU.id());
        }
    }

    private void renderPauseOverlay(float screenW, float screenH) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = rendererManager.getShapeRenderer();
        sr.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0f, 0f, 0f, 0.6f);
        sr.rect(0, 0, screenW, screenH);
        sr.end();

        pauseCamera.setToOrtho(false, screenW, screenH);
        pauseBatch.setProjectionMatrix(pauseCamera.combined);
        pauseBatch.begin();

        float scale  = screenH / 1080f;
        float popupW = 500f * scale;
        float popupH = 620f * scale;
        float popupX = (screenW - popupW) / 2f;
        float popupY = (screenH - popupH) / 2f;

        if (pauseBgTex != null) pauseBatch.draw(pauseBgTex, popupX, popupY, popupW, popupH);

        float btnW   = BUTTON_BASE_WIDTH  * scale;
        float btnH   = BUTTON_BASE_HEIGHT * scale;
        float btnX   = popupX + (popupW - btnW) / 2f;

        drawOverlayButton(pauseBatch, pauseResume1,  pauseResume2,  btnX, popupY + popupH * 0.61f, btnW, btnH, screenH);
        drawOverlayButton(pauseBatch, pauseHelp1,    pauseHelp2,    btnX, popupY + popupH * 0.45f, btnW, btnH, screenH);
        drawOverlayButton(pauseBatch, pauseRestart1, pauseRestart2, btnX, popupY + popupH * 0.29f, btnW, btnH, screenH);
        drawOverlayButton(pauseBatch, pauseQuit1,    pauseQuit2,    btnX, popupY + popupH * 0.13f, btnW, btnH, screenH);

        pauseBatch.end();
    }

    private void renderInstructionsOverlay(float screenW, float screenH) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = rendererManager.getShapeRenderer();
        sr.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0f, 0f, 0f, 0.45f);
        sr.rect(0, 0, screenW, screenH);
        sr.end();

        pauseCamera.setToOrtho(false, screenW, screenH);
        pauseBatch.setProjectionMatrix(pauseCamera.combined);
        pauseBatch.begin();

        if (instructionsTexture != null) {
            float panelW = screenW * 0.88f;
            float panelH = screenH * 0.86f;
            pauseBatch.draw(instructionsTexture,
                    (screenW - panelW) * 0.5f, (screenH - panelH) * 0.5f, panelW, panelH);
        }

        pauseBatch.end();
    }

    private void renderBrightnessOverlay(float screenW, float screenH) {
        pauseCamera.setToOrtho(false, screenW, screenH);
        pauseBatch.setProjectionMatrix(pauseCamera.combined);
        pauseBatch.begin();
        brightnessOverlayRenderer.render(pauseBatch, context.getGameSettings());
        pauseBatch.end();
    }

    private void drawOverlayButton(SpriteBatch batch, Texture normal, Texture pressed,
                                   float bx, float by, float bw, float bh, float screenH) {
        boolean hovered   = isPauseHovered(bx, by, bw, bh, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture tex = isPressed ? pressed : normal;
        if (tex != null) batch.draw(tex, bx, by, bw, bh);
    }

    private boolean isPauseHovered(float bx, float by, float bw, float bh, float screenH) {
        float mx = Gdx.input.getX();
        float my = screenH - Gdx.input.getY();
        return mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
    }

    private boolean isPauseButtonClicked(float bx, float by, float bw, float bh, float screenH) {
        return isPauseHovered(bx, by, bw, bh, screenH)
                && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    private String resolveStageBackgroundKey(String sceneId) {
        if (GameSceneId.STAGE_ONE.id().equals(sceneId))   return STAGE_ONE_BACKGROUND_KEY;
        if (GameSceneId.STAGE_TWO.id().equals(sceneId))   return STAGE_TWO_BACKGROUND_KEY;
        if (GameSceneId.STAGE_THREE.id().equals(sceneId)) return STAGE_THREE_BACKGROUND_KEY;
        if (GameSceneId.STAGE_FOUR.id().equals(sceneId))  return STAGE_FOUR_BACKGROUND_KEY;
        return STAGE_ONE_BACKGROUND_KEY;
    }

    private void generateOilSplotchPattern() {
        for (int i = 0; i < OIL_SPLOTCH_COUNT; i++) {
            oilSplotchXNorm[i]    = context.getRandomManager().range(0.05f, 0.95f);
            oilSplotchYNorm[i]    = context.getRandomManager().range(0.05f, 0.95f);
            oilSplotchSizeNorm[i] = context.getRandomManager().range(0.08f, 0.13f);
            oilSplotchRotation[i] = context.getRandomManager().range(0f, 360f);
        }
    }

    private void renderOilBlotOverlay(float screenW, float screenH) {
        ShapeRenderer shapeRenderer = rendererManager.getShapeRenderer();
        float intensity    = activeEffects.getOilBlotIntensity();
        float alpha        = 0.18f + (0.26f * intensity);
        float minScreenDim = Math.min(screenW, screenH);

        float[] lobeDistance = {0.62f, 0.74f, 0.58f, 0.70f, 0.64f};
        float[] lobeSize     = {0.58f, 0.44f, 0.52f, 0.40f, 0.47f};

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.04f, 0.04f, 0.04f, alpha);
        for (int i = 0; i < OIL_SPLOTCH_COUNT; i++) {
            float centerX   = oilSplotchXNorm[i] * screenW;
            float centerY   = oilSplotchYNorm[i] * screenH;
            float baseRadius = oilSplotchSizeNorm[i] * minScreenDim;
            float rotation  = oilSplotchRotation[i];
            float phase     = oilSplotchAnimTime * 3.2f + (i * 0.9f);
            float wobble    = 1f + (0.06f * MathUtils.sin(phase));
            float wobbleX   = MathUtils.cos(phase * 0.8f) * baseRadius * 0.07f;
            float wobbleY   = MathUtils.sin(phase * 1.1f) * baseRadius * 0.05f;

            centerX    += wobbleX;
            centerY    += wobbleY;
            baseRadius *= wobble;

            shapeRenderer.circle(centerX, centerY, baseRadius);
            for (int l = 0; l < lobeDistance.length; l++) {
                float angle      = rotation + (l * (360f / lobeDistance.length));
                float offset     = baseRadius * lobeDistance[l];
                float lobeRadius = baseRadius * lobeSize[l];
                float lx = centerX + MathUtils.cosDeg(angle) * offset;
                float ly = centerY + MathUtils.sinDeg(angle) * offset;
                shapeRenderer.circle(lx, ly, lobeRadius);
            }
            float dripX   = centerX + MathUtils.cosDeg(rotation + 230f) * baseRadius * 0.55f;
            float dripY   = centerY + MathUtils.sinDeg(rotation + 230f) * baseRadius * 0.55f;
            float dripDrop = (0.35f + 0.65f * ((MathUtils.sin(phase * 1.7f) + 1f) * 0.5f)) * baseRadius;
            shapeRenderer.circle(dripX, dripY,             baseRadius * 0.24f);
            shapeRenderer.circle(dripX, dripY - dripDrop,  baseRadius * 0.17f);
        }
        shapeRenderer.end();
    }

    private List<DebuffOverlayInfo> buildDebuffOverlayList(Player player) {
        List<DebuffOverlayInfo> debuffs = new ArrayList<>();
        if (player != null) {
            if (player.getReversedFlightTimer() > 0f)
                debuffs.add(new DebuffOverlayInfo("Reversed Flight",   player.getReversedFlightTimer(),    PLASTIC_BOTTLE_DURATION));
            if (player.getJumpIntervalDebuffTimer() > 0f)
                debuffs.add(new DebuffOverlayInfo("Slow Jumps",        player.getJumpIntervalDebuffTimer(), FACTORY_SMOKE_DURATION));
            if (player.getControlLockTimer() > 0f)
                debuffs.add(new DebuffOverlayInfo("Control Lock",      player.getControlLockTimer(),        FACTORY_SMOKE_DURATION));
        }
        if (activeEffects != null) {
            if (activeEffects.getOilBlotTimer() > 0f)
                debuffs.add(new DebuffOverlayInfo("Obstructed Vision", activeEffects.getOilBlotTimer(),    OIL_BLOT_DURATION));
            if (activeEffects.getTrashRattleTimer() > 0f)
                debuffs.add(new DebuffOverlayInfo("Screen Shake",      activeEffects.getTrashRattleTimer(), TRASH_PILE_SHAKE_DURATION));
        }
        return debuffs;
    }

    private void renderDebuffCountdownText(SpriteBatch hudBatch, float screenW, float screenH,
                                           float hudScale, List<DebuffOverlayInfo> debuffs) {
        if (debuffs.isEmpty()) return;
        float s       = hudScale;
        float pad     = 16f * s;
        float pillW   = 300f * s;
        float pillH   = 64f  * s;
        float pillGap = 10f  * s;
        float pillX   = (screenW - pillW) * 0.5f;

        float barY         = screenH - pad - (18f * s);
        float stageTitleY  = barY - (HUD_STAGE_TITLE_OFFSET * s);
        float firstPillY   = stageTitleY - (HUD_STAGE_TO_DEBUFF_GAP * s) - pillH;

        hudBatch.end();

        ShapeRenderer sr = rendererManager.getShapeRenderer();
        sr.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < debuffs.size(); i++) {
            DebuffOverlayInfo info     = debuffs.get(i);
            float             pillY   = firstPillY - i * (pillH + pillGap);
            float             progress = info.durationSeconds > 0f
                    ? Math.min(1f, info.timerSeconds / info.durationSeconds) : 0f;
            sr.setColor(0f, 0f, 0f, 0.55f);
            sr.rect(pillX, pillY, pillW, pillH);
            sr.setColor(1f, 0.45f + 0.35f * progress, 0f, 0.85f);
            sr.rect(pillX, pillY, pillW * progress, pillH * 0.22f);
        }
        sr.end();

        hudBatch.begin();
        float textScale = 1.5f * s;
        introFont.getData().setScale(textScale);
        for (int i = 0; i < debuffs.size(); i++) {
            DebuffOverlayInfo info  = debuffs.get(i);
            float             pillY = firstPillY - i * (pillH + pillGap);
            int countdown = Math.max(1, (int) Math.ceil(Math.min(DEBUFF_POPUP_MAX_SECONDS, info.timerSeconds)));
            String label  = info.label + "  " + countdown + "s";
            introLayout.setText(introFont, label);
            float textX = pillX + (pillW - introLayout.width) / 2f;
            float textY = pillY + pillH * 0.75f;
            introFont.setColor(0f, 0f, 0f, 0.8f);
            introFont.draw(hudBatch, label, textX + 1.5f * s, textY - 1.5f * s);
            introFont.setColor(1f, 0.9f, 0.5f, 1f);
            introFont.draw(hudBatch, label, textX, textY);
        }
        introFont.setColor(Color.WHITE);
        hudBatch.end();
        introFont.getData().setScale(1.7f * s);
        hudBatch.begin();
    }

    private void enableFontSmoothing(BitmapFont bitmapFont) {
        for (TextureRegion region : bitmapFont.getRegions()) {
            region.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    private static final class DebuffOverlayInfo {
        private final String label;
        private final float  timerSeconds;
        private final float  durationSeconds;

        private DebuffOverlayInfo(String label, float timerSeconds, float durationSeconds) {
            this.label           = label;
            this.timerSeconds    = timerSeconds;
            this.durationSeconds = durationSeconds;
        }
    }
}
