package inf1009.p63.flappyearth.game.events;

public class BadHitEvent {
    // Obstacle collision data
    public final String obstacleType;
    public final int    entityId;

    public BadHitEvent(String obstacleType, int entityId) {
        this.obstacleType = obstacleType;
        this.entityId     = entityId;
    }
}
