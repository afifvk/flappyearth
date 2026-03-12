package inf1009.p63.flappyearth.game.factories;

import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.RandomManager;
import inf1009.p63.flappyearth.game.entities.Collectible;
import inf1009.p63.flappyearth.game.entities.collectibles.RecyclingCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.ReusableBottleCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.SolarPanelCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.TreeSaplingCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.SmogCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.PlasticBottleCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.PlasticWasteCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.OilSpillCollectible;

public class CollectibleFactory {

    public CollectibleFactory() {}

    public void spawnRandom(EntityManager entityManager, RandomManager random,
                            float x, float y) {
        entityManager.queueAdd(createRandom(random, x, y));
    }

    public void spawnGood(EntityManager entityManager, RandomManager random,
                          float x, float y) {
        int pick = random.range(0, 3);
        Collectible good;
        switch (pick) {
            case 0: good = new RecyclingCollectible(x, y); break;
            case 1: good = new SolarPanelCollectible(x, y); break;
            case 2: good = new TreeSaplingCollectible(x, y); break;
            default: good = new ReusableBottleCollectible(x, y);
        }
        entityManager.queueAdd(good);
    }

    public void spawnBad(EntityManager entityManager, RandomManager random,
                         float x, float y) {
        int pick = random.range(0, 3);
        Collectible bad;
        switch (pick) {
            case 0: bad = new PlasticBottleCollectible(x, y); break;
            case 1: bad = new OilSpillCollectible(x, y); break;
            case 2: bad = new SmogCollectible(x, y); break;
            default: bad = new PlasticWasteCollectible(x, y);
        }
        entityManager.queueAdd(bad);
    }

    private Collectible createRandom(RandomManager random, float x, float y) {
        int pick = random.range(0, 8);
        switch (pick) {
            case 0:  return new RecyclingCollectible(x, y);
            case 1:  return new SolarPanelCollectible(x, y);
            case 2:  return new TreeSaplingCollectible(x, y);
            case 3:  return new ReusableBottleCollectible(x, y);
            case 4:  return new PlasticBottleCollectible(x, y);
            case 5:  return new OilSpillCollectible(x, y);
            case 6:  return new SmogCollectible(x, y);
            default: return new PlasticWasteCollectible(x, y);
        }
    }
}
