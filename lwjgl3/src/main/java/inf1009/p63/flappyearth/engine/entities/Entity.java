package inf1009.p63.flappyearth.engine.entities;

import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
    private final Rectangle bounds;
    private String tag;

    public Entity(float x, float y, float width, float height) {
        this(x, y, width, height, null);
    }

    public Entity(float x, float y, float width, float height, String tag) {
        this.bounds = new Rectangle(x, y, width, height);
        this.tag = tag;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getX() { return bounds.x; }
    public float getY() { return bounds.y; }
    public float getWidth() { return bounds.width; }
    public float getHeight() { return bounds.height; }
}
