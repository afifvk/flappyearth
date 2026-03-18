package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.model.EntityBase;
import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;

import java.util.List;

public class DespawnStep implements LoopStep {

    private final EntityStore entityStore;
    private final GameplayDimensions dimensions;

    public DespawnStep(EntityStore entityStore, GameplayDimensions dimensions) {
        this.entityStore = entityStore;
        this.dimensions = dimensions;
    }

    @Override
    public void execute(float delta) {
        Player player = (Player) entityStore.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        float cullX = player.getBounds().x
            - dimensions.getWorldWidth()
            - dimensions.getDespawnCleanupMargin();

        List<EntityBase> entities = entityStore.getAll();
        for (EntityBase e : entities) {
            if (e instanceof Player) continue;
            if (e.getBounds().x + e.getBounds().width < cullX) {
                entityStore.queueRemove(e);
            }
        }
    }
}
