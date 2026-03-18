package inf1009.p63.flappyearth.engine.model;

import com.badlogic.gdx.math.Rectangle;

public abstract class EntityBase {
    private final Rectangle bounds;
    private String tag;

    public EntityBase(float x, float y, float width, float height) {
        this(x, y, width, height, null);
    }

    public EntityBase(float x, float y, float width, float height, String tag) {
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
