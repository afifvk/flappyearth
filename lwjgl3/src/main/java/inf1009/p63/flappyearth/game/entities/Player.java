package inf1009.p63.flappyearth.game.entities;

import com.badlogic.gdx.math.Rectangle;

import inf1009.p63.flappyearth.engine.entities.RenderData;
import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.game.config.Tags;

public class Player extends GameEntity implements Movable {

    private float velX;
    private float velY;

    private final float gravity;
    private final float jumpImpulse;

    private boolean passed = false;

    private static final String[] BIRD_FRAMES = {
        "flappy00.png", "flappy01.png", "flappy02.png",
        "flappy03.png", "flappy04.png", "flappy05.png"
    };
    private static final float FRAME_DURATION = 0.1f;
    private int currentFrame = 0;
    private float animationTimer = 0f;

    private boolean deathFallActive = false;
    private float rotationDegrees = 0f;
    private float deathFallSpeedMultiplier = 1f;
    private static final float DEATH_ROTATION_SPEED = 360f;
    private static final float DEATH_FALL_SPEED = 361f;
    private static final float MAX_UPWARD_TILT = 25f;
    private static final float MAX_DOWNWARD_TILT = -55f;
    private static final float TILT_VELOCITY_FACTOR = 0.12f;

    private float flickerTimer = 0f;
    private static final float FLICKER_FREQUENCY = 20f; // flashes per second

    public Player(float x, float y, float velX, float gravity, float jumpImpulse) {
        super(x, y, 60, 45, BIRD_FRAMES[0], Tags.PLAYER);
        this.velX        = velX;
        this.velY        = 0f;
        this.gravity     = gravity;
        this.jumpImpulse = jumpImpulse;
    }

    @Override
    public void update(float delta) {
        if (deathFallActive) {
            rotationDegrees = Math.max(-90f, rotationDegrees - (DEATH_ROTATION_SPEED * delta));
            return;
        }

        velY += gravity * delta;
        rotationDegrees = Math.max(MAX_DOWNWARD_TILT,
            Math.min(MAX_UPWARD_TILT, velY * TILT_VELOCITY_FACTOR));

        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0f;
            currentFrame = (currentFrame + 1) % BIRD_FRAMES.length;
        }

        if (flickerTimer > 0f) {
            flickerTimer = Math.max(0f, flickerTimer - delta);
        }
    }

    @Override
    public RenderData getRenderData() {
        Rectangle b = getBounds();
        float r = 1f, g = 1f, bl = 1f;
        if (flickerTimer > 0f) {
            float phase = (float) Math.floor(flickerTimer * FLICKER_FREQUENCY);
            if (((int) phase) % 2 == 0) {
                r = g = bl = 0.25f;
            }
        }
        return new RenderData(BIRD_FRAMES[currentFrame], b.x, b.y, b.width, b.height,
                r, g, bl, false, rotationDegrees);
    }

    @Override
    public void movement(float delta) {
        if (deathFallActive) {
            Rectangle b = getBounds();
            b.y -= (DEATH_FALL_SPEED * deathFallSpeedMultiplier) * delta;
            return;
        }

        Rectangle b = getBounds();
        b.x += velX * delta;
        b.y += velY * delta;
    }

    @Override
    public void setVelocity(float vx, float vy) {
        this.velX = vx;
        this.velY = vy;
    }

    @Override public float getVelocityX() { return velX; }
    @Override public float getVelocityY() { return velY; }

    public void flap() {
        if (deathFallActive) return;
        velY = jumpImpulse;
    }

    public void startDeathFall(float speedMultiplier) {
        deathFallActive = true;
        deathFallSpeedMultiplier = speedMultiplier;
        velX = 0f;
        velY = 0f;
    }

    public void flicker(float duration) {
        flickerTimer = Math.max(flickerTimer, duration);
    }

    public boolean isDeathFallActive() {
        return deathFallActive;
    }

    public boolean hasPassed()       { return passed; }
    public void    setPassed(boolean passed) { this.passed = passed; }
}