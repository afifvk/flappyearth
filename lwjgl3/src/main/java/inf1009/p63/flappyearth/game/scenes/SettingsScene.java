package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;

public class SettingsScene extends Scene {

    private static final float BUTTON_BASE_WIDTH = 320f;
    private static final float BUTTON_BASE_HEIGHT = 90f;

    private final SceneManager       sceneManager;
    private final GameContextManager context;

    private final SpriteBatch        batch;
    private final OrthographicCamera camera;

    private Texture bgTexture;
    private Texture back1, back2;

    public SettingsScene(SceneManager sceneManager, GameContextManager context) {
        this.sceneManager = sceneManager;
        this.context      = context;
        this.batch        = new SpriteBatch();
        this.camera       = new OrthographicCamera();
    }

    @Override
    public void onEnter() {
        bgTexture = context.getAssetManager().get("ui/settings_background.png", Texture.class);
        back1     = context.getAssetManager().get("buttons/A_Back1.png",        Texture.class);
        back2     = context.getAssetManager().get("buttons/A_Back2.png",        Texture.class);
    }

    @Override
    public void update(float delta) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float scale   = screenH / 1080f;
        float btnW    = BUTTON_BASE_WIDTH * scale;
        float btnH    = BUTTON_BASE_HEIGHT * scale;
        float btnX    = (screenW - btnW) / 2f;
        float btnY    = screenH * 0.12f;

        if (isButtonClicked(btnX, btnY, btnW, btnH, screenH)) {
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
        float btnW  = BUTTON_BASE_WIDTH * scale;
        float btnH  = BUTTON_BASE_HEIGHT * scale;
        float btnX  = (screenW - btnW) / 2f;
        float btnY  = screenH * 0.12f;

        boolean hovered   = isHovered(btnX, btnY, btnW, btnH, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture tex = isPressed ? back2 : back1;
        if (tex != null) batch.draw(tex, btnX, btnY, btnW, btnH);

        batch.end();
    }

    @Override
    public void onExit() {}

    @Override
    public void disposeResources() {
        if (batch != null) batch.dispose();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /** LibGDX Y-origin for getY() is at the top; flip to bottom-origin for hit testing. */
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
