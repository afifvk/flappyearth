package inf1009.p63.flappyearth.game.entities.collectibles.bad;

import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Collectible;

abstract class BadCollectible extends Collectible {
    public BadCollectible(float x, float y, float width, float height,
                          String assetKey, CollectibleType type) {
        super(x, y, width, height, assetKey, type, Tags.COLLECTIBLE_BAD);
        setColor(1f, 0f, 0f);
    }
}

