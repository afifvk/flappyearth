package inf1009.p63.flappyearth.game.runtime;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.RenderManager;
import inf1009.p63.flappyearth.game.util.FontUtils;

public class GameplaySceneResources {

    private static final String STAGE_ONE_BACKGROUND_KEY = "textures/backgrounds/stage/stage1.png";
    private static final String[] COLLECTIBLE_ASSET_KEYS = {
        "textures/entities/collectibles/bad/garbage.png",
        "textures/entities/collectibles/bad/oil_spill.png",
        "textures/entities/collectibles/bad/factory.png",
        "textures/entities/collectibles/bad/plastic_bottle.png",
        "textures/entities/collectibles/good/recycling_bin.png",
        "textures/entities/collectibles/good/solar_panel.png",
        "textures/entities/collectibles/good/sapling.png",
        "textures/entities/collectibles/good/greenhouse.png",
        "textures/entities/collectibles/good/wind_turbine.png"
    };

    private final EntityStore entityStore;
    private final RenderManager renderManager;
    private final CollectibleSourceBoundsResolver collectibleSourceBoundsResolver;
    private final GameplayEffectsOverlayRenderer gameplayEffectsOverlayRenderer;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;
    private final BitmapFont introFont;
    private final GlyphLayout introLayout;
    private final List<Renderable> renderablesScratch = new ArrayList<>();

    private Texture stageBackgroundTexture;
    private boolean stageBackgroundOwned;
    private boolean collectibleBoundsPrimed;
    private int lastViewportWidth = -1;
    private int lastViewportHeight = -1;
    private boolean disposed;

    public GameplaySceneResources(EngineContext context) {
        this.entityStore = new EntityStore();
        this.renderManager = new RenderManager();
        this.renderManager.setAssetManager(context.getAssetManager());
        this.collectibleSourceBoundsResolver = new CollectibleSourceBoundsResolver();
        this.renderManager.setSourceRegionResolver((renderData, texture) ->
                collectibleSourceBoundsResolver.resolve(renderData.assetKey, texture));
        this.gameplayEffectsOverlayRenderer = new GameplayEffectsOverlayRenderer();
        this.introFont = FontUtils.loadFontOrDefault("fonts/BoldPixels.ttf", 20);
        this.introFont.getData().setScale(1.7f);
        this.introFont.setUseIntegerPositions(false);
        enableFontSmoothing(this.introFont);
        this.introLayout = new GlyphLayout();
        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();
    }

    public void prepareForStage(EngineContext context, String backgroundKey) {
        disposed = false;
        disposeOwnedBackground();
        stageBackgroundTexture = loadStageBackground(context, backgroundKey);
        primeCollectibleBounds(context);
        gameplayEffectsOverlayRenderer.reset();
    }

    public void updateViewportIfNeeded(Viewport worldViewport, int screenW, int screenH) {
        if (worldViewport == null) {
            return;
        }
        if (screenW == lastViewportWidth && screenH == lastViewportHeight) {
            return;
        }
        worldViewport.update(screenW, screenH, false);
        lastViewportWidth = screenW;
        lastViewportHeight = screenH;
    }

    public EntityStore entityStore() {
        return entityStore;
    }

    public RenderManager renderManager() {
        return renderManager;
    }

    public GameplayEffectsOverlayRenderer effectsOverlayRenderer() {
        return gameplayEffectsOverlayRenderer;
    }

    public BrightnessOverlayRenderer brightnessOverlayRenderer() {
        return brightnessOverlayRenderer;
    }

    public BitmapFont introFont() {
        return introFont;
    }

    public GlyphLayout introLayout() {
        return introLayout;
    }

    public List<Renderable> renderablesScratch() {
        return renderablesScratch;
    }

    public Texture stageBackgroundTexture() {
        return stageBackgroundTexture;
    }

    public void dispose() {
        if (disposed) {
            return;
        }
        disposeOwnedBackground();
        safeDisposeRenderManager();
        entityStore.dispose();
        collectibleSourceBoundsResolver.clear();
        gameplayEffectsOverlayRenderer.reset();
        if (introFont != null) {
            introFont.dispose();
        }
        brightnessOverlayRenderer.dispose();
        disposed = true;
    }

    private Texture loadStageBackground(EngineContext context, String key) {
        try {
            if (context.getAssetManager().isLoaded(key)) {
                stageBackgroundOwned = false;
                return context.getAssetManager().get(key, Texture.class);
            }
            if (Gdx.files.internal(key).exists()) {
                stageBackgroundOwned = true;
                return new Texture(Gdx.files.internal(key));
            }
            if (context.getAssetManager().isLoaded(STAGE_ONE_BACKGROUND_KEY)) {
                stageBackgroundOwned = false;
                return context.getAssetManager().get(STAGE_ONE_BACKGROUND_KEY, Texture.class);
            }
            if (Gdx.files.internal(STAGE_ONE_BACKGROUND_KEY).exists()) {
                stageBackgroundOwned = true;
                return new Texture(Gdx.files.internal(STAGE_ONE_BACKGROUND_KEY));
            }
        } catch (Exception ex) {
            Gdx.app.error("GameplaySceneResources", "Failed to load stage background: " + key, ex);
        }
        stageBackgroundOwned = false;
        return null;
    }

    private void disposeOwnedBackground() {
        if (stageBackgroundOwned && stageBackgroundTexture != null) {
            try {
                stageBackgroundTexture.dispose();
            } catch (Exception ex) {
                Gdx.app.error("GameplaySceneResources", "Failed to dispose owned stage background", ex);
            }
            stageBackgroundTexture = null;
            stageBackgroundOwned = false;
        }
    }

    private void primeCollectibleBounds(EngineContext context) {
        if (collectibleBoundsPrimed) {
            return;
        }

        for (String assetKey : COLLECTIBLE_ASSET_KEYS) {
            if (!context.getAssetManager().isLoaded(assetKey)) {
                continue;
            }
            collectibleSourceBoundsResolver.resolve(
                    assetKey,
                    context.getAssetManager().get(assetKey, Texture.class)
            );
        }
        collectibleBoundsPrimed = true;
    }

    private void enableFontSmoothing(BitmapFont bitmapFont) {
        for (TextureRegion region : bitmapFont.getRegions()) {
            region.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    private void safeDisposeRenderManager() {
        try {
            renderManager.dispose();
        } catch (Exception ex) {
            Gdx.app.error("GameplaySceneResources", "Failed to dispose render manager", ex);
        }
    }
}
