package inf1009.p63.flappyearth.game.entities.collectibles;

public class OilSpillCollectible extends BadCollectible {
    public OilSpillCollectible(float x, float y) {
        super(x, y, 60, 60, "oil_spill.png", CollectibleType.OIL_SPILL);
    }
    @Override
    public void onCollect() {}
}
