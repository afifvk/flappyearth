package inf1009.p63.flappyearth.game.entities;

public abstract class Collectible extends GameEntity {

    public enum CollectibleType {
        RECYCLING,
        SOLAR_PANEL,
        TREE_SAPLING,
        REUSABLE_BOTTLE,
        CAR_EXHAUST,
        FACTORY_SMOKE,
        OIL_SPILL,
        PLASTIC_WASTE
    }

    private final CollectibleType collectibleType;

    public Collectible(float x, float y, float width, float height,
                       String assetKey, CollectibleType type, String tag) {
        super(x, y, width, height, assetKey, tag);
        this.collectibleType = type;
    }

    @Override
    public void update(float delta) {
        // add any collectible-specific animation or behavior here if needed
    }

    public CollectibleType getCollectibleType() { return collectibleType; }

    public float getSmokeVisibility() {
        // Assign visibility based on the type
        switch (collectibleType) {
            case FACTORY_SMOKE:
            case OIL_SPILL:
            case CAR_EXHAUST:
                return 1.0f; // Very visible for "Bad" items
            case SOLAR_PANEL:
            case TREE_SAPLING:
                return 0.2f; // Faint for "Good" items
            default:
                return 0.5f;
        }
    }

    // Keep this as a "Placeholder"
    public void onCollect() {
        // Leave empty or add a print statement
    }
}
