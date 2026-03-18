package inf1009.p63.flappyearth.game.entities.collectibles.bad;

import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Collectible;

abstract class BadCollectible extends Collectible {
    public BadCollectible(float x, float y, String assetKey, CollectibleType type) {
        super(x, y, STANDARD_SIZE, STANDARD_SIZE, assetKey, type, Tags.COLLECTIBLE_BAD);
        setColor(1f, 0f, 0f);
    }

    public BadCollectible(float x, float y, float size, String assetKey, CollectibleType type) {
        super(x, y, size, size, assetKey, type, Tags.COLLECTIBLE_BAD);
        setColor(1f, 0f, 0f);
    }
}

