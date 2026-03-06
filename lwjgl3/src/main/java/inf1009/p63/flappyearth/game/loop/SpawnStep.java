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

        // Spawn collectibles: max 1 good + 1 bad at a time, inside the current pipe gap
        spawnCollectibles(screenW, screenH, gapLowerY, gapUpperY);
    }

    private void spawnCollectibles(float screenW, float screenH, float gapLowerY, float gapUpperY) {
        // Count active collectibles by tag
        List<Entity> goods = entityManager.getByTag(Tags.COLLECTIBLE_GOOD);
        List<Entity> bads = entityManager.getByTag(Tags.COLLECTIBLE_BAD);
        
        int goodCount = goods != null ? goods.size() : 0;
        int badCount = bads != null ? bads.size() : 0;
        
        // Get player position to spawn ahead (like obstacles)
        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;
        
        float playerX = player.getBounds().x;
        // Spawn at right side (ahead of player) so they move left into view
        float spawnX = playerX + screenW + 80f;
        
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

        // Spawn inside the current pipe gap so collectibles never overlap pipe bodies
        float collectibleMargin = 20f;
        float gapSpawnMinY = gapLowerY + collectibleMargin;
        float gapSpawnMaxY = gapUpperY - collectibleMargin;

        float y;
        if (gapSpawnMaxY > gapSpawnMinY) {
            y = random.range(gapSpawnMinY, gapSpawnMaxY);
        } else {
            // Fallback to gap center if margins leave no space
            y = (gapLowerY + gapUpperY) / 2f;
        }
        
        // Spawn good collectible if slot available and decided
        if ((spawnDecision == 1) && goodCount == 0) {
            collectibleFactory.spawnGood(entityManager, random, spawnX, y);
        }
        
        // Spawn bad collectible if slot available and decided
        if ((spawnDecision == 2) && badCount == 0) {
            collectibleFactory.spawnBad(entityManager, random, spawnX, y);
        }
    }
}
