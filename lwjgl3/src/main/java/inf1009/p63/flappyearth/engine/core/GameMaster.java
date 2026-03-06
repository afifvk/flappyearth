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
        // Original setup logic
        sceneManager = new SceneManager();
        contextManager = new GameContextManager();
        contextManager.init();

        gameSetup.configure(contextManager, sceneManager);
        sceneManager.switchTo(gameSetup.getInitialSceneId());
    }

    @Override
    public void render() {
        // The sceneManager handles all rendering for the current scene
        float delta = Gdx.graphics.getDeltaTime();
        sceneManager.update(delta);
        sceneManager.render();
    }

    @Override
    public void dispose() {
        if (sceneManager != null) sceneManager.dispose();
        if (contextManager != null) contextManager.dispose();
    }
}