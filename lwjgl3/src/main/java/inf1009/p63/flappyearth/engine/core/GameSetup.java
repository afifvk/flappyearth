package inf1009.p63.flappyearth.engine.core;

public interface GameSetup {

    void configure(GameContextManager contextManager, SceneManager sceneManager);

    String getInitialSceneId();
}
