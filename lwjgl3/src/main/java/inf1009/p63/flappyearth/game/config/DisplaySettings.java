package inf1009.p63.flappyearth.game.config;

public class DisplaySettings {

    private final String title;
    private final int targetFps;
    private final boolean vSync;
    private final boolean resizable;
    private final int launchWidth;
    private final int launchHeight;
    private final boolean startMaximized;
    private final float referenceWorldWidth;
    private final float referenceWorldHeight;

    public DisplaySettings(String title,
                           int targetFps,
                           boolean vSync,
                           boolean resizable,
                           int launchWidth,
                           int launchHeight,
                           boolean startMaximized,
                           float referenceWorldWidth,
                           float referenceWorldHeight) {
        this.title = title;
        this.targetFps = targetFps;
        this.vSync = vSync;
        this.resizable = resizable;
        this.launchWidth = launchWidth;
        this.launchHeight = launchHeight;
        this.startMaximized = startMaximized;
        this.referenceWorldWidth = referenceWorldWidth;
        this.referenceWorldHeight = referenceWorldHeight;
    }

    public String getTitle() {
        return title;
    }

    public int getTargetFps() {
        return targetFps;
    }

    public boolean isVSync() {
        return vSync;
    }

    public boolean isResizable() {
        return resizable;
    }

    public int getLaunchWidth() {
        return launchWidth;
    }

    public int getLaunchHeight() {
        return launchHeight;
    }

    public boolean isStartMaximized() {
        return startMaximized;
    }

    public float getReferenceWorldWidth() {
        return referenceWorldWidth;
    }

    public float getReferenceWorldHeight() {
        return referenceWorldHeight;
    }
}