package inf1009.p63.flappyearth.game.managers;

import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.game.entities.Collectible;

import java.util.List;

public class CollectibleManager {

    private final EntityManager entityManager;

    public CollectibleManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Collectible> getCollectibles() {
        return entityManager.getByType(Collectible.class);
    }

    public void clearAll() {
        for (Collectible c : getCollectibles()) {
            entityManager.queueRemove(c);
        }
    }
}
