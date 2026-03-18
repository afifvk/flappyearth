package inf1009.p63.flappyearth.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HelpOverlayController {

    private final OrthographicCamera camera;
    private final Texture instructionsTexture;

    public HelpOverlayController(Texture instructionsTexture) {
        this.instructionsTexture = instructionsTexture;
        this.camera = new OrthographicCamera();
    }

    public boolean isDismissRequested() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch, float screenW, float screenH) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, screenW, screenH);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.45f);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.end();

        camera.setToOrtho(false, screenW, screenH);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (instructionsTexture != null) {
            float panelW = screenW * 0.88f;
            float panelH = screenH * 0.86f;
            batch.draw(instructionsTexture,
                    (screenW - panelW) * 0.5f,
                    (screenH - panelH) * 0.5f,
                    panelW,
                    panelH);
        }

        batch.end();
    }
}
