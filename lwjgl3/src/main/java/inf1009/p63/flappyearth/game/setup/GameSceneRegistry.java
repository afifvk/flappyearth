package inf1009.p63.flappyearth.game.setup;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.StageConfig;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.scenes.CreditsScene;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.scenes.GameplayScene;
import inf1009.p63.flappyearth.game.scenes.MenuScene;
import inf1009.p63.flappyearth.game.scenes.SettingsScene;
import inf1009.p63.flappyearth.game.state.GameSession;

public final class GameSceneRegistry {

    private GameSceneRegistry() {}

    public static void registerAll(SceneManager sceneManager,
                                   EngineContext context,
                                   GameSession gameSession,
                                   StagePlan stagePlan,
                                   GameplayDimensions dimensions) {
        sceneManager.registerScene(GameSceneId.MENU.id(), new MenuScene(sceneManager, context, gameSession, stagePlan));
        sceneManager.registerScene(GameSceneId.SETTINGS.id(), new SettingsScene(sceneManager, context));
        sceneManager.registerScene(GameSceneId.CREDITS.id(), new CreditsScene(sceneManager, context));

        for (StageConfig stage : stagePlan.getStages()) {
            sceneManager.registerScene(
                    stage.getSceneId(),
                    new GameplayScene(sceneManager, context, gameSession, stagePlan, stage, dimensions)
            );
        }

        sceneManager.registerScene(
                GameSceneId.GAME_OVER.id(),
                new GameOverScene(sceneManager, context, gameSession, stagePlan)
        );
    }
}
