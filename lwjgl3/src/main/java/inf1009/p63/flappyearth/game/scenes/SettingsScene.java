package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.runtime.BrightnessOverlayRenderer;

public class SettingsScene extends Scene {

    private static final float BUTTON_BASE_WIDTH = 420f;
    private static final float BUTTON_BASE_HEIGHT = 118f;
    private static final float CONTROL_BUTTON_BASE_SIZE = 150f;
    private static final float CONTROL_BUTTON_GAP = 440f;
    private static final float CONTROL_ROW_GAP = 210f;
    private static final float SETTING_STEP = 0.1f;
    private static final float BAR_WIDTH_BASE = 500f;
    private static final float BAR_HEIGHT_BASE = 24f;
    private static final float PANEL_BASE_HEIGHT = 760f;
    private static final float CHECKBOX_BASE_SIZE = 56f;

    private final SceneManager sceneManager;
    private final EngineContext context;

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;

    private Texture bgTexture;
    private Texture back1;
    private Texture back2;
    private Texture brightnessDown1;
    private Texture brightnessDown2;
    private Texture brightnessUp1;
    private Texture brightnessUp2;
    private Texture volumeDown1;
    private Texture volumeDown2;
    private Texture volumeUp1;
    private Texture volumeUp2;
    private Texture muteIcon;
    private Texture pixel;

    private float pendingBrightness;
    private float pendingVolume;
    private boolean pendingMuted;

    public SettingsScene(SceneManager sceneManager, EngineContext context) {
        this.sceneManager = sceneManager;
        this.context = context;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();
    }

    @Override
    public void onEnter() {
        bgTexture = context.getAssetManager().get("textures/backgrounds/settings.png", Texture.class);
        back1 = context.getAssetManager().get("textures/ui/buttons/back_1.png", Texture.class);
        back2 = context.getAssetManager().get("textures/ui/buttons/back_2.png", Texture.class);
        brightnessDown1 = context.getAssetManager().get("textures/ui/sliders/brightness_down_1.png", Texture.class);
        brightnessDown2 = context.getAssetManager().get("textures/ui/sliders/brightness_down_2.png", Texture.class);
        brightnessUp1 = context.getAssetManager().get("textures/ui/sliders/brightness_up_1.png", Texture.class);
        brightnessUp2 = context.getAssetManager().get("textures/ui/sliders/brightness_up_2.png", Texture.class);
        volumeDown1 = context.getAssetManager().get("textures/ui/sliders/volume_down_1.png", Texture.class);
        volumeDown2 = context.getAssetManager().get("textures/ui/sliders/volume_down_2.png", Texture.class);
        volumeUp1 = context.getAssetManager().get("textures/ui/sliders/volume_up_1.png", Texture.class);
        volumeUp2 = context.getAssetManager().get("textures/ui/sliders/volume_up_2.png", Texture.class);
        muteIcon = context.getAssetManager().get("textures/ui/misc/mute.png", Texture.class);

        pendingBrightness = context.getBrightness();
        pendingVolume = context.getMasterVolume();
        pendingMuted = context.isMuted();

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
        float scale = Math.min(screenW / 1920f, screenH / 1080f);

        float panelH = PANEL_BASE_HEIGHT * scale;
        float panelY = (screenH - panelH) * 0.5f;

        float centerX = screenW * 0.5f;
        float controlSize = CONTROL_BUTTON_BASE_SIZE * scale;
        float controlGap = CONTROL_BUTTON_GAP * scale;
        float controlYOne = panelY + panelH * 0.70f;
        float controlYTwo = controlYOne - (CONTROL_ROW_GAP * scale);
        float leftX = centerX - (controlGap * 0.5f) - controlSize;
        float rightX = centerX + (controlGap * 0.5f);

        float btnW = BUTTON_BASE_WIDTH * scale;
        float btnH = BUTTON_BASE_HEIGHT * scale;
        float btnX = centerX - (btnW * 0.5f);
        float btnY = panelY + panelH * 0.06f;

        float checkboxSize = CHECKBOX_BASE_SIZE * scale;
        float barX = centerX - (barWidth(scale) * 0.5f);
        float checkboxX = barX + (24f * scale);
        float checkboxY = controlYTwo - (130f * scale);

        if (isButtonClicked(leftX, controlYOne, controlSize, controlSize, screenH)) {
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            pendingBrightness = clamp(pendingBrightness - SETTING_STEP, 0.4f, 1.6f);
        } else if (isButtonClicked(rightX, controlYOne, controlSize, controlSize, screenH)) {
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            pendingBrightness = clamp(pendingBrightness + SETTING_STEP, 0.4f, 1.6f);
        } else if (isButtonClicked(leftX, controlYTwo, controlSize, controlSize, screenH)) {
            pendingVolume = clamp(pendingVolume - SETTING_STEP, 0f, 1f);
            pendingMuted = false;
            applyPendingAudioSettings();
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
        } else if (isButtonClicked(rightX, controlYTwo, controlSize, controlSize, screenH)) {
            pendingVolume = clamp(pendingVolume + SETTING_STEP, 0f, 1f);
            pendingMuted = false;
            applyPendingAudioSettings();
            context.getAudioManager().playSound(AudioKeys.UI_CLICK);
        } else if (isButtonClicked(checkboxX, checkboxY, checkboxSize, checkboxSize, screenH)) {
            pendingMuted = !pendingMuted;
            applyPendingAudioSettings();
            if (!pendingMuted) {
                context.getAudioManager().playSound(AudioKeys.UI_CLICK);
            }
        } else if (isButtonClicked(btnX, btnY, btnW, btnH, screenH)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            saveAndExit();
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

        float scale = Math.min(screenW / 1920f, screenH / 1080f);
        float panelH = PANEL_BASE_HEIGHT * scale;
        float panelY = (screenH - panelH) * 0.5f;

        float centerX = screenW * 0.5f;
        float controlSize = CONTROL_BUTTON_BASE_SIZE * scale;
        float controlGap = CONTROL_BUTTON_GAP * scale;
        float controlYOne = panelY + panelH * 0.70f;
        float controlYTwo = controlYOne - (CONTROL_ROW_GAP * scale);
        float leftX = centerX - (controlGap * 0.5f) - controlSize;
        float rightX = centerX + (controlGap * 0.5f);

        float barW = barWidth(scale);
        float barH = BAR_HEIGHT_BASE * scale;
        float barX = centerX - (barW * 0.5f);
        float brightnessBarY = controlYOne + (controlSize * 0.5f) - (barH * 0.5f);
        float volumeBarY = controlYTwo + (controlSize * 0.5f) - (barH * 0.5f);

        float checkboxSize = CHECKBOX_BASE_SIZE * scale;
        float checkboxX = barX + (24f * scale);
        float checkboxY = controlYTwo - (130f * scale);
        float iconSize = checkboxSize * 1.4f;
        float iconX = checkboxX + checkboxSize + (16f * scale);
        float iconY = checkboxY - (iconSize - checkboxSize) * 0.5f;

        float btnW = BUTTON_BASE_WIDTH * scale;
        float btnH = BUTTON_BASE_HEIGHT * scale;
        float btnX = centerX - (btnW * 0.5f);
        float btnY = panelY + panelH * 0.06f;

        drawButton(brightnessDown1, brightnessDown2, leftX, controlYOne, controlSize, controlSize, screenH);
        drawButton(brightnessUp1, brightnessUp2, rightX, controlYOne, controlSize, controlSize, screenH);
        drawButton(volumeDown1, volumeDown2, leftX, controlYTwo, controlSize, controlSize, screenH);
        drawButton(volumeUp1, volumeUp2, rightX, controlYTwo, controlSize, controlSize, screenH);

        drawProgressBar(barX, brightnessBarY, barW, barH, getBrightnessProgress());
        drawProgressBar(barX, volumeBarY, barW, barH, getVolumeProgress());

        drawMuteCheckbox(checkboxX, checkboxY, checkboxSize);
        if (muteIcon != null) {
            batch.setColor(1f, 1f, 1f, pendingMuted ? 1f : 0.65f);
            batch.draw(muteIcon, iconX, iconY, iconSize, iconSize);
            batch.setColor(1f, 1f, 1f, 1f);
        }

        boolean hovered = isHovered(btnX, btnY, btnW, btnH, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture tex = isPressed ? back2 : back1;
        if (tex != null) {
            batch.draw(tex, btnX, btnY, btnW, btnH);
        }

        batch.end();

        batch.begin();
        brightnessOverlayRenderer.render(batch, pendingBrightness);
        batch.end();
    }

    @Override
    public void onExit() {
    }

    @Override
    public void disposeResources() {
        if (batch != null) {
            batch.dispose();
        }
        if (pixel != null) {
            pixel.dispose();
        }
        brightnessOverlayRenderer.dispose();
    }

    private void drawButton(Texture normal, Texture pressed,
                            float bx, float by, float bw, float bh, float screenH) {
        boolean hovered = isHovered(bx, by, bw, bh, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture tex = isPressed ? pressed : normal;
        if (tex != null) {
            batch.draw(tex, bx, by, bw, bh);
        }
    }

    private float getBrightnessProgress() {
        float min = 0.4f;
        float max = 1.6f;
        return (pendingBrightness - min) / (max - min);
    }

    private float getVolumeProgress() {
        return pendingVolume;
    }

    private void drawProgressBar(float x, float y, float width, float height, float progress) {
        progress = clamp(progress, 0f, 1f);

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

    private void drawMuteCheckbox(float x, float y, float size) {
        batch.setColor(0f, 0f, 0f, 0.45f);
        batch.draw(pixel, x, y, size, size);
        batch.setColor(0.96f, 0.95f, 0.86f, 1f);
        batch.draw(pixel, x + 3f, y + 3f, size - 6f, size - 6f);

        if (pendingMuted) {
            batch.setColor(0.16f, 0.63f, 0.34f, 1f);
            batch.draw(pixel, x + size * 0.20f, y + size * 0.40f, size * 0.17f, size * 0.20f);
            batch.draw(pixel, x + size * 0.33f, y + size * 0.27f, size * 0.45f, size * 0.20f);
        }

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

    private float barWidth(float scale) {
        return BAR_WIDTH_BASE * scale;
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private void applyPendingAudioSettings() {
        context.setMasterVolume(pendingVolume);
        context.setMuted(pendingMuted);
    }

    private void saveAndExit() {
        context.getAudioManager().playSound(AudioKeys.UI_CLICK);

        context.getEngineSettings().setBrightness(pendingBrightness);
        applyPendingAudioSettings();

        sceneManager.switchTo(GameSceneId.MENU.id());
    }
}
