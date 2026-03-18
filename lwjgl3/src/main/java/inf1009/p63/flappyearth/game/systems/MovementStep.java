package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.MovementManager;
import inf1009.p63.flappyearth.engine.services.TimeManager;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.ArrayList;
import java.util.List;

public class MovementStep implements LoopStep {

    private final EntityStore  entityStore;
    private final MovementManager movementManager;
    private final TimeManager    timeManager;
    private final GameState state;
    private final GameplayDimensions dimensions;
    private final List<Movable> movablesScratch = new ArrayList<>();

    public MovementStep(EntityStore entityStore,
                               MovementManager movementManager,
                               TimeManager timeManager,
                               GameState state,
                               GameplayDimensions dimensions) {
        this.entityStore   = entityStore;
        this.movementManager = movementManager;
        this.timeManager     = timeManager;
        this.state           = state;
        this.dimensions      = dimensions;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        entityStore.flush();

        float scaledDelta = timeManager.scale(delta);
        entityStore.collectMovables(movablesScratch);
        movementManager.moveAll(movablesScratch, scaledDelta);

        Player player = (Player) entityStore.getFirstByTag(Tags.PLAYER);
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
