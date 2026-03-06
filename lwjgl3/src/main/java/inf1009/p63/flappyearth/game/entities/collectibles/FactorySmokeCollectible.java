package inf1009.p63.flappyearth.game.entities.collectibles;

public class FactorySmokeCollectible extends BadCollectible {
    public FactorySmokeCollectible(float x, float y) {
        super(x, y, 30, 30, "factory_smoke", CollectibleType.FACTORY_SMOKE);
    }
    @Override
    public float getSmokeVisibility() {
        return 1.0f;
}
}
