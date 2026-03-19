package inf1009.p63.flappyearth.engine.core;

import inf1009.p63.flappyearth.engine.config.EngineSettings;
import inf1009.p63.flappyearth.engine.services.AssetManager;
import inf1009.p63.flappyearth.engine.services.CollisionManager;
import inf1009.p63.flappyearth.engine.services.EventBus;
import inf1009.p63.flappyearth.engine.services.InputManager;
import inf1009.p63.flappyearth.engine.services.MovementManager;
import inf1009.p63.flappyearth.engine.services.RandomManager;
import inf1009.p63.flappyearth.engine.services.AudioManager;
import inf1009.p63.flappyearth.engine.services.TimeManager;

public class EngineContext {

    private final EngineSettings engineSettings = new EngineSettings();
    private AssetManager assetManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private AudioManager audioManager;  
    private EventBus eventBus;
    private TimeManager timeManager;
    private RandomManager randomManager;

    public void init() {
        eventBus       = new EventBus();
        assetManager       = new AssetManager();
        inputManager = new InputManager();
        movementManager    = new MovementManager();
        collisionManager   = new CollisionManager();
        audioManager       = new AudioManager();
        timeManager        = new TimeManager();
        randomManager      = new RandomManager();
    }

    public void dispose() {
        if (assetManager       != null) assetManager.dispose();
        if (audioManager       != null) audioManager.dispose();
        if (eventBus       != null) eventBus.clearAll();
    }

    public AssetManager getAssetManager()             { return assetManager; }
    public InputManager getInputManager() { return inputManager; }
    public MovementManager getMovementManager()       { return movementManager; }
    public CollisionManager getCollisionManager()     { return collisionManager; }
    public AudioManager getAudioManager()             { return audioManager; }
    public EventBus getEventBus()             { return eventBus; }
    public TimeManager getTimeManager()               { return timeManager; }
    public RandomManager getRandomManager()           { return randomManager; }
    public float getBrightness()                      { return engineSettings.getBrightness(); }
    public void increaseBrightness(float amount)      { engineSettings.increaseBrightness(amount); }
    public void decreaseBrightness(float amount)      { engineSettings.decreaseBrightness(amount); }
    public float getMasterVolume()                    { return engineSettings.getMasterVolume(); }
    public void increaseMasterVolume(float amount)    { engineSettings.increaseVolume(amount); }
    public void decreaseMasterVolume(float amount)    { engineSettings.decreaseVolume(amount); }
    public void setMasterVolume(float value)          {
        engineSettings.setMasterVolume(value);
        if (audioManager != null) {
            audioManager.setMasterVolume(engineSettings.getMasterVolume());
        }
    }
    public boolean isMuted()                          { return engineSettings.isMuted(); }
    public void setMuted(boolean muted)               {
        engineSettings.setMuted(muted);
        if (audioManager != null) {
            audioManager.setMuted(muted);
        }
    }
    public EngineSettings getEngineSettings()         { return engineSettings; }
}
