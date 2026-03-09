package inf1009.p63.flappyearth.game.factories;

import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.RandomManager;

public class EntityFactory {

    private final ObstacleFactory   obstacleFactory;
    private final CollectibleFactory collectibleFactory;
    private final RandomManager            random;

    public EntityFactory(RandomManager random) {
        this.random             = random;
        this.obstacleFactory    = new ObstacleFactory();
        this.collectibleFactory = new CollectibleFactory();
    }

    public ObstacleFactory   getObstacleFactory()    { return obstacleFactory; }
    public CollectibleFactory getCollectibleFactory() { return collectibleFactory; }

    public void spawnObstacleColumn(EntityManager entityManager, float x,
                                    float gapCentreY, float gapSize, float screenH) {
        obstacleFactory.spawnColumn(entityManager, x, gapCentreY, gapSize, screenH);
    }

    public void spawnCollectible(EntityManager entityManager, float x, float y) {
        collectibleFactory.spawnRandom(entityManager, random, x, y);
    }

    public void spawnDebrisForObstacle(EntityManager entityManager, inf1009.p63.flappyearth.game.entities.Obstacle obstacle) {
        // create a few small debris pieces originating from obstacle bounds
        float x = obstacle.getBounds().x + obstacle.getBounds().width / 2f;
        float y = obstacle.getBounds().y + obstacle.getBounds().height / 2f;
        int pieces = 4 + random.range(0,2);
        for (int i = 0; i < pieces; i++) {
            float w = 12f + random.range(0f, 8f);
            float h = 8f + random.range(0f, 6f);
            float vx = random.range(-200f, 200f);
            float vy = random.range(100f, 350f);
            float life = 0.9f + random.range(0f, 0.6f);
            entityManager.queueAdd(new inf1009.p63.flappyearth.game.entities.Debris(x, y, w, h, vx, vy, life, entityManager));
        }
    }
}
