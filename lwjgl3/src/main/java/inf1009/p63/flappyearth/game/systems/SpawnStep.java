package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.RandomManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.factories.CollectibleFactory;
import inf1009.p63.flappyearth.game.factories.ObstacleFactory;
import inf1009.p63.flappyearth.game.state.GameState;

public class SpawnStep implements LoopStep {

    private static final int   MAX_GOOD           = 2;
    private static final int   MAX_BAD            = 2;

    private final EntityStore      entityStore;
    private final ObstacleFactory    obstacleFactory;
    private final CollectibleFactory collectibleFactory;
    private final RandomManager      random;
    private final GameState          state;
    private final GameConfig         config;
    private final GameplayDimensions dimensions;
    private float initialSpawnDelay;
    private boolean firstSpawnPending;

    private float spawnTimer     = 0f;
    private float lastPipeSpawnX = Float.MIN_VALUE;

    public SpawnStep(EntityStore entityStore,
                       ObstacleFactory obstacleFactory,
                       CollectibleFactory collectibleFactory,
                       RandomManager random, GameState state,
                       GameConfig config,
                       GameplayDimensions dimensions,
                       float initialSpawnDelaySeconds) {
        this.entityStore      = entityStore;
        this.obstacleFactory    = obstacleFactory;
        this.collectibleFactory = collectibleFactory;
        this.random             = random;
        this.state              = state;
        this.config             = config;
        this.dimensions         = dimensions;
        this.initialSpawnDelay  = Math.max(0f, initialSpawnDelaySeconds);
        this.firstSpawnPending  = true;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;
        if (!state.isSpawningEnabled()) return;

        Player player = (Player) entityStore.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        if (firstSpawnPending) {
            if (initialSpawnDelay > 0f) {
                initialSpawnDelay = Math.max(0f, initialSpawnDelay - delta);
                return;
            }
            firstSpawnPending = false;
        } else {
            spawnTimer += delta;
            if (spawnTimer < config.obstacleSpawnInterval) return;
            spawnTimer = 0f;
        }

        float worldWidth = dimensions.getWorldWidth();
        float worldHeight = dimensions.getWorldHeight();

        float playerX    = player.getBounds().x;
        float cameraRightEdgeX = playerX + (worldWidth * dimensions.getCameraRightEdgeRatio());
        float pipeSpawnX = cameraRightEdgeX + dimensions.getPipeSpawnLead();

        float minGapY    = config.gapSize / 2f + dimensions.getVerticalPadding();
        float maxGapY    = worldHeight - config.gapSize / 2f - dimensions.getVerticalPadding();
        float gapCentreY = random.range(minGapY, maxGapY);

        obstacleFactory.spawnColumn(entityStore, pipeSpawnX, gapCentreY, config.gapSize, worldHeight);

        if (lastPipeSpawnX != Float.MIN_VALUE) {
            float minY = dimensions.getCollectibleMargin() + dimensions.getVerticalPadding();
            float maxY = worldHeight - dimensions.getCollectibleMargin() - dimensions.getVerticalPadding();

            int goodCount = entityStore.countByTag(Tags.COLLECTIBLE_GOOD);
            int badCount  = entityStore.countByTag(Tags.COLLECTIBLE_BAD);

            if (goodCount < MAX_GOOD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(
                        dimensions.getCollectibleSpawnAheadMin(),
                        dimensions.getCollectibleSpawnAheadMax());
                collectibleFactory.spawnGood(entityStore, random, spawnX, random.range(minY, maxY));
            }
            if (badCount < MAX_BAD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(
                        dimensions.getCollectibleSpawnAheadMin(),
                        dimensions.getCollectibleSpawnAheadMax());
                collectibleFactory.spawnBad(entityStore, random, spawnX, random.range(minY, maxY));
            }
        }

        lastPipeSpawnX = pipeSpawnX;
    }
}