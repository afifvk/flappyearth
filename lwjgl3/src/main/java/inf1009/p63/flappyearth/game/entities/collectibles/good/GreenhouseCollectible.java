package inf1009.p63.flappyearth.game.entities.collectibles.good;

public class GreenhouseCollectible extends GoodCollectible {
    public GreenhouseCollectible(float x, float y) {
        super(x, y, STANDARD_SIZE, STANDARD_SIZE * 0.9f, "textures/entities/collectibles/good/greenhouse.png", CollectibleType.GREENHOUSE);
    }
}

