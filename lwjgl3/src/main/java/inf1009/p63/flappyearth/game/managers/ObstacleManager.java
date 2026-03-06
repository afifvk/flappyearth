package inf1009.p63.flappyearth.game.managers;

import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.game.entities.Obstacle;

import java.util.List;

public class ObstacleManager {

    private final EntityManager entityManager;

    public ObstacleManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Obstacle> getObstacles() {
        return entityManager.getByType(Obstacle.class);
    }

    public void clearAll() {
        for (Obstacle obs : getObstacles()) {
            entityManager.queueRemove(obs);
        }
    }
}
