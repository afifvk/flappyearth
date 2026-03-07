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
}
