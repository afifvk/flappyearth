package inf1009.p63.flappyearth.game.loop;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.engine.entities.Entity;
import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.RandomManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.factories.CollectibleFactory;
import inf1009.p63.flappyearth.game.factories.ObstacleFactory;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.List;

public class SpawnStep implements StepManager {

    private static final float PIPE_WIDTH = 37.5f;
    private static final float PIPE_LANE_CLEARANCE_X = 40f;
    private static final float COLLECTIBLE_SIZE_ESTIMATE = 30f;
    private static final float COLLECTIBLE_MARGIN_Y = 30f;
    private static final int COLLECTIBLE_SPAWN_ATTEMPTS = 24;

    private final EntityManager              entityManager;
    private final ObstacleFactory     obstacleFactory;
    private final CollectibleFactory  collectibleFactory;
    private final RandomManager              random;
    private final GameState           state;
    private final GameConfig                 config;

    private float spawnTimer = 0f;

    public SpawnStep(EntityManager entityManager,
                            ObstacleFactory obstacleFactory,
                            CollectibleFactory collectibleFactory,
                            RandomManager random, GameState state,
                            GameConfig config) {
        this.entityManager     = entityManager;
        this.obstacleFactory   = obstacleFactory;
        this.collectibleFactory = collectibleFactory;
        this.random            = random;
        this.state             = state;
        this.config            = config;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        // Track spawn timing
        spawnTimer += delta;
        if (spawnTimer < config.obstacleSpawnInterval) return;
        spawnTimer = 0f;

        float screenH  = Gdx.graphics.getHeight();
        float screenW  = Gdx.graphics.getWidth();
        
        // Get player from EntityManager using tag
        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;
        
        float playerX  = player.getBounds().x;
        // Spawn ahead of player so they can see it coming
        float spawnX   = playerX + screenW + 80f;

        // Random gap position (can be anywhere vertically, with margin for gap size)
        float minGapY = config.gapSize / 2f + 50f;
        float maxGapY = screenH - config.gapSize / 2f - 50f;
        float gapCentreY = random.range(minGapY, maxGapY);
        float gapLowerY = gapCentreY - config.gapSize / 2f;
        float gapUpperY = gapCentreY + config.gapSize / 2f;

        obstacleFactory.spawnColumn(entityManager, spawnX, gapCentreY, config.gapSize, screenH);

        // Spawn collectibles in free lanes, not inside pipe-column lanes.
        spawnCollectibles(screenW, screenH, spawnX);
    }

    private void spawnCollectibles(float screenW, float screenH, float upcomingPipeX) {
        // Count active collectibles by tag
        List<Entity> goods = entityManager.getByTag(Tags.COLLECTIBLE_GOOD);
        List<Entity> bads = entityManager.getByTag(Tags.COLLECTIBLE_BAD);
        
        int goodCount = goods != null ? goods.size() : 0;
        int badCount = bads != null ? bads.size() : 0;
        
        // Get player position to spawn ahead (like obstacles)
        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;
        
        float playerX = player.getBounds().x;
        // Search in the visible-forward region between player and next obstacle spawn lane.
        float minSpawnX = playerX + screenW * 0.45f;
        float maxSpawnX = playerX + screenW - 40f;
        if (maxSpawnX <= minSpawnX) {
            maxSpawnX = minSpawnX + 120f;
        }
        
        // Decide what to spawn: 0=nothing, 1=only good, 2=only bad, 3=both
        int spawnDecision = 0;
        
        // Random chance to spawn something (reduced from 0.4 to 0.2 for less frequency)
        if (random.chance(config.collectibleSpawnChance)) {
            if (goodCount == 0 && badCount == 0) {
                spawnDecision = random.range(1, 2);  // 1=good, 2=bad
            } else if (goodCount == 0) {
                spawnDecision = 1;  // Spawn good only
            } else if (badCount == 0) {
                spawnDecision = 2;  // Spawn bad only
            }
        }

        float minSpawnY = COLLECTIBLE_MARGIN_Y;
        float maxSpawnY = screenH - COLLECTIBLE_MARGIN_Y - COLLECTIBLE_SIZE_ESTIMATE;
        if (maxSpawnY <= minSpawnY) {
            return;
        }

        List<Entity> hazards = entityManager.getByTag(Tags.HAZARD);

        float spawnX = 0f;
        float spawnY = 0f;
        boolean foundFreeSlot = false;
        for (int i = 0; i < COLLECTIBLE_SPAWN_ATTEMPTS; i++) {
            float testX = random.range(minSpawnX, maxSpawnX);
            float testY = random.range(minSpawnY, maxSpawnY);
            if (isFreeCollectibleLane(testX, hazards, upcomingPipeX)) {
                spawnX = testX;
                spawnY = testY;
                foundFreeSlot = true;
                break;
            }
        }

        if (!foundFreeSlot) return;
        
        // Spawn good collectible if slot available and decided
        if ((spawnDecision == 1) && goodCount == 0) {
            collectibleFactory.spawnGood(entityManager, random, spawnX, spawnY);
        }
        
        // Spawn bad collectible if slot available and decided
        if ((spawnDecision == 2) && badCount == 0) {
            collectibleFactory.spawnBad(entityManager, random, spawnX, spawnY);
        }
    }

    private boolean isFreeCollectibleLane(float collectibleX, List<Entity> hazards, float upcomingPipeX) {
        float upcomingPipeLaneX = upcomingPipeX - PIPE_LANE_CLEARANCE_X;
        float upcomingPipeLaneW = PIPE_WIDTH + PIPE_LANE_CLEARANCE_X * 2f;
        if (overlapsHorizontally(collectibleX, COLLECTIBLE_SIZE_ESTIMATE,
                                 upcomingPipeLaneX, upcomingPipeLaneW)) {
            return false;
        }

        for (Entity hazard : hazards) {
            float laneX = hazard.getBounds().x - PIPE_LANE_CLEARANCE_X;
            float laneW = hazard.getBounds().width + PIPE_LANE_CLEARANCE_X * 2f;
            if (overlapsHorizontally(collectibleX, COLLECTIBLE_SIZE_ESTIMATE, laneX, laneW)) {
                return false;
            }
        }
        return true;
    }

    private boolean overlapsHorizontally(float x1, float w1, float x2, float w2) {
        return x1 < x2 + w2 && x1 + w1 > x2;
    }
}
