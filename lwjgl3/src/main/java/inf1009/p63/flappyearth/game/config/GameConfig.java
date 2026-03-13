package inf1009.p63.flappyearth.game.config;

public class GameConfig {

    public final float gravity;
    public final float jumpImpulse;
    public final float playerSpeed;

    public final float obstacleSpawnInterval;
    public final float collectibleSpawnChance;
    public final float gapSize;

    public final String levelName;

    public GameConfig(String levelName,
                      float gravity, float jumpImpulse, float playerSpeed,
                      float obstacleSpawnInterval, float collectibleSpawnChance,
                      float gapSize) {
        this.levelName              = levelName;
        this.gravity                = gravity;
        this.jumpImpulse            = jumpImpulse;
        this.playerSpeed            = playerSpeed;
        this.obstacleSpawnInterval  = obstacleSpawnInterval;
        this.collectibleSpawnChance = collectibleSpawnChance;
        this.gapSize                = gapSize;
    }

    public static GameConfig defaultConfig() {
        return new GameConfig(
                "Default", -562.5f, 330f, 156f, 3.6f, 0.4f, 180f);
    }

    public GameConfig withPlayerSpeedMultiplier(float speedMultiplier) {
        return new GameConfig(
                levelName,
                gravity,
                jumpImpulse,
                playerSpeed * speedMultiplier,
                obstacleSpawnInterval,
                collectibleSpawnChance,
                gapSize);
    }
}
