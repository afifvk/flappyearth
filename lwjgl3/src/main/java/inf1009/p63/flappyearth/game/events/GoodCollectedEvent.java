package inf1009.p63.flappyearth.game.events;

public class GoodCollectedEvent {
    // Collectible pickup data
    public final String collectibleType;
    public final int    entityId;

    public GoodCollectedEvent(String collectibleType, int entityId) {
        this.collectibleType = collectibleType;
        this.entityId        = entityId;
    }
}
