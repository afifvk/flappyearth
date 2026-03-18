package inf1009.p63.flappyearth.game.factories;

import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.RandomManager;
import inf1009.p63.flappyearth.game.entities.Collectible;
import inf1009.p63.flappyearth.game.entities.collectibles.good.RecyclingCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.good.SolarPanelCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.good.TreeSaplingCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.good.GreenhouseCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.good.WindTurbineCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.bad.FactoryCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.bad.PlasticBottleCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.bad.PlasticWasteCollectible;
import inf1009.p63.flappyearth.game.entities.collectibles.bad.OilSpillCollectible;

public class CollectibleFactory {

    public CollectibleFactory() {}

    public void spawnRandom(EntityStore entityManager, RandomManager random,
                            float x, float y) {
        entityManager.queueAdd(createRandom(random, x, y));
    }

    public void spawnGood(EntityStore entityManager, RandomManager random,
                          float x, float y) {
        int pick = random.range(0, 4);
        Collectible good;
        switch (pick) {
            case 0: good = new RecyclingCollectible(x, y); break;
            case 1: good = new SolarPanelCollectible(x, y); break;
            case 2: good = new TreeSaplingCollectible(x, y); break;
            case 3: good = new GreenhouseCollectible(x, y); break;
            default: good = new WindTurbineCollectible(x, y);
        }
        entityManager.queueAdd(good);
    }

    public void spawnBad(EntityStore entityManager, RandomManager random,
                         float x, float y) {
        int pick = random.range(0, 3);
        Collectible bad;
        switch (pick) {
            case 0: bad = new PlasticBottleCollectible(x, y); break;
            case 1: bad = new OilSpillCollectible(x, y); break;
            case 2: bad = new FactoryCollectible(x, y); break;
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
            case 3:  return new GreenhouseCollectible(x, y);
            case 4:  return new WindTurbineCollectible(x, y);
            case 5:  return new PlasticBottleCollectible(x, y);
            case 6:  return new OilSpillCollectible(x, y);
            case 7:  return new FactoryCollectible(x, y);
            default: return new PlasticWasteCollectible(x, y);
        }
    }
}

