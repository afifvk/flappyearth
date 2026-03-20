package inf1009.p63.flappyearth.game.setup;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.GameSetup;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.DisplaySettings;
import inf1009.p63.flappyearth.game.config.DisplaySettingsFactory;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.input.KeyboardInputDevice;
import inf1009.p63.flappyearth.game.input.TouchInputDevice;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.state.GameSession;

public class FlappyEarthSetup implements GameSetup {

    private final DisplaySettings displaySettings;

    public FlappyEarthSetup() {
        this(DisplaySettingsFactory.createFromRuntime());
    }

    public FlappyEarthSetup(DisplaySettings displaySettings) {
        this.displaySettings = displaySettings;
    }

    @Override
    public void configure(EngineContext contextManager, SceneManager sceneManager) {
        contextManager.getInputManager().registerInputDevice(new KeyboardInputDevice());
        contextManager.getInputManager().registerInputDevice(new TouchInputDevice());

        StagePlan stagePlan = GameStagePlanFactory.createDefaultPlan();
        GameplayDimensions dimensions = GameplayDimensions.fromDisplaySettings(displaySettings);
        GameSession gameSession = new GameSession(stagePlan.getFinalTargetGoodCollectibles());

        GameAssetRegistry.loadAll(contextManager);
        GameAudioRegistry.registerAll(contextManager);
        GameSceneRegistry.registerAll(sceneManager, contextManager, gameSession, stagePlan, dimensions);
    }

    @Override
    public String getInitialSceneId() {
        return GameSceneId.MENU.id();
    }
}
