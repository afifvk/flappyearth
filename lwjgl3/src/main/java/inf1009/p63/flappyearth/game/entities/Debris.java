package inf1009.p63.flappyearth.game.entities;

import inf1009.p63.flappyearth.engine.model.RenderData;
import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.engine.services.EntityStore;

public class Debris extends GameEntity implements Movable {

    private float vx;
    private float vy;
    private float life;
    private final EntityStore entityManager;

    public Debris(float x, float y, float w, float h, float vx, float vy, float life, EntityStore em) {
        super(x, y, w, h, null);
        this.vx = vx; this.vy = vy; this.life = life;
        this.entityManager = em;
        // small default color
        setColor(0.6f, 0.8f, 0.6f);
    }

    @Override
    public void update(float delta) {
        // simple physics
        vy -= 800f * delta; // gravity
        getBounds().x += vx * delta;
        getBounds().y += vy * delta;

        life -= delta;
        if (life <= 0f) {
            entityManager.queueRemove(this);
        }
    }

    @Override
    public void movement(float delta) {
        // movement handled in update()
    }

    @Override
    public void setVelocity(float vx, float vy) { this.vx = vx; this.vy = vy; }

    @Override
    public float getVelocityX() { return vx; }

    @Override
    public float getVelocityY() { return vy; }

    @Override
    public RenderData getRenderData() {
        return new RenderData("", getBounds().x, getBounds().y, getBounds().width, getBounds().height,
                getColorR(), getColorG(), getColorB());
    }
}
