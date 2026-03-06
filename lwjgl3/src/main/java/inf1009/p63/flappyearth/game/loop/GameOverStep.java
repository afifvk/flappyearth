package inf1009.p63.flappyearth.game.loop;
import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.state.GameState;

public class GameOverStep implements StepManager {

    private final EntityManager    entityManager;
    private final GameState state;

    public GameOverStep(EntityManager entityManager, GameState state,
                               EventManager eventManager) {
        this.entityManager = entityManager;
        this.state         = state;

        // Listen for collisions and trigger game over
        eventManager.subscribe(GameEvents.BAD_HIT, data -> {
            state.setAlive(false);
            state.setGameOverPending(true);
            state.setRequestedScene(GameState.SceneRequest.GAME_OVER);
        });
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        // Kill only if falls below bottom of screen (top is clamped safely in MovementStep)
        if (player.getBounds().y < 0f) {
            state.setAlive(false);
            state.setGameOverPending(true);
            state.setRequestedScene(GameState.SceneRequest.GAME_OVER);
        }
    }
}
