package inf1009.p63.flappyearth.game.config;

public class GameSettings {

    private static final float MIN_BRIGHTNESS = 0.4f;
    private static final float MAX_BRIGHTNESS = 1.6f;
    private static final float MIN_VOLUME = 0f;
    private static final float MAX_VOLUME = 1f;

    private float brightness = 1.0f;
    private float masterVolume = 1.0f;

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = clamp(brightness, MIN_BRIGHTNESS, MAX_BRIGHTNESS);
    }

    public void increaseBrightness(float amount) {
        setBrightness(brightness + amount);
    }

    public void decreaseBrightness(float amount) {
        setBrightness(brightness - amount);
    }

    public float getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = clamp(masterVolume, MIN_VOLUME, MAX_VOLUME);
    }

    public void increaseVolume(float amount) {
        setMasterVolume(masterVolume + amount);
    }

    public void decreaseVolume(float amount) {
        setMasterVolume(masterVolume - amount);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
