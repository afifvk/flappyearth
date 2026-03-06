package inf1009.p63.flappyearth.engine.core;

public abstract class Scene {

    public Scene() {
    }

    public abstract void onEnter();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void onExit();

    // Subclasses override if they have resources to dispose
    public void disposeResources() {}
}
