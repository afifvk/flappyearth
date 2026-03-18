package inf1009.p63.flappyearth.game.entities.collectibles.bad;

import inf1009.p63.flappyearth.game.entities.Collectible;

public class PlasticBottleCollectible extends BadCollectible {
    public PlasticBottleCollectible(float x, float y) {
        super(x, y,
              Collectible.STANDARD_SIZE * 0.9f,
              "textures/entities/collectibles/bad/plastic_bottle.png",
              CollectibleType.PLASTIC_BOTTLE);
    }
}
