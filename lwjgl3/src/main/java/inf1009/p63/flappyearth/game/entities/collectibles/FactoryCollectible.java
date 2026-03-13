package inf1009.p63.flappyearth.game.entities.collectibles;

public class FactoryCollectible extends BadCollectible {
    public FactoryCollectible(float x, float y) {
        super(x, y, 60, 60, "collectibles/bc_factory.png", CollectibleType.FACTORY);
    }
}
