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
import inf1009.p63.flappyearth.game.state.GameSession;

public class MenuScene extends Scene {

    private final SceneManager      sceneManager;
    private final GameContextManager context;
    private final GameSession gameSession;
    private final StagePlan stagePlan;

    private final SpriteBatch batch;
    private final BitmapFont  titleFont;
    private final BitmapFont  subFont;
    private final GlyphLayout layout;
    private final OrthographicCamera camera;

    public MenuScene(SceneManager sceneManager,
                     GameContextManager context,
                     GameSession gameSession,
                     StagePlan stagePlan) {
        this.sceneManager = sceneManager;
        this.context      = context;
        this.gameSession  = gameSession;
        this.stagePlan    = stagePlan;

        this.batch     = new SpriteBatch();
        this.titleFont = new BitmapFont();
        this.subFont   = new BitmapFont();
        this.layout    = new GlyphLayout();
        this.camera    = new OrthographicCamera();
        this.titleFont.getData().setScale(3f);
        this.subFont.getData().setScale(1.5f);
    }

    @Override
    public void onEnter() {}

    @Override
    public void update(float delta) {
        if (context.getInputOutputManager().isActionJustPressed(GameInputAction.FLAP.id())) {
            gameSession.resetForNewRun();
            sceneManager.switchTo(stagePlan.getInitialStageId());
        }
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenW, screenH);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.5f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        layout.setText(titleFont, "FlappyEarth");
        titleFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.75f);

        layout.setText(subFont, "Press SPACE or tap to deploy");
        subFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.55f);

        layout.setText(subFont, "Recover the skies. Rebuild the world.");
        subFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.48f);

        batch.end();
    }

    @Override
    public void onExit() {}

    @Override
    public void disposeResources() {
        if (batch     != null) batch.dispose();
        if (titleFont != null) titleFont.dispose();
        if (subFont   != null) subFont.dispose();
    }
}
