package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.GameSettings;
import inf1009.p63.flappyearth.game.managers.BrightnessOverlayRenderer;

public class SettingsScene extends Scene {

    private static final float BUTTON_BASE_WIDTH = 320f;
    private static final float BUTTON_BASE_HEIGHT = 90f;
    private static final float CONTROL_BUTTON_BASE_SIZE = 120f;
    private static final float CONTROL_ROW_GAP_RATIO = 0.12f;
    private static final float CONTROL_BUTTON_GAP_RATIO = 0.08f;
    private static final float SETTING_STEP = 0.1f;
    private static final float BAR_WIDTH_BASE = 220f;
    private static final float BAR_HEIGHT_BASE = 18f;

    private final SceneManager       sceneManager;
    private final GameContextManager context;

    private final SpriteBatch        batch;
    private final OrthographicCamera camera;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;

    private Texture bgTexture;
    private Texture back1, back2;
    private Texture brightnessDown1, brightnessDown2;
    private Texture brightnessUp1, brightnessUp2;
    private Texture volumeDown1, volumeDown2;
    private Texture volumeUp1, volumeUp2;
    private Texture pixel;

    public SettingsScene(SceneManager sceneManager, GameContextManager context) {
        this.sceneManager = sceneManager;
        this.context      = context;
        this.batch        = new SpriteBatch();
        this.camera       = new OrthographicCamera();
        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();
    }

    @Override
    public void onEnter() {
        bgTexture = context.getAssetManager().get("ui/settings_background.png", Texture.class);
        back1     = context.getAssetManager().get("buttons/A_Back1.png",        Texture.class);
        back2     = context.getAssetManager().get("buttons/A_Back2.png",        Texture.class);
        brightnessDown1 = context.getAssetManager().get("buttons/brightness_down1.png", Texture.class);
        brightnessDown2 = context.getAssetManager().get("buttons/brightness_down2.png", Texture.class);
        brightnessUp1   = context.getAssetManager().get("buttons/brightness_up1.png",   Texture.class);
        brightnessUp2   = context.getAssetManager().get("buttons/brightness_up2.png",   Texture.class);
        volumeDown1     = context.getAssetManager().get("buttons/volume_down1.png",     Texture.class);
        volumeDown2     = context.getAssetManager().get("buttons/volume_down2.png",     Texture.class);
        volumeUp1       = context.getAssetManager().get("buttons/volume_up1.png",       Texture.class);
        volumeUp2       = context.getAssetManager().get("buttons/volume_up2.png",       Texture.class);

        if (pixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(1f, 1f, 1f, 1f);
            pixmap.fill();
            pixel = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    @Override
    public void update(float delta) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float scale   = screenH / 1080f;
        float controlSize = CONTROL_BUTTON_BASE_SIZE * scale;
        float controlGap  = screenW * CONTROL_BUTTON_GAP_RATIO;
        float controlYOne = screenH * 0.50f;
        float controlYTwo = controlYOne - (screenH * CONTROL_ROW_GAP_RATIO);
        float leftX       = (screenW - ((controlSize * 2f) + controlGap)) / 2f;
        float rightX      = leftX + controlSize + controlGap;
        float btnW    = BUTTON_BASE_WIDTH * scale;
        float btnH    = BUTTON_BASE_HEIGHT * scale;
        float btnX    = (screenW - btnW) / 2f;
        float btnY    = screenH * 0.12f;
        GameSettings settings = context.getGameSettings();

        if (isButtonClicked(leftX, controlYOne, controlSize, controlSize, screenH)) {
            settings.decreaseBrightness(SETTING_STEP);
        } else if (isButtonClicked(rightX, controlYOne, controlSize, controlSize, screenH)) {
            settings.increaseBrightness(SETTING_STEP);
        } else if (isButtonClicked(leftX, controlYTwo, controlSize, controlSize, screenH)) {
            settings.decreaseVolume(SETTING_STEP);
            context.getSoundManager().setMasterVolume(settings.getMasterVolume());
        } else if (isButtonClicked(rightX, controlYTwo, controlSize, controlSize, screenH)) {
            settings.increaseVolume(SETTING_STEP);
            context.getSoundManager().setMasterVolume(settings.getMasterVolume());
        } else if (isButtonClicked(btnX, btnY, btnW, btnH, screenH)) {
            sceneManager.switchTo(GameSceneId.MENU.id());
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

        if (bgTexture != null) {
            batch.draw(bgTexture, 0, 0, screenW, screenH);
        }

        float scale = screenH / 1080f;
        float controlSize = CONTROL_BUTTON_BASE_SIZE * scale;
        float controlGap  = screenW * CONTROL_BUTTON_GAP_RATIO;
        float controlYOne = screenH * 0.50f;
        float controlYTwo = controlYOne - (screenH * CONTROL_ROW_GAP_RATIO);
        float leftX       = (screenW - ((controlSize * 2f) + controlGap)) / 2f;
        float rightX      = leftX + controlSize + controlGap;
        float centerX     = screenW / 2f;
        float btnW  = BUTTON_BASE_WIDTH * scale;
        float btnH  = BUTTON_BASE_HEIGHT * scale;
        float btnX  = (screenW - btnW) / 2f;
        float btnY  = screenH * 0.12f;
        float barW  = BAR_WIDTH_BASE * scale;
        float barH  = BAR_HEIGHT_BASE * scale;
        float barX  = centerX - (barW / 2f);
        float brightnessBarY = controlYOne + (controlSize * 0.5f) - (barH * 0.5f);
        float volumeBarY     = controlYTwo + (controlSize * 0.5f) - (barH * 0.5f);

        drawButton(brightnessDown1, brightnessDown2, leftX, controlYOne, controlSize, controlSize, screenH);
        drawButton(brightnessUp1, brightnessUp2, rightX, controlYOne, controlSize, controlSize, screenH);
        drawButton(volumeDown1, volumeDown2, leftX, controlYTwo, controlSize, controlSize, screenH);
        drawButton(volumeUp1, volumeUp2, rightX, controlYTwo, controlSize, controlSize, screenH);
        drawProgressBar(barX, brightnessBarY, barW, barH, getBrightnessProgress());
        drawProgressBar(barX, volumeBarY, barW, barH, getVolumeProgress());

        boolean hovered   = isHovered(btnX, btnY, btnW, btnH, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture tex = isPressed ? back2 : back1;
        if (tex != null) batch.draw(tex, btnX, btnY, btnW, btnH);

        batch.end();

        batch.begin();
        brightnessOverlayRenderer.render(batch, context.getGameSettings());
        batch.end();
    }

    @Override
    public void onExit() {}

    @Override
    public void disposeResources() {
        if (batch != null) batch.dispose();
        if (pixel != null) pixel.dispose();
        brightnessOverlayRenderer.dispose();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    
    private void drawButton(Texture normal, Texture pressed,
                            float bx, float by, float bw, float bh, float screenH) {
        boolean hovered   = isHovered(bx, by, bw, bh, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture tex = isPressed ? pressed : normal;
        if (tex != null) batch.draw(tex, bx, by, bw, bh);
    }

    private float getBrightnessProgress() {
        float min = 0.4f;
        float max = 1.6f;
        float value = context.getGameSettings().getBrightness();
        return (value - min) / (max - min);
    }

    private float getVolumeProgress() {
        return context.getGameSettings().getMasterVolume();
    }

    private void drawProgressBar(float x, float y, float width, float height, float progress) {
        progress = Math.max(0f, Math.min(1f, progress));

        batch.setColor(0f, 0f, 0f, 0.45f);
        batch.draw(pixel, x, y, width, height);

        batch.setColor(1f, 0.82f, 0.25f, 0.95f);
        batch.draw(pixel, x, y, width * progress, height);

        batch.setColor(1f, 0.95f, 0.75f, 0.9f);
        batch.draw(pixel, x, y + height - 2f, width, 2f);

        batch.setColor(0.35f, 0.12f, 0.12f, 0.9f);
        batch.draw(pixel, x, y, width, 2f);

        batch.setColor(1f, 1f, 1f, 1f);
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
