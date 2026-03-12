package inf1009.p63.flappyearth.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.Scene;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.input.GameInputAction;

public class GameOverScene extends Scene {

    private final SceneManager      sceneManager;
    private final GameContextManager context;
    private int                     finalScore;

    private final SpriteBatch batch;
    private final BitmapFont  headingFont;
    private final BitmapFont  bodyFont;
    private final GlyphLayout layout;
    private final OrthographicCamera camera;

    public GameOverScene(SceneManager sceneManager, GameContextManager context) {
        this.sceneManager = sceneManager;
        this.context      = context;
        this.finalScore   = 0;

        this.batch       = new SpriteBatch();
        this.headingFont = new BitmapFont();
        this.bodyFont    = new BitmapFont();
        this.layout      = new GlyphLayout();
        this.camera      = new OrthographicCamera();
        this.headingFont.getData().setScale(3f);
        this.bodyFont.getData().setScale(1.5f);
    }

    public void setScore(int score) {
        this.finalScore = score;
    }

    @Override
    public void onEnter() {
        context.getSoundManager().stopMusic();
        context.getSoundManager().playGameOver();
    }

    @Override
    public void update(float delta) {
        if (context.getInputOutputManager().isActionJustPressed(GameInputAction.FLAP.id())) {
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

        layout.setText(headingFont, "GAME OVER");
        headingFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.75f);

        layout.setText(bodyFont, "Score: " + finalScore);
        bodyFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.55f);

        layout.setText(bodyFont, "Tap or SPACE to return to menu");
        bodyFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.42f);

        batch.end();
    }

    @Override
    public void onExit() {}

    @Override
    public void disposeResources() {
        if (batch       != null) batch.dispose();
        if (headingFont != null) headingFont.dispose();
        if (bodyFont    != null) bodyFont.dispose();
    }
}
