package inf1009.p63.flappyearth.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PauseOverlayController {

    public enum Action {
        NONE,
        RESUME,
        HELP,
        RESTART,
        QUIT
    }

    private final float buttonBaseWidth;
    private final float buttonBaseHeight;

    private final OrthographicCamera camera;

    private final Texture pauseBgTex;
    private final Texture pauseResume1;
    private final Texture pauseResume2;
    private final Texture pauseHelp1;
    private final Texture pauseHelp2;
    private final Texture pauseRestart1;
    private final Texture pauseRestart2;
    private final Texture pauseQuit1;
    private final Texture pauseQuit2;

    public PauseOverlayController(float buttonBaseWidth,
                                  float buttonBaseHeight,
                                  Texture pauseBgTex,
                                  Texture pauseResume1,
                                  Texture pauseResume2,
                                  Texture pauseHelp1,
                                  Texture pauseHelp2,
                                  Texture pauseRestart1,
                                  Texture pauseRestart2,
                                  Texture pauseQuit1,
                                  Texture pauseQuit2) {
        this.buttonBaseWidth = buttonBaseWidth;
        this.buttonBaseHeight = buttonBaseHeight;
        this.pauseBgTex = pauseBgTex;
        this.pauseResume1 = pauseResume1;
        this.pauseResume2 = pauseResume2;
        this.pauseHelp1 = pauseHelp1;
        this.pauseHelp2 = pauseHelp2;
        this.pauseRestart1 = pauseRestart1;
        this.pauseRestart2 = pauseRestart2;
        this.pauseQuit1 = pauseQuit1;
        this.pauseQuit2 = pauseQuit2;
        this.camera = new OrthographicCamera();
    }

    public Action handleInput(float screenW, float screenH) {
        float scale   = screenH / 1080f;
        float popupW  = 500f * scale;
        float popupH  = 620f * scale;
        float popupX  = (screenW - popupW) / 2f;
        float popupY  = (screenH - popupH) / 2f;
        float btnW    = buttonBaseWidth  * scale;
        float btnH    = buttonBaseHeight * scale;
        float btnX    = popupX + (popupW - btnW) / 2f;

        float resumeY  = popupY + popupH * 0.61f;
        float helpY    = popupY + popupH * 0.45f;
        float restartY = popupY + popupH * 0.29f;
        float quitY    = popupY + popupH * 0.13f;

        if (isButtonClicked(btnX, resumeY, btnW, btnH, screenH)) return Action.RESUME;
        if (isButtonClicked(btnX, helpY, btnW, btnH, screenH)) return Action.HELP;
        if (isButtonClicked(btnX, restartY, btnW, btnH, screenH)) return Action.RESTART;
        if (isButtonClicked(btnX, quitY, btnW, btnH, screenH)) return Action.QUIT;
        return Action.NONE;
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch, float screenW, float screenH) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.6f);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.end();

        camera.setToOrtho(false, screenW, screenH);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float scale  = screenH / 1080f;
        float popupW = 500f * scale;
        float popupH = 620f * scale;
        float popupX = (screenW - popupW) / 2f;
        float popupY = (screenH - popupH) / 2f;

        if (pauseBgTex != null) batch.draw(pauseBgTex, popupX, popupY, popupW, popupH);

        float btnW   = buttonBaseWidth  * scale;
        float btnH   = buttonBaseHeight * scale;
        float btnX   = popupX + (popupW - btnW) / 2f;

        drawButton(batch, pauseResume1,  pauseResume2,  btnX, popupY + popupH * 0.61f, btnW, btnH, screenH);
        drawButton(batch, pauseHelp1,    pauseHelp2,    btnX, popupY + popupH * 0.45f, btnW, btnH, screenH);
        drawButton(batch, pauseRestart1, pauseRestart2, btnX, popupY + popupH * 0.29f, btnW, btnH, screenH);
        drawButton(batch, pauseQuit1,    pauseQuit2,    btnX, popupY + popupH * 0.13f, btnW, btnH, screenH);

        batch.end();
    }

    private void drawButton(SpriteBatch batch,
                            Texture normal,
                            Texture pressed,
                            float bx,
                            float by,
                            float bw,
                            float bh,
                            float screenH) {
        boolean hovered = isHovered(bx, by, bw, bh, screenH);
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
