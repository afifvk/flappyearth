package inf1009.p63.flappyearth.engine.managers;

public class TimeManager {

    private float totalElapsed;
    private float slowFactor = 1f;

    public void update(float delta) {
        totalElapsed += delta * slowFactor;
    }

    public float getElapsed() {
        return totalElapsed;
    }

    public float scale(float delta) {
        // Apply slow-motion factor to delta time
        return delta * slowFactor;
    }

    // Clamp and set time scale (0.0 to 1.0)
    public void setSlowFactor(float factor) {
        this.slowFactor = Math.max(0f, Math.min(1f, factor));
    }

    public float getSlowFactor() {
        return slowFactor;
    }

    // Return to normal speed
    public void resetSlowFactor() {
        slowFactor = 1f;
    }

    public void reset() {
        totalElapsed = 0f;
        slowFactor = 1f;
    }
}
