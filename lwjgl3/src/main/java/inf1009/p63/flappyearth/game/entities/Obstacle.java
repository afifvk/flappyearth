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

        // Apply simple physics while crashing: fall straight down (no spin)
        crashVelY -= 800f * delta; // gravity accelerates downward velocity
        Rectangle b = getBounds();
        b.x += crashVelX * delta; // crashVelX can be small or zero
        b.y += crashVelY * delta; // negative vy falls down

        crashTimer = Math.max(0f, crashTimer - delta);
    }

    public void startCrash(float initialVelX, float initialVelY, float duration) {
        if (crashActive) return;
        crashActive = true;
        crashTimer = duration;
        // Force no spin: horizontal velocity optional, but keep vertical towards falling
        this.crashVelX = 0f; // fall straight down by default
        this.crashVelY = initialVelY < 0f ? initialVelY : -150f; // ensure downward
    }

    public boolean isCrashActive() { return crashActive; }

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
            // No spin during crash — render with default rotation (0f)
            return new inf1009.p63.flappyearth.engine.entities.RenderData(
                getAssetKey(), b.x, b.y, b.width, b.height,
                getColorR(), getColorG(), getColorB(), isFlipped());
        }
        return super.getRenderData();
    }
}
