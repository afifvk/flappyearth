package inf1009.p63.flappyearth.game.entities.collectibles;

import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Collectible;

// Abstract base class for beneficial collectibles (yellow color)
abstract class GoodCollectible extends Collectible {
    public GoodCollectible(float x, float y, float width, float height,
                           String assetKey, CollectibleType type) {
        super(x, y, width, height, assetKey, type, Tags.COLLECTIBLE_GOOD);
        // Good collectibles display as yellow
        setColor(1f, 1f, 0f);
    }
}
