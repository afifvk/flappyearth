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

public class SpawnStep implements StepManager {

    private static final float COLLECTIBLE_MARGIN = 20f;
    private static final int   MAX_GOOD           = 2;
    private static final int   MAX_BAD            = 2;

    private final EntityManager      entityManager;
    private final ObstacleFactory    obstacleFactory;
    private final CollectibleFactory collectibleFactory;
    private final RandomManager      random;
    private final GameState          state;
    private final GameConfig         config;

    private float spawnTimer     = 0f;
    private float lastPipeSpawnX = Float.MIN_VALUE;

    public SpawnStep(EntityManager entityManager,
                     ObstacleFactory obstacleFactory,
                     CollectibleFactory collectibleFactory,
                     RandomManager random, GameState state,
                     GameConfig config) {
        this.entityManager      = entityManager;
        this.obstacleFactory    = obstacleFactory;
        this.collectibleFactory = collectibleFactory;
        this.random             = random;
        this.state              = state;
        this.config             = config;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        spawnTimer += delta;
        if (spawnTimer < config.obstacleSpawnInterval) return;
        spawnTimer = 0f;

        float screenH = Gdx.graphics.getHeight();
        float screenW = Gdx.graphics.getWidth();

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        float playerX    = player.getBounds().x;
        float pipeSpawnX = playerX + screenW + 80f;

        float minGapY    = config.gapSize / 2f + 50f;
        float maxGapY    = screenH - config.gapSize / 2f - 50f;
        float gapCentreY = random.range(minGapY, maxGapY);

        obstacleFactory.spawnColumn(entityManager, pipeSpawnX, gapCentreY, config.gapSize, screenH);

        // Spawn collectibles in open space BETWEEN last pipe and current pipe
        if (lastPipeSpawnX != Float.MIN_VALUE) {
            float midX = lastPipeSpawnX + (pipeSpawnX - lastPipeSpawnX) / 2f;
            float minY = COLLECTIBLE_MARGIN + 50f;
            float maxY = screenH - COLLECTIBLE_MARGIN - 50f;

            int goodCount = entityManager.getByTag(Tags.COLLECTIBLE_GOOD).size();
            int badCount  = entityManager.getByTag(Tags.COLLECTIBLE_BAD).size();

            if (goodCount < MAX_GOOD && random.chance(config.collectibleSpawnChance)) {
                collectibleFactory.spawnGood(entityManager, random, midX, random.range(minY, maxY));
            }
            if (badCount < MAX_BAD && random.chance(config.collectibleSpawnChance)) {
                collectibleFactory.spawnBad(entityManager, random, midX, random.range(minY, maxY));
            }
        }

        lastPipeSpawnX = pipeSpawnX;
    }
}