package inf1009.p63.flappyearth.game.scenes;

public class StageConfig {

    private final String sceneId;
    private final String title;
    private final String subtitle;
    private final float clearR;
    private final float clearG;
    private final float clearB;
    private final float smokeOverlayAlpha;
    private final float playerSpeedMultiplier;

    public StageConfig(String sceneId,
                       String title,
                       String subtitle,
                       float clearR,
                       float clearG,
                       float clearB,
                       float smokeOverlayAlpha,
                       float playerSpeedMultiplier) {
        this.sceneId = sceneId;
        this.title = title;
        this.subtitle = subtitle;
        this.clearR = clearR;
        this.clearG = clearG;
        this.clearB = clearB;
        this.smokeOverlayAlpha = Math.max(0f, Math.min(1f, smokeOverlayAlpha));
        this.playerSpeedMultiplier = Math.max(0f, playerSpeedMultiplier);
    }

    public String getSceneId() {
        return sceneId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public float getClearR() {
        return clearR;
    }

    public float getClearG() {
        return clearG;
    }

    public float getClearB() {
        return clearB;
    }

    public float getSmokeOverlayAlpha() {
        return smokeOverlayAlpha;
    }

    public float getPlayerSpeedMultiplier() {
        return playerSpeedMultiplier;
    }
}
