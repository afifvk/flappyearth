package inf1009.p63.flappyearth.game.scenes;

import java.util.ArrayList;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.RenderManager;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.config.StageConfig;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;

import inf1009.p63.flappyearth.game.controllers.DeathController;
import inf1009.p63.flappyearth.game.controllers.EndingController;
import inf1009.p63.flappyearth.game.controllers.CameraController;
import inf1009.p63.flappyearth.game.controllers.HelpOverlayController;
import inf1009.p63.flappyearth.game.controllers.PauseOverlayController;
import inf1009.p63.flappyearth.game.controllers.StageProgressController;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.runtime.BrightnessOverlayRenderer;
import inf1009.p63.flappyearth.game.runtime.CollectibleSourceBoundsResolver;
import inf1009.p63.flappyearth.game.runtime.CollectibleSourceRegionBinder;
import inf1009.p63.flappyearth.game.runtime.GameplayBootstrapper;
import inf1009.p63.flappyearth.game.runtime.GameplayAudioController;
import inf1009.p63.flappyearth.game.runtime.GameplayEventHandler;
import inf1009.p63.flappyearth.game.runtime.GameplayEffectsOverlayRenderer;
import inf1009.p63.flappyearth.game.runtime.GameplayHudRenderer;
import inf1009.p63.flappyearth.game.runtime.GameplayOverlayRenderer;
import inf1009.p63.flappyearth.game.runtime.GameplayRenderer;
import inf1009.p63.flappyearth.game.runtime.SmokeEffect;
import inf1009.p63.flappyearth.game.runtime.StageTransitionController;
import inf1009.p63.flappyearth.game.runtime.GameplayUpdateCoordinator;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.util.FontUtils;

public class GameplayScene extends Scene {

    private static final String STAGE_ONE_BACKGROUND_KEY   = "textures/backgrounds/stage/stage1.png";
    private static final String STAGE_TWO_BACKGROUND_KEY   = "textures/backgrounds/stage/stage2.png";
    private static final String STAGE_THREE_BACKGROUND_KEY = "textures/backgrounds/stage/stage3.png";
    private static final String STAGE_FOUR_BACKGROUND_KEY  = "textures/backgrounds/stage/stage4.png";

    private final SceneManager sceneManager;
    private final EngineContext context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;
    private final StageConfig stageConfig;
    private final GameplayDimensions dimensions;

    private EntityStore entityStore;
    private RenderManager rendererManager;

    private Player player;
    private GameplayAudioController gameplayAudioController;
    private GameplayHudRenderer hudRenderer;
    private SmokeEffect smokeEffect;
    private GameplayBootstrapper gameplayBootstrapper;
    private GameplayRenderer gameplayRenderer;
    private GameplayOverlayRenderer gameplayOverlayRenderer;
    private GameplayUpdateCoordinator gameplayUpdateCoordinator;

    private inf1009.p63.flappyearth.game.factories.EntityFactory entityFactory;
    private inf1009.p63.flappyearth.game.systems.GameplayLoop gameLoopManager;
    private Viewport worldViewport;
    private StageProgressController stageController;
    private DeathController deathController;
    private CameraController cameraController;
    private EndingController endingSceneController;

    private GameplayEventHandler gameplayEventHandler;
    private ActiveEffects activeEffects;
    private Texture heartFullTexture;
    private Texture heartEmptyTexture;


    private Texture stageBackgroundTexture;
    private boolean stageBackgroundOwned = false;
    private PauseOverlayController pauseOverlayController;
    private HelpOverlayController helpOverlayController;

    private final List<Renderable> renderablesScratch = new ArrayList<>();
    private final CollectibleSourceBoundsResolver collectibleSourceBoundsResolver;
    private final CollectibleSourceRegionBinder collectibleSourceRegionBinder;
    private final GameplayEffectsOverlayRenderer gameplayEffectsOverlayRenderer;
    private final StageTransitionController stageTransitionController;

    private final BitmapFont              introFont;
    private final GlyphLayout             introLayout;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;

    private static final float   STAGE_TRANSITION_FADE = 0.45f;

    public GameplayScene(SceneManager sceneManager,
                     EngineContext context,
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

        this.entityStore   = new EntityStore();
        this.rendererManager = new RenderManager();
        this.rendererManager.setAssetManager(context.getAssetManager());
        this.collectibleSourceBoundsResolver = new CollectibleSourceBoundsResolver();
        this.collectibleSourceRegionBinder = new CollectibleSourceRegionBinder();
        this.gameplayEffectsOverlayRenderer = new GameplayEffectsOverlayRenderer();
        this.stageTransitionController = new StageTransitionController(STAGE_TRANSITION_FADE);
        this.collectibleSourceRegionBinder.bind(rendererManager, collectibleSourceBoundsResolver);
        this.gameplayBootstrapper = new GameplayBootstrapper();
        this.gameplayRenderer = new GameplayRenderer();
        this.gameplayOverlayRenderer = new GameplayOverlayRenderer();
        inf1009.p63.flappyearth.game.runtime.GameplaySession gameplayRuntimeSession =
            new inf1009.p63.flappyearth.game.runtime.GameplaySession();
        this.gameplayUpdateCoordinator = new GameplayUpdateCoordinator(
            sceneManager,
            context,
            gameSession,
            stagePlan,
            stageTransitionController,
            gameplayRuntimeSession
        );

        this.introFont = FontUtils.loadFontOrDefault("fonts/BoldPixels.ttf", 20);
        this.introFont.getData().setScale(1.7f);
        this.introFont.setUseIntegerPositions(false);
        enableFontSmoothing(this.introFont);
        this.introLayout = new GlyphLayout();

        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();
    }

    private Texture loadStageBackground(String key) {
        if (context.getAssetManager().isLoaded(key)) {
            stageBackgroundOwned = false;
            Gdx.app.log("GameplayScene", "BG from AssetManager: " + key);
            return context.getAssetManager().get(key, Texture.class);
        }
        if (Gdx.files.internal(key).exists()) {
            stageBackgroundOwned = true;
            Gdx.app.log("GameplayScene", "BG loaded directly from file: " + key);
            return new Texture(Gdx.files.internal(key));
        }
        Gdx.app.error("GameplayScene", "BG not found: " + key + " — falling back to stage1");
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
        if (!context.getAudioManager().isMusicPlaying()) {
            context.getAudioManager().playMusic(AudioKeys.MUSIC_GAME);
        }

        if (hudRenderer != null)     hudRenderer.dispose();
        if (entityStore != null)   entityStore.clear();

        disposeOwnedBackground();

        this.activeEffects   = gameSession.getActiveEffects();
        gameplayEffectsOverlayRenderer.reset();

        this.gameplayEventHandler = new GameplayEventHandler(
            context,
            entityStore,
            activeEffects,
            () -> this.player,
            () -> this.entityFactory
        );
        this.gameplayEventHandler.subscribe(gameSession);

        GameplayBootstrapper.Result bootstrap = gameplayBootstrapper.bootstrap(
            sceneManager,
            context,
            gameSession,
            stagePlan,
            stageConfig,
            dimensions,
            entityStore,
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
        gameLoopManager = bootstrap.gameplayLoop;

        String bgKey = resolveStageBackgroundKey(stageConfig.getSceneId());
        stageBackgroundTexture = loadStageBackground(bgKey);

        gameplayUpdateCoordinator.onEnter(gameSession.consumeFadeInRequest(), player);

        gameplayAudioController.onEnter(() -> this.player);
    }

    @Override
    public void update(float delta) {
        gameplayUpdateCoordinator.update(
                delta,
                entityStore,
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
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        worldViewport.update((int) screenW, (int) screenH, false);

        Gdx.gl.glClearColor(stageConfig.getClearR(), stageConfig.getClearG(), stageConfig.getClearB(), 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Player player = this.player;
        cameraController.apply(player);
        worldViewport.apply(false);

        gameplayRenderer.renderWorld(
                rendererManager,
                entityStore,
                cameraController,
                dimensions,
                activeEffects,
                context.getRandomManager(),
                renderablesScratch,
                stageBackgroundTexture,
                smokeEffect
        );

        gameplayRenderer.setupHudProjection(rendererManager, screenW, screenH);
        SpriteBatch hudBatch = rendererManager.getBatch();
        hudRenderer.render(rendererManager.getShapeRenderer(), hudBatch, screenW, screenH);

        gameplayEffectsOverlayRenderer.renderOilOverlay(
                rendererManager,
                screenW,
                screenH,
                activeEffects,
                context.getRandomManager()
        );

        float hudScale = screenH / 1080f;
        introFont.getData().setScale(1.7f * hudScale);

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

        gameplayEffectsOverlayRenderer.renderDebuffCountdownText(
            rendererManager,
            hudBatch,
            introFont,
            introLayout,
            screenW,
            screenH,
            hudScale,
            player,
            activeEffects
        );

        gameplayOverlayRenderer.renderHudTextOverlays(
                hudBatch,
                introFont,
                introLayout,
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
                rendererManager,
                gameplayRenderer,
                brightnessOverlayRenderer,
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

    @Override
    public void onExit() {
        if (gameplayEventHandler != null)
            gameplayEventHandler.unsubscribe();
        if (gameplayAudioController != null)
            gameplayAudioController.onExit();
    }

    @Override
    public void disposeResources() {
        super.disposeResources();
        disposeOwnedBackground();
        if (rendererManager != null)    rendererManager.dispose();
        if (entityStore != null)      entityStore.dispose();
        if (hudRenderer != null)        hudRenderer.dispose();
        if (introFont != null)          introFont.dispose();
        collectibleSourceBoundsResolver.clear();
        gameplayEffectsOverlayRenderer.reset();
        brightnessOverlayRenderer.dispose();
    }

    private String resolveStageBackgroundKey(String sceneId) {
        if (GameSceneId.STAGE_ONE.id().equals(sceneId))   return STAGE_ONE_BACKGROUND_KEY;
        if (GameSceneId.STAGE_TWO.id().equals(sceneId))   return STAGE_TWO_BACKGROUND_KEY;
        if (GameSceneId.STAGE_THREE.id().equals(sceneId)) return STAGE_THREE_BACKGROUND_KEY;
        if (GameSceneId.STAGE_FOUR.id().equals(sceneId))  return STAGE_FOUR_BACKGROUND_KEY;
        return STAGE_ONE_BACKGROUND_KEY;
    }

    private void enableFontSmoothing(BitmapFont bitmapFont) {
        for (TextureRegion region : bitmapFont.getRegions()) {
            region.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}
