package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.model.EntityBase;
import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.runtime.GameplayRuntimeContext;

import java.util.List;

public class DespawnStep implements LoopStep {

    private final GameplayRuntimeContext runtimeContext;

    public DespawnStep(GameplayRuntimeContext runtimeContext) {
        this.runtimeContext = runtimeContext;
    }

    @Override
    public void execute(float delta) {
        Player player = runtimeContext.player();
        if (player == null) return;

        float cullX = player.getBounds().x
            - runtimeContext.dimensions().getWorldWidth()
            - runtimeContext.dimensions().getDespawnCleanupMargin();

        List<EntityBase> entities = runtimeContext.entityStore().getAll();
        for (EntityBase e : entities) {
            if (e instanceof Player) continue;
            if (e.getBounds().x + e.getBounds().width < cullX) {
                runtimeContext.entityStore().queueRemove(e);
            }
        }
    }
}
