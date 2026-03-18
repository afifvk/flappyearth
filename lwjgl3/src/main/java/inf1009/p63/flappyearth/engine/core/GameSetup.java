package inf1009.p63.flappyearth.engine.core;

public interface GameSetup {

    void configure(EngineContext contextManager, SceneManager sceneManager);

    String getInitialSceneId();
}
