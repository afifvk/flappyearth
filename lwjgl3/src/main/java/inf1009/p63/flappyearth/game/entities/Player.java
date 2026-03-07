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

    // Animation
    private static final String[] BIRD_FRAMES = {
        "flappy00.png", "flappy01.png", "flappy02.png",
        "flappy03.png", "flappy04.png", "flappy05.png"
    };
    private static final float FRAME_DURATION = 0.1f;  // 100ms per frame
    private int currentFrame = 0;
    private float animationTimer = 0f;

    public Player(float x, float y, float velX, float gravity, float jumpImpulse) {
        super(x, y, 60, 45, BIRD_FRAMES[0], Tags.PLAYER);
        this.velX        = velX;
        this.velY        = 0f;
        this.gravity     = gravity;
        this.jumpImpulse = jumpImpulse;
    }

    @Override
    public void update(float delta) {
        // Apply gravity each frame
        velY += gravity * delta;
        
        // Update animation frame
        animationTimer += delta;
        if (animationTimer >= FRAME_DURATION) {
            animationTimer = 0f;
            currentFrame = (currentFrame + 1) % BIRD_FRAMES.length;
        }
    }

    @Override
    public RenderData getRenderData() {
        // Return current animation frame
        Rectangle b = getBounds();
        return new RenderData(BIRD_FRAMES[currentFrame], b.x, b.y, b.width, b.height);
    }

    @Override
    public void movement(float delta) {
        // Move based on velocity
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

    // Jump by resetting vertical velocity
    public void flap() {
        velY = jumpImpulse;
    }

    public boolean hasPassed()       { return passed; }
    public void    setPassed(boolean passed) { this.passed = passed; }
}