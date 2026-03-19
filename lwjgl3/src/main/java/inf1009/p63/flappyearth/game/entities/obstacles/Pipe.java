package inf1009.p63.flappyearth.game.entities.obstacles;

import inf1009.p63.flappyearth.game.entities.Obstacle;

public class Pipe extends Obstacle {
    public Pipe(float x, float y, float width, float height, boolean isTop) {
        super(x, y, width, height, "textures/entities/obstacles/pipe.png", ObstacleType.PIPE);
        setFlipped(isTop);
    }
}
