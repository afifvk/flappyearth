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
import inf1009.p63.flappyearth.game.config.AssetKeys;
import inf1009.p63.flappyearth.game.managers.BrightnessOverlayRenderer;
import inf1009.p63.flappyearth.game.state.GameSession;

public class GameOverScene extends Scene {

    private static final float BUTTON_BASE_WIDTH = 320f;
    private static final float BUTTON_BASE_HEIGHT = 90f;
    private static final float RESTART_Y_RATIO = 0.24f;
    private static final float QUIT_Y_RATIO = 0.13f;

    private final SceneManager sceneManager;
    private final GameContextManager context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;
    private int finalScore;
    private boolean victoryEnding;

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BrightnessOverlayRenderer brightnessOverlayRenderer;

    private Texture backgroundTexture;
    private Texture restart1;
    private Texture restart2;
    private Texture quit1;
    private Texture quit2;

    public GameOverScene(SceneManager sceneManager,
                         GameContextManager context,
                         GameSession gameSession,
                         StagePlan stagePlan) {
        this.sceneManager = sceneManager;
        this.context = context;
        this.gameSession = gameSession;
        this.stagePlan = stagePlan;
        this.finalScore = 0;
        this.victoryEnding = false;

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.brightnessOverlayRenderer = new BrightnessOverlayRenderer();
    }

    public void setScore(int score) {
        this.finalScore = score;
    }

    public void setVictoryEnding(boolean victoryEnding) {
        this.victoryEnding = victoryEnding;
    }

    @Override
    public void onEnter() {
        context.getSoundManager().stopMusic();
        context.getSoundManager().playGameOver();

        backgroundTexture = context.getAssetManager().get(
            victoryEnding ? AssetKeys.ENDGAME_BG : AssetKeys.GAMEFAILED_BG,
            Texture.class);
        restart1 = context.getAssetManager().get("buttons/A_Restart1.png", Texture.class);
        restart2 = context.getAssetManager().get("buttons/A_Restart2.png", Texture.class);
        quit1 = context.getAssetManager().get("buttons/A_Quit1.png", Texture.class);
        quit2 = context.getAssetManager().get("buttons/A_Quit2.png", Texture.class);
    }

    @Override
    public void update(float delta) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || isRestartButtonClicked(screenW, screenH)) {
            context.getSoundManager().playButtonClick();
            gameSession.resetForNewRun();
            gameSession.prepareForStageEntry();
            sceneManager.switchTo(stagePlan.getInitialStageId());
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || isQuitButtonClicked(screenW, screenH)) {
            context.getSoundManager().playButtonClick();
            gameSession.resetForNewRun();
            sceneManager.switchTo(GameSceneId.MENU.id());
        }
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenW, screenH);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.15f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0f, 0f, screenW, screenH);
        }

        float scale = screenH / 1080f;
        float buttonW = BUTTON_BASE_WIDTH * scale;
        float buttonH = BUTTON_BASE_HEIGHT * scale;
        float buttonX = (screenW - buttonW) / 2f;

        drawButton(restart1, restart2, buttonX, screenH * RESTART_Y_RATIO, buttonW, buttonH, screenH);
        drawButton(quit1, quit2, buttonX, screenH * QUIT_Y_RATIO, buttonW, buttonH, screenH);

        batch.end();

        batch.begin();
        brightnessOverlayRenderer.render(batch, context.getBrightness());
        batch.end();
    }

    @Override
    public void onExit() {
        victoryEnding = false;
    }

    @Override
    public void disposeResources() {
        if (batch       != null) batch.dispose();
        brightnessOverlayRenderer.dispose();
    }

    private void drawButton(Texture normal, Texture pressed,
                            float x, float y, float width, float height, float screenH) {
        boolean hovered = isHovered(x, y, width, height, screenH);
        boolean isPressed = hovered && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Texture texture = isPressed ? pressed : normal;
        if (texture != null) {
            batch.draw(texture, x, y, width, height);
        }
    }

    private boolean isRestartButtonClicked(float screenW, float screenH) {
        float scale = screenH / 1080f;
        float buttonW = BUTTON_BASE_WIDTH * scale;
        float buttonH = BUTTON_BASE_HEIGHT * scale;
        float buttonX = (screenW - buttonW) / 2f;
        float buttonY = screenH * RESTART_Y_RATIO;
        return isButtonClicked(buttonX, buttonY, buttonW, buttonH, screenH);
    }

    private boolean isQuitButtonClicked(float screenW, float screenH) {
        float scale = screenH / 1080f;
        float buttonW = BUTTON_BASE_WIDTH * scale;
        float buttonH = BUTTON_BASE_HEIGHT * scale;
        float buttonX = (screenW - buttonW) / 2f;
        float buttonY = screenH * QUIT_Y_RATIO;
        return isButtonClicked(buttonX, buttonY, buttonW, buttonH, screenH);
    }

    private boolean isHovered(float x, float y, float width, float height, float screenH) {
        float mouseX = Gdx.input.getX();
        float mouseY = screenH - Gdx.input.getY();
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private boolean isButtonClicked(float x, float y, float width, float height, float screenH) {
        return isHovered(x, y, width, height, screenH)
                && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }
}
