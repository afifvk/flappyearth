package inf1009.p63.flappyearth.game.entities.obstacles;

import inf1009.p63.flappyearth.game.entities.Obstacle;

public class Pipe extends Obstacle {
    public Pipe(float x, float y, float width, float height, boolean isTop) {
        // Pipe obstacle with texture - kills player on contact
        super(x, y, width, height, "pipe.png", ObstacleType.PIPE, "pipe");
        // Top pipes should be flipped/inverted visually
        setFlipped(isTop);
    }
}
