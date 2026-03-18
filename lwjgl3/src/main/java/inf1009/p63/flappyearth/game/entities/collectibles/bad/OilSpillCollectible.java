package inf1009.p63.flappyearth.game.entities.collectibles.bad;

public class OilSpillCollectible extends BadCollectible {
    public OilSpillCollectible(float x, float y) {
        super(x, y, "textures/entities/collectibles/bad/oil_spill.png", CollectibleType.OIL_SPILL);
    }
    @Override
    public void onCollect() {}
}

