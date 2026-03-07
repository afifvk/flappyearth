package inf1009.p63.flappyearth.game.loop;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.RandomManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.factories.CollectibleFactory;
import inf1009.p63.flappyearth.game.factories.ObstacleFactory;
import inf1009.p63.flappyearth.game.state.GameState;

public class SpawnSystem implements StepManager {

    private static final float COLLECTIBLE_MARGIN = 20f;
    private static final int   MAX_GOOD           = 2;
    private static final int   MAX_BAD            = 2;
    private static final float PIPE_SPAWN_LEAD    = 40f;

    private final EntityManager      entityManager;
    private final ObstacleFactory    obstacleFactory;
    private final CollectibleFactory collectibleFactory;
    private final RandomManager      random;
    private final GameState          state;
    private final GameConfig         config;
    private float initialSpawnDelay;
    private boolean firstSpawnPending;

    private float spawnTimer     = 0f;
    private float lastPipeSpawnX = Float.MIN_VALUE;

    public SpawnSystem(EntityManager entityManager,
                       ObstacleFactory obstacleFactory,
                       CollectibleFactory collectibleFactory,
                       RandomManager random, GameState state,
                       GameConfig config,
                       float initialSpawnDelaySeconds) {
        this.entityManager      = entityManager;
        this.obstacleFactory    = obstacleFactory;
        this.collectibleFactory = collectibleFactory;
        this.random             = random;
        this.state              = state;
        this.config             = config;
        this.initialSpawnDelay  = Math.max(0f, initialSpawnDelaySeconds);
        this.firstSpawnPending  = true;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;
        if (!state.isSpawningEnabled()) return;

        float screenH = Gdx.graphics.getHeight();
        float screenW = Gdx.graphics.getWidth();

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

        float playerX    = player.getBounds().x;
        float cameraRightEdgeX = playerX + (screenW * 0.75f);
        float pipeSpawnX = cameraRightEdgeX + PIPE_SPAWN_LEAD;

        float minGapY    = config.gapSize / 2f + 50f;
        float maxGapY    = screenH - config.gapSize / 2f - 50f;
        float gapCentreY = random.range(minGapY, maxGapY);

        obstacleFactory.spawnColumn(entityManager, pipeSpawnX, gapCentreY, config.gapSize, screenH);

        if (lastPipeSpawnX != Float.MIN_VALUE) {
            float minY = COLLECTIBLE_MARGIN + 50f;
            float maxY = screenH - COLLECTIBLE_MARGIN - 50f;

            int goodCount = entityManager.getByTag(Tags.COLLECTIBLE_GOOD).size();
            int badCount  = entityManager.getByTag(Tags.COLLECTIBLE_BAD).size();

            if (goodCount < MAX_GOOD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(100f, 220f);
                collectibleFactory.spawnGood(entityManager, random, spawnX, random.range(minY, maxY));
            }
            if (badCount < MAX_BAD && random.chance(config.collectibleSpawnChance)) {
                float spawnX = pipeSpawnX + random.range(100f, 220f);
                collectibleFactory.spawnBad(entityManager, random, spawnX, random.range(minY, maxY));
            }
        }

        lastPipeSpawnX = pipeSpawnX;
    }
}