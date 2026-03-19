package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.AssetKeys;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.runtime.BrightnessOverlayRenderer;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.util.EcoFactManager;
import inf1009.p63.flappyearth.game.util.FontUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MenuScene extends Scene {
    private EcoFactManager ecoFactManager;
    private BitmapFont ecoFactFont;
    private GlyphLayout ecoFactLayout;
    private String ecoFactToDisplay;
    private float ecoFactTimer = 0f;
    private float ecoFactAlpha = 1f;
    private boolean ecoFactFadingOut = false;
    private static final float ECO_FACT_CYCLE_TIME = 15f;
    private static final float ECO_FACT_FADE_TIME = 1f;
    private static float cubicEase(float t) {
        return t < 0 ? 0 : t > 1 ? 1 : (float)Math.pow(t, 3);
    }

    private static final float BUTTON_BASE_WIDTH = 320f;
    private static final float BUTTON_BASE_HEIGHT = 90f;
    private static final float START_Y_RATIO = 0.45f;
    private static final float BUTTON_VERTICAL_GAP_RATIO = 0.11f;

    private final SceneManager       sceneManager;
    private final EngineContext context;
    private final GameSession        gameSession;
    private final StagePlan          stagePlan;

    private final SpriteBatch        batch;
    private final OrthographicCamera camera;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;

    private Texture bgTexture;
    private Texture instructionsTexture;
    private Texture start1, start2;
    private Texture settings1, settings2;
    private Texture credits1, credits2;
    private Texture quit1, quit2;
    private boolean showingInstructions;

    public MenuScene(SceneManager sceneManager,
                     EngineContext context,
                     GameSession gameSession,
                     StagePlan stagePlan) {
        this.sceneManager = sceneManager;
        this.context      = context;
        this.gameSession  = gameSession;
        this.stagePlan    = stagePlan;

        this.batch  = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();
        this.ecoFactManager = new EcoFactManager("assets/eco_facts.txt");
        this.ecoFactFont = FontUtils.loadFontOrDefault("fonts/BoldPixels.ttf", 18);
        this.ecoFactFont.getData().setScale(1.5f);
        this.ecoFactFont.setUseIntegerPositions(false);
        this.ecoFactLayout = new GlyphLayout();
        this.ecoFactToDisplay = ecoFactManager.getRandomFact();
    }

    @Override
    public void onEnter() {
        bgTexture = context.getAssetManager().get("textures/backgrounds/menu.png",  Texture.class);
        instructionsTexture = context.getAssetManager().get(AssetKeys.INSTRUCTIONS_BG, Texture.class);
        start1    = context.getAssetManager().get("textures/ui/buttons/start_1.png",    Texture.class);
        start2    = context.getAssetManager().get("textures/ui/buttons/start_2.png",    Texture.class);
        settings1 = context.getAssetManager().get("textures/ui/buttons/settings_1.png", Texture.class);
        settings2 = context.getAssetManager().get("textures/ui/buttons/settings_2.png", Texture.class);
        credits1  = context.getAssetManager().get("textures/ui/buttons/credits_1.png",  Texture.class);
        credits2  = context.getAssetManager().get("textures/ui/buttons/credits_2.png",  Texture.class);
        quit1     = context.getAssetManager().get("textures/ui/buttons/quit_1.png",     Texture.class);
        quit2     = context.getAssetManager().get("textures/ui/buttons/quit_2.png",     Texture.class);
        showingInstructions = false;
        
        context.getAudioManager().playMusic(AudioKeys.MUSIC_MENU);
        // Refresh eco fact on menu enter
        this.ecoFactToDisplay = ecoFactManager.getRandomFact();
        this.ecoFactTimer = 0f;
        this.ecoFactAlpha = 1f;
        this.ecoFactFadingOut = false;
    }

    @Override
    public void update(float delta) {
        if (showingInstructions) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                context.getAudioManager().playSound(AudioKeys.UI_CLICK);
                context.getAudioManager().playMusic(AudioKeys.MUSIC_GAME);
                showingInstructions = false;
                gameSession.resetForNewRun();
                sceneManager.switchTo(stagePlan.getInitialStageId());
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                context.getAudioManager().playSound(AudioKeys.UI_CLICK);
                showingInstructions = false;
            }
            return;
        }

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float scale   = screenH / 1080f;
        float btnW    = BUTTON_BASE_WIDTH * scale;
        float btnH    = BUTTON_BASE_HEIGHT * scale;
        float btnX    = (screenW - btnW) / 2f;

        float startY = screenH * START_Y_RATIO;
        float settingsY = startY - (screenH * BUTTON_VERTICAL_GAP_RATIO);
        float creditsY = settingsY - (screenH * BUTTON_VERTICAL_GAP_RATIO);
        float quitY = creditsY - (screenH * BUTTON_VERTICAL_GAP_RATIO);

        if (isButtonClicked(btnX, startY, btnW, btnH, screenH)) {
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            showingInstructions = true;
        } else if (isButtonClicked(btnX, settingsY, btnW, btnH, screenH)) {
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            sceneManager.switchTo(GameSceneId.SETTINGS.id());
        } else if (isButtonClicked(btnX, creditsY, btnW, btnH, screenH)) {
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            sceneManager.switchTo(GameSceneId.CREDITS.id());
        } else if (isButtonClicked(btnX, quitY, btnW, btnH, screenH)) {
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            Gdx.app.exit();
        }

        // Eco fact cycling logic
        ecoFactTimer += delta;
        if (!ecoFactFadingOut && ecoFactTimer >= ECO_FACT_CYCLE_TIME) {
            ecoFactFadingOut = true;
            ecoFactTimer = 0f;
        }
        // Fade out current fact
        if (ecoFactFadingOut) {
            ecoFactAlpha -= delta / ECO_FACT_FADE_TIME;
            if (ecoFactAlpha <= 0f) {
                ecoFactAlpha = 0f;
                ecoFactToDisplay = ecoFactManager.getRandomFact();
                ecoFactFadingOut = false;
            }
        } else if (ecoFactAlpha < 1f) {
            ecoFactAlpha += delta / ECO_FACT_FADE_TIME;
            if (ecoFactAlpha > 1f) ecoFactAlpha = 1f;
        }
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenW, screenH);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        if (showingInstructions) {
            if (instructionsTexture != null) {
                batch.draw(instructionsTexture, 0, 0, screenW, screenH);
            }

            batch.end();
            batch.begin();
            brightnessOverlayRenderer.render(batch, context.getBrightness());
            batch.end();
            return;
        } else if (bgTexture != null) {
            batch.draw(bgTexture, 0, 0, screenW, screenH);
        }

        float scale = screenH / 1080f;
        float btnW = BUTTON_BASE_WIDTH * scale;
        float btnH = BUTTON_BASE_HEIGHT * scale;
        float btnX = (screenW - btnW) / 2f;
        float startY = screenH * START_Y_RATIO;
        float settingsY = startY - (screenH * BUTTON_VERTICAL_GAP_RATIO);
        float creditsY = settingsY - (screenH * BUTTON_VERTICAL_GAP_RATIO);
        float quitY = creditsY - (screenH * BUTTON_VERTICAL_GAP_RATIO);

        drawButton(start1, start2, btnX, startY, btnW, btnH, screenH);
        drawButton(settings1, settings2, btnX, settingsY, btnW, btnH, screenH);
        drawButton(credits1, credits2, btnX, creditsY, btnW, btnH, screenH);
        drawButton(quit1, quit2, btnX, quitY, btnW, btnH, screenH);

        batch.end();

        batch.begin();
        brightnessOverlayRenderer.render(batch, context.getBrightness());
        batch.end();

        // Draw eco fact slightly above bottom with smooth fade
        batch.begin();
        float ecoFactBaseY = 60f * (screenH / 1080f);
        ecoFactLayout.setText(ecoFactFont, ecoFactToDisplay);
        float ecoFactX = (screenW - ecoFactLayout.width) / 2f;
        ecoFactFont.setColor(1f, 1f, 1f, cubicEase(ecoFactAlpha));
        ecoFactFont.draw(batch, ecoFactLayout, ecoFactX, ecoFactBaseY);
        ecoFactFont.setColor(1f, 1f, 1f, 1f); // Reset alpha
        batch.end();
    }

    @Override
    public void onExit() {}

    @Override
    public void disposeResources() {
        if (batch != null) batch.dispose();
        brightnessOverlayRenderer.dispose();
    }

    private void drawButton(Texture normal, Texture pressed,
                            float bx, float by, float bw, float bh, float screenH) {
        boolean hovered   = isHovered(bx, by, bw, bh, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture tex = isPressed ? pressed : normal;
        if (tex != null) batch.draw(tex, bx, by, bw, bh);
    }

    private boolean isHovered(float bx, float by, float bw, float bh, float screenH) {
        float mx = Gdx.input.getX();
        float my = screenH - Gdx.input.getY();
        return mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
    }

    private boolean isButtonClicked(float bx, float by, float bw, float bh, float screenH) {
        return isHovered(bx, by, bw, bh, screenH)
                && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }
}
