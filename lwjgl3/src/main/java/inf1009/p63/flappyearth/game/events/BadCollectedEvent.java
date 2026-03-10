package inf1009.p63.flappyearth.game.events;

public class BadCollectedEvent {
    public final String collectibleType;
    public final int entityId;

    public BadCollectedEvent(String collectibleType, int entityId) {
        this.collectibleType = collectibleType;
        this.entityId = entityId;
    }
}
