package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.GameState;

public class GameOverStep implements StepManager {

    private final EntityManager entityManager;
    private final GameState     state;

    public GameOverStep(EntityManager entityManager, GameState state,
                        EventManager eventManager) {
        this.entityManager = entityManager;
        this.state         = state;
        // No BAD_HIT listener - hearts handled in CollisionStep
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        if (player.getBounds().y < 0f) {
            state.setAlive(false);
            state.setGameOverPending(true);
            state.setRequestedScene(GameState.SceneRequest.GAME_OVER);
        }
    }
}