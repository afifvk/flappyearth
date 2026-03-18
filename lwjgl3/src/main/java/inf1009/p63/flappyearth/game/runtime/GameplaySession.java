package inf1009.p63.flappyearth.game.runtime;

import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.controllers.CameraController;
import inf1009.p63.flappyearth.game.controllers.DeathController;
import inf1009.p63.flappyearth.game.controllers.EndingController;
import inf1009.p63.flappyearth.game.controllers.StageProgressController;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.GameState;
import inf1009.p63.flappyearth.game.systems.GameplayLoop;

public class GameplaySession {

    public static final class UpdateResult {
        public Player player;
        public boolean routedToGameOver;
        public boolean stageTransitioning;
        public String pendingTransitionTarget;
    }

    public UpdateResult update(float delta,
                               EntityStore entityStore,
                               GameState gameState,
                               GameplayLoop gameplayLoop,
                               DeathController deathController,
                               CameraController cameraController,
                               EndingController endingController,
                               StageProgressController stageController) {
        UpdateResult result = new UpdateResult();

        if (!gameState.isDeathSequenceActive()) {
            gameplayLoop.update(delta);
        }

        Player player = (Player) entityStore.getFirstByTag(Tags.PLAYER);
        result.player = player;
        deathController.update(delta, player, cameraController, endingController.isSafeEndingWindow());

        result.routedToGameOver = deathController.routeToGameOverIfRequested();
        if (result.routedToGameOver) {
            return result;
        }

        result.stageTransitioning = stageController.isTransitioning();
        if (result.stageTransitioning) {
            return result;
        }

        stageController.update();
        result.pendingTransitionTarget = stageController.consumePendingTransition();
        return result;
    }
}
