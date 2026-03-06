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

public class MenuScene extends Scene {

    private final SceneManager      sceneManager;
    private final GameContextManager context;

    private final SpriteBatch batch;
    private final BitmapFont  titleFont;
    private final BitmapFont  subFont;
    private final GlyphLayout layout;
    private final OrthographicCamera camera;

    public MenuScene(SceneManager sceneManager, GameContextManager context) {
        this.sceneManager = sceneManager;
        this.context      = context;
        
        // Create resources once in constructor (scene is reused)
        this.batch     = new SpriteBatch();
        this.titleFont = new BitmapFont();
        this.subFont   = new BitmapFont();
        this.layout    = new GlyphLayout();
        this.camera    = new OrthographicCamera();
        this.titleFont.getData().setScale(3f);
        this.subFont.getData().setScale(1.5f);
    }

    @Override
    public void onEnter() {
        // Scene is reused, resources already created in constructor
    }

    @Override
    public void update(float delta) {
        // Start game on player input - use type-safe scene switching
        if (context.getInputOutputManager().isActionJustPressed(GameInputAction.FLAP.id())) {
            sceneManager.switchTo(GameSceneId.GAME.id());
        }
    }

    @Override
    public void render() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // Setup camera for proper rendering at any resolution
        camera.setToOrtho(false, screenW, screenH);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.5f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        layout.setText(titleFont, "FlappyEarth");
        titleFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.75f);

        layout.setText(subFont, "Tap or SPACE to Play");
        subFont.draw(batch, layout,
                (screenW - layout.width) / 2f,
                screenH * 0.55f);

        layout.setText(subFont, "Fly through a world worth saving!");
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
