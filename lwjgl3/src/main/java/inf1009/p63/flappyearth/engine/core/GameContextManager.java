package inf1009.p63.flappyearth.engine.core;

import inf1009.p63.flappyearth.engine.managers.AssetManager;
import inf1009.p63.flappyearth.engine.managers.CollisionManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.engine.managers.InputOutputManager;
import inf1009.p63.flappyearth.engine.managers.MovementManager;
import inf1009.p63.flappyearth.engine.managers.RandomManager;
import inf1009.p63.flappyearth.engine.managers.SoundManager;
import inf1009.p63.flappyearth.engine.managers.TimeManager;
import inf1009.p63.flappyearth.game.config.GameSettings;

public class GameContextManager {

    private final GameSettings gameSettings = new GameSettings();
    private AssetManager assetManager;
    private InputOutputManager inputOutputManager;
    private MovementManager movementManager;
    private CollisionManager collisionManager;
    private SoundManager soundManager;  
    private EventManager eventManager;
    private TimeManager timeManager;
    private RandomManager randomManager;

    public void init() {
        eventManager       = new EventManager();
        assetManager       = new AssetManager();
        inputOutputManager = new InputOutputManager();
        movementManager    = new MovementManager();
        collisionManager   = new CollisionManager();
        soundManager       = new SoundManager();
        timeManager        = new TimeManager();
        randomManager      = new RandomManager();
    }

    public void dispose() {
        if (assetManager       != null) assetManager.dispose();
        if (soundManager       != null) soundManager.dispose();
        if (eventManager       != null) eventManager.clearAll();
    }

    public AssetManager getAssetManager()             { return assetManager; }
    public InputOutputManager getInputOutputManager() { return inputOutputManager; }
    public MovementManager getMovementManager()       { return movementManager; }
    public CollisionManager getCollisionManager()     { return collisionManager; }
    public SoundManager getSoundManager()             { return soundManager; }
    public EventManager getEventManager()             { return eventManager; }
    public TimeManager getTimeManager()               { return timeManager; }
    public RandomManager getRandomManager()           { return randomManager; }
    public float getBrightness()                      { return gameSettings.getBrightness(); }
    public void increaseBrightness(float amount)      { gameSettings.increaseBrightness(amount); }
    public void decreaseBrightness(float amount)      { gameSettings.decreaseBrightness(amount); }
    public float getMasterVolume()                    { return gameSettings.getMasterVolume(); }
    public void increaseMasterVolume(float amount)    { gameSettings.increaseVolume(amount); }
    public void decreaseMasterVolume(float amount)    { gameSettings.decreaseVolume(amount); }
    public GameSettings getGameSettings()             { return gameSettings; }
}
