package inf1009.p63.flappyearth.game.entities;

public abstract class Collectible extends GameEntity {

    public enum CollectibleType {
        RECYCLING,
        SOLAR_PANEL,
        TREE_SAPLING,
        REUSABLE_BOTTLE,
        SMOG,
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
    public void update(float delta) {}

    public CollectibleType getCollectibleType() { return collectibleType; }

    public void onCollect() {}
}
