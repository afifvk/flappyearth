package inf1009.p63.flappyearth.game.scenes;

public class StageTemplate {

    private final String sceneId;
    private final String title;
    private final String subtitle;
    private final float clearR;
    private final float clearG;
    private final float clearB;

    public StageTemplate(String sceneId,
                         String title,
                         String subtitle,
                         float clearR,
                         float clearG,
                         float clearB) {
        this.sceneId = sceneId;
        this.title = title;
        this.subtitle = subtitle;
        this.clearR = clearR;
        this.clearG = clearG;
        this.clearB = clearB;
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
}
