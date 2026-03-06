package inf1009.p63.flappyearth.engine.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class GameMaster extends ApplicationAdapter {

    private SceneManager sceneManager;
    private GameContextManager contextManager;
    private final GameSetup gameSetup;

    public GameMaster(GameSetup gameSetup) {
        if (gameSetup == null) {
            throw new IllegalArgumentException("GameSetup cannot be null");
        }
        this.gameSetup = gameSetup;
    }

    @Override
    public void create() {
        sceneManager = new SceneManager();
        contextManager = new GameContextManager();
        contextManager.init();

        gameSetup.configure(contextManager, sceneManager);
        sceneManager.switchTo(gameSetup.getInitialSceneId());
    }

    @Override
    public void render() {
        // Update and render current scene each frame
        float delta = Gdx.graphics.getDeltaTime();
        sceneManager.update(delta);
        sceneManager.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        contextManager.dispose();
    }
}
