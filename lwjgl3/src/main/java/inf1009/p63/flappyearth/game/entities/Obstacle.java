package inf1009.p63.flappyearth.game.entities;

import com.badlogic.gdx.math.Rectangle;
import inf1009.p63.flappyearth.game.config.Tags;

public abstract class Obstacle extends GameEntity {

    public enum ObstacleType {
        PIPE
    }

    private final ObstacleType obstacleType;
    private final String        factTopic;
    private boolean             passed = false;
    private boolean             isFlipped = false;

    // Crash animation state
    private boolean crashActive = false;
    private float   crashTimer = 0f;
    private float   crashRotationDegrees = 0f;
    private float   crashVelX = 0f;
    private float   crashVelY = 0f;

    public Obstacle(float x, float y, float width, float height,
                       String assetKey, ObstacleType type, String factTopic) {
        super(x, y, width, height, assetKey, Tags.HAZARD);
        this.obstacleType = type;
        this.factTopic    = factTopic;
    }

    @Override
    public void update(float delta) {
        if (!crashActive) return;

        // Apply simple physics while crashing
        crashVelY -= 800f * delta; // gravity downwards
        Rectangle b = getBounds();
        b.x += crashVelX * delta;
        b.y += crashVelY * delta;

        crashRotationDegrees += 360f * delta;

        crashTimer = Math.max(0f, crashTimer - delta);
    }

    public void startCrash(float initialVelX, float initialVelY, float duration) {
        if (crashActive) return;
        crashActive = true;
        crashTimer = duration;
        this.crashVelX = initialVelX;
        this.crashVelY = initialVelY;
    }

    public boolean isCrashActive() { return crashActive; }
    public float getCrashRotation() { return crashRotationDegrees; }

    public ObstacleType getObstacleType() { return obstacleType; }
    public String       getFactTopic()    { return factTopic; }
    public boolean      isPassed()        { return passed; }
    public void         setPassed(boolean passed) { this.passed = passed; }
    public boolean      isFlipped()       { return isFlipped; }
    public void         setFlipped(boolean flipped) { this.isFlipped = flipped; }

    @Override
    public inf1009.p63.flappyearth.engine.entities.RenderData getRenderData() {
        Rectangle b = getBounds();
        if (crashActive) {
            return new inf1009.p63.flappyearth.engine.entities.RenderData(
                    getAssetKey(), b.x, b.y, b.width, b.height,
                    getColorR(), getColorG(), getColorB(), isFlipped(), crashRotationDegrees);
        }
        return super.getRenderData();
    }
}
