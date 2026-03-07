package inf1009.p63.flappyearth.game.entities;

import inf1009.p63.flappyearth.game.config.Tags;

public abstract class Obstacle extends GameEntity {

    public enum ObstacleType {
        PIPE
    }

    private final ObstacleType obstacleType;
    private final String        factTopic;
    private boolean             passed = false;
    private boolean             isFlipped = false;

    public Obstacle(float x, float y, float width, float height,
                       String assetKey, ObstacleType type, String factTopic) {
        super(x, y, width, height, assetKey, Tags.HAZARD);
        this.obstacleType = type;
        this.factTopic    = factTopic;
    }

    @Override
    public void update(float delta) {}

    public ObstacleType getObstacleType() { return obstacleType; }
    public String       getFactTopic()    { return factTopic; }
    public boolean      isPassed()        { return passed; }
    public void         setPassed(boolean passed) { this.passed = passed; }
    public boolean      isFlipped()       { return isFlipped; }
    public void         setFlipped(boolean flipped) { this.isFlipped = flipped; }
}
