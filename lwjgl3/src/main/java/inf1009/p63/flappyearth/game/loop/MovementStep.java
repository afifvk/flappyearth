package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.MovementManager;
import inf1009.p63.flappyearth.engine.managers.TimeManager;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.List;

public class MovementStep implements StepManager {

    private final EntityManager  entityManager;
    private final MovementManager movementManager;
    private final TimeManager    timeManager;
    private final GameState state;
    private final GameplayDimensions dimensions;

    public MovementStep(EntityManager entityManager,
                               MovementManager movementManager,
                               TimeManager timeManager,
                               GameState state,
                               GameplayDimensions dimensions) {
        this.entityManager   = entityManager;
        this.movementManager = movementManager;
        this.timeManager     = timeManager;
        this.state           = state;
        this.dimensions      = dimensions;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        entityManager.flush();

        float scaledDelta = timeManager.scale(delta);
        List<Movable> movables = entityManager.getMovables();
        movementManager.moveAll(movables, scaledDelta);

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player != null) {
            float playerY = player.getBounds().y;
            float playerH = player.getBounds().height;

            float worldHeight = dimensions.getWorldHeight();
            if (playerY + playerH > worldHeight) {
                player.getBounds().y = worldHeight - playerH;
            }
        }
    }
}
