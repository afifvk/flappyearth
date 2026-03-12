package inf1009.p63.flappyearth.game.config;

public class GameplayDimensions {

    private final float worldWidth;
    private final float worldHeight;
    private final float cameraFollowLeadRatio;
    private final float cameraRightEdgeRatio;
    private final float pipeSpawnLead;
    private final float verticalPadding;
    private final float collectibleMargin;
    private final float collectibleSpawnAheadMin;
    private final float collectibleSpawnAheadMax;
    private final float despawnCleanupMargin;
    private final float endingTargetHeightRatio;
    private final float endingFlapWindow;

    public GameplayDimensions(float worldWidth,
                              float worldHeight,
                              float cameraFollowLeadRatio,
                              float cameraRightEdgeRatio,
                              float pipeSpawnLead,
                              float verticalPadding,
                              float collectibleMargin,
                              float collectibleSpawnAheadMin,
                              float collectibleSpawnAheadMax,
                              float despawnCleanupMargin,
                              float endingTargetHeightRatio,
                              float endingFlapWindow) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.cameraFollowLeadRatio = cameraFollowLeadRatio;
        this.cameraRightEdgeRatio = cameraRightEdgeRatio;
        this.pipeSpawnLead = pipeSpawnLead;
        this.verticalPadding = verticalPadding;
        this.collectibleMargin = collectibleMargin;
        this.collectibleSpawnAheadMin = collectibleSpawnAheadMin;
        this.collectibleSpawnAheadMax = collectibleSpawnAheadMax;
        this.despawnCleanupMargin = despawnCleanupMargin;
        this.endingTargetHeightRatio = endingTargetHeightRatio;
        this.endingFlapWindow = endingFlapWindow;
    }

    public static GameplayDimensions fromDisplaySettings(DisplaySettings displaySettings) {
        return new GameplayDimensions(
                displaySettings.getReferenceWorldWidth(),
                displaySettings.getReferenceWorldHeight(),
                0.25f,
                0.75f,
                40f,
                50f,
                20f,
                100f,
                220f,
                200f,
                0.55f,
                24f);
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public float getCameraFollowLeadRatio() {
        return cameraFollowLeadRatio;
    }

    public float getCameraRightEdgeRatio() {
        return cameraRightEdgeRatio;
    }

    public float getPipeSpawnLead() {
        return pipeSpawnLead;
    }

    public float getVerticalPadding() {
        return verticalPadding;
    }

    public float getCollectibleMargin() {
        return collectibleMargin;
    }

    public float getCollectibleSpawnAheadMin() {
        return collectibleSpawnAheadMin;
    }

    public float getCollectibleSpawnAheadMax() {
        return collectibleSpawnAheadMax;
    }

    public float getDespawnCleanupMargin() {
        return despawnCleanupMargin;
    }

    public float getEndingTargetHeightRatio() {
        return endingTargetHeightRatio;
    }

    public float getEndingFlapWindow() {
        return endingFlapWindow;
    }
}