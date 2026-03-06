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
        // Game difficulty and spawn parameters
        this.levelName              = levelName;
        this.gravity                = gravity;
        this.jumpImpulse            = jumpImpulse;
        this.playerSpeed            = playerSpeed;
        this.obstacleSpawnInterval  = obstacleSpawnInterval;
        this.collectibleSpawnChance = collectibleSpawnChance;
        this.gapSize                = gapSize;
    }

    // Default difficulty settings
    public static GameConfig defaultConfig() {
        return new GameConfig(
                "Default", -562.5f, 330f, 156f, 3.6f, 0.4f, 180f);
    }
}
