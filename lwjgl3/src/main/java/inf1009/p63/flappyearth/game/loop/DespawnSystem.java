package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.entities.Entity;
import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;

import java.util.List;

public class DespawnSystem implements StepManager {

    private final EntityManager entityManager;
    private final GameplayDimensions dimensions;

    public DespawnSystem(EntityManager entityManager, GameplayDimensions dimensions) {
        this.entityManager = entityManager;
        this.dimensions = dimensions;
    }

    @Override
    public void execute(float delta) {
        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        float cullX = player.getBounds().x
            - dimensions.getWorldWidth()
            - dimensions.getDespawnCleanupMargin();

        List<Entity> entities = entityManager.getAll();
        for (Entity e : entities) {
            if (e instanceof Player) continue;
            if (e.getBounds().x + e.getBounds().width < cullX) {
                entityManager.queueRemove(e);
            }
        }
    }
}
