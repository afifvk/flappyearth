package inf1009.p63.flappyearth.game.events;

public class ObstaclePassedEvent {
    public final int entityId;

    public ObstaclePassedEvent(int entityId) {
        this.entityId = entityId;
    }
}
