package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.RandomManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.factories.CollectibleFactory;
import inf1009.p63.flappyearth.game.factories.ObstacleFactory;
import inf1009.p63.flappyearth.game.runtime.GameplayRuntimeContext;

public class SpawnStep implements LoopStep {

    private static final int   MAX_GOOD           = 2;
    private static final int   MAX_BAD            = 2;

    private final GameplayRuntimeContext runtimeContext;
    private final ObstacleFactory    obstacleFactory;
    private final CollectibleFactory collectibleFactory;
    private final RandomManager      random;
    private final GameConfig         config;
    private float initialSpawnDelay;
    private boolean firstSpawnPending;

    private float spawnTimer     = 0f;
    private float lastPipeSpawnX = Float.MIN_VALUE;

    public SpawnStep(GameplayRuntimeContext runtimeContext,
                       ObstacleFactory obstacleFactory,
                       CollectibleFactory collectibleFactory,
                       RandomManager random,
                       GameConfig config,
                       float initialSpawnDelaySeconds) {
        this.runtimeContext   = runtimeContext;
        this.obstacleFactory    = obstacleFactory;
        this.collectibleFactory = collectibleFactory;
        this.random             = random;
        this.config             = config;
        this.initialSpawnDelay  = Math.max(0f, initialSpawnDelaySeconds);
        this.firstSpawnPending  = true;
    }

    @Override
    public void execute(float delta) {
        if (!runtimeContext.gameState().isAlive()) return;
        if (!runtimeContext.gameState().isSpawningEnabled()) return;

        Player player = runtimeContext.player();
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

        float worldWidth = runtimeContext.dimensions().getWorldWidth();
        float worldHeight = runtimeContext.dimensions().getWorldHeight();

        float playerX    = player.getBounds().x;
        float cameraRightEdgeX = playerX + (worldWidth * runtimeContext.dimensions().getCameraRightEdgeRatio());
        float pipeSpawnX = cameraRightEdgeX + runtimeContext.dimensions().getPipeSpawnLead();

        float minGapY    = config.gapSize / 2f + runtimeContext.dimensions().getVerticalPadding();
        float maxGapY    = worldHeight - config.gapSize / 2f - runtimeContext.dimensions().getVerticalPadding();
        float gapCentreY = random.range(minGapY, maxGapY);

        obstacleFactory.spawnColumn(runtimeContext.entityStore(), pipeSpawnX, gapCentreY, config.gapSize, worldHeight);

        if (lastPipeSpawnX != Float.MIN_VALUE) {
            float minY = runtimeContext.dimensions().getCollectibleMargin() + runtimeContext.dimensions().getVerticalPadding();
            float maxY = worldHeight - runtimeContext.dimensions().getCollectibleMargin() - runtimeContext.dimensions().getVerticalPadding();

            int goodCount = runtimeContext.entityStore().countByTag(Tags.COLLECTIBLE_GOOD);
            int badCount  = runtimeContext.entityStore().countByTag(Tags.COLLECTIBLE_BAD);

            if (goodCount < MAX_GOOD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(
                        runtimeContext.dimensions().getCollectibleSpawnAheadMin(),
                        runtimeContext.dimensions().getCollectibleSpawnAheadMax());
                collectibleFactory.spawnGood(runtimeContext.entityStore(), random, spawnX, random.range(minY, maxY));
            }
            if (badCount < MAX_BAD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(
                        runtimeContext.dimensions().getCollectibleSpawnAheadMin(),
                        runtimeContext.dimensions().getCollectibleSpawnAheadMax());
                collectibleFactory.spawnBad(runtimeContext.entityStore(), random, spawnX, random.range(minY, maxY));
            }
        }

        lastPipeSpawnX = pipeSpawnX;
    }
}
