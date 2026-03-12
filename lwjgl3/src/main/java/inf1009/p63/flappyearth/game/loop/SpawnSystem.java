package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.RandomManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.factories.CollectibleFactory;
import inf1009.p63.flappyearth.game.factories.ObstacleFactory;
import inf1009.p63.flappyearth.game.state.GameState;

public class SpawnSystem implements StepManager {

    private static final int   MAX_GOOD           = 2;
    private static final int   MAX_BAD            = 2;

    private final EntityManager      entityManager;
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

    public SpawnSystem(EntityManager entityManager,
                       ObstacleFactory obstacleFactory,
                       CollectibleFactory collectibleFactory,
                       RandomManager random, GameState state,
                       GameConfig config,
                       GameplayDimensions dimensions,
                       float initialSpawnDelaySeconds) {
        this.entityManager      = entityManager;
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

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
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

        obstacleFactory.spawnColumn(entityManager, pipeSpawnX, gapCentreY, config.gapSize, worldHeight);

        if (lastPipeSpawnX != Float.MIN_VALUE) {
            float minY = dimensions.getCollectibleMargin() + dimensions.getVerticalPadding();
            float maxY = worldHeight - dimensions.getCollectibleMargin() - dimensions.getVerticalPadding();

            int goodCount = entityManager.getByTag(Tags.COLLECTIBLE_GOOD).size();
            int badCount  = entityManager.getByTag(Tags.COLLECTIBLE_BAD).size();

            if (goodCount < MAX_GOOD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(
                        dimensions.getCollectibleSpawnAheadMin(),
                        dimensions.getCollectibleSpawnAheadMax());
                collectibleFactory.spawnGood(entityManager, random, spawnX, random.range(minY, maxY));
            }
            if (badCount < MAX_BAD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(
                        dimensions.getCollectibleSpawnAheadMin(),
                        dimensions.getCollectibleSpawnAheadMax());
                collectibleFactory.spawnBad(entityManager, random, spawnX, random.range(minY, maxY));
            }
        }

        lastPipeSpawnX = pipeSpawnX;
    }
}