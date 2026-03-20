package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.MovementManager;
import inf1009.p63.flappyearth.engine.services.TimeManager;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.runtime.GameplayRuntimeContext;

import java.util.ArrayList;
import java.util.List;

public class MovementStep implements LoopStep {

    private final GameplayRuntimeContext runtimeContext;
    private final MovementManager movementManager;
    private final TimeManager    timeManager;
    private final List<Movable> movablesScratch = new ArrayList<>();

    public MovementStep(GameplayRuntimeContext runtimeContext,
                               MovementManager movementManager,
                               TimeManager timeManager) {
        this.runtimeContext = runtimeContext;
        this.movementManager = movementManager;
        this.timeManager     = timeManager;
    }

    @Override
    public void execute(float delta) {
        if (!runtimeContext.gameState().isAlive()) return;

        runtimeContext.entityStore().flush();

        float scaledDelta = timeManager.scale(delta);
        runtimeContext.entityStore().collectMovables(movablesScratch);
        movementManager.moveAll(movablesScratch, scaledDelta);

        Player player = runtimeContext.player();
        if (player != null) {
            float playerY = player.getBounds().y;
            float playerH = player.getBounds().height;

            float worldHeight = runtimeContext.dimensions().getWorldHeight();
            if (playerY + playerH > worldHeight) {
                player.getBounds().y = worldHeight - playerH;
            }
        }
    }
}
