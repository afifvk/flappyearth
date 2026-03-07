package inf1009.p63.flappyearth.game.core;

import com.badlogic.gdx.graphics.Texture;
import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.GameSetup;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.input.KeyboardInputDevice;
import inf1009.p63.flappyearth.game.input.TouchInputDevice;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.scenes.MenuScene;
import inf1009.p63.flappyearth.game.scenes.StagePlan;
import inf1009.p63.flappyearth.game.scenes.StageTemplate;
import inf1009.p63.flappyearth.game.state.GameSession;

import java.util.Arrays;

public class FlappyEarthSetup implements GameSetup {

    @Override
    public void configure(GameContextManager contextManager, SceneManager sceneManager) {
        contextManager.getInputOutputManager().registerInputDevice(new KeyboardInputDevice());
        contextManager.getInputOutputManager().registerInputDevice(new TouchInputDevice());

        StagePlan stagePlan = new StagePlan(Arrays.asList(
            new StageTemplate(GameSceneId.STAGE_ONE.id(), "Stage 1: Impact Zone", "Collect 5 good items", 0.20f, 0.45f, 0.95f),
            new StageTemplate(GameSceneId.STAGE_TWO.id(), "Stage 2: Recovery Line", "Collect 5 good items", 0.98f, 0.94f, 0.62f),
            new StageTemplate(GameSceneId.STAGE_THREE.id(), "Stage 3: Turning Point", "Reach 15 to finish", 0.98f, 0.76f, 0.48f),
            new StageTemplate(GameSceneId.STAGE_FOUR.id(), "Stage 4: New Earth", "Peaceful skies", 0.46f, 0.72f, 0.50f)
        ));

        GameSession gameSession = new GameSession(stagePlan.getFinalTargetGoodCollectibles());

        contextManager.getAssetManager().load("pipe.png", Texture.class);
        contextManager.getAssetManager().load("background.png", Texture.class);
        contextManager.getAssetManager().load("flappy00.png", Texture.class);
        contextManager.getAssetManager().load("flappy01.png", Texture.class);
        contextManager.getAssetManager().load("flappy02.png", Texture.class);
        contextManager.getAssetManager().load("flappy03.png", Texture.class);
        contextManager.getAssetManager().load("flappy04.png", Texture.class);
        contextManager.getAssetManager().load("flappy05.png", Texture.class);
        contextManager.getAssetManager().finishLoading();

        sceneManager.registerScene(GameSceneId.MENU.id(), new MenuScene(sceneManager, contextManager, gameSession, stagePlan));

        for (StageTemplate stage : stagePlan.getStages()) {
            sceneManager.registerScene(stage.getSceneId(),
                    new GameScene(sceneManager, contextManager, gameSession, stagePlan, stage));
        }

        sceneManager.registerScene(GameSceneId.GAME_OVER.id(), new GameOverScene(sceneManager, contextManager));
    }

    @Override
    public String getInitialSceneId() {
        return GameSceneId.MENU.id();
    }
}
