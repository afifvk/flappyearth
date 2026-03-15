package inf1009.p63.flappyearth.game.factories;

import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.obstacles.Pipe;

public class ObstacleFactory {

    private static final float OBSTACLE_WIDTH = 42.0f;

    public ObstacleFactory() {
    }

    public void spawnColumn(EntityManager entityManager, float x, float gapCentreY,
                            float gapSize, float screenH) {
        float halfGap = gapSize / 2f;
        float gapLowerY = gapCentreY - halfGap;
        float gapUpperY = gapCentreY + halfGap;

        float lowerPipeHeight = Math.max(gapLowerY, 0f);
        float upperPipeHeight = Math.max(screenH - gapUpperY, 0f);

        Obstacle lowerPipe = createRandom(x, 0f, OBSTACLE_WIDTH, lowerPipeHeight, false);
        Obstacle upperPipe = createRandom(x, gapUpperY, OBSTACLE_WIDTH, upperPipeHeight, true);

        entityManager.queueAdd(lowerPipe);
        entityManager.queueAdd(upperPipe);
    }

    private Obstacle createRandom(float x, float y, float w, float h, boolean isTop) {
        return new Pipe(x, y, w, h, isTop);
    }
}
