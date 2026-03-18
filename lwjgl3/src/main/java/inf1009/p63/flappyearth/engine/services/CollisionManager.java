package inf1009.p63.flappyearth.engine.services;

import com.badlogic.gdx.math.Rectangle;
import inf1009.p63.flappyearth.engine.interfaces.Collidable;

import java.util.List;

public class CollisionManager {

    private static final float HITBOX_SHRINK = 0.6f;

    public Collidable checkFirst(Collidable a, List<? extends Collidable> others) {
        Rectangle boundsA = shrink(a.getBounds());
        for (Collidable other : others) {
            if (boundsA.overlaps(shrink(other.getBounds()))) return other;
        }
        return null;
    }

    public boolean overlapsAny(Collidable a, List<? extends Collidable> others) {
        return checkFirst(a, others) != null;
    }

    public boolean overlaps(Collidable a, Collidable b) {
        return shrink(a.getBounds()).overlaps(shrink(b.getBounds()));
    }

    public boolean overlapsExact(Collidable a, Collidable b) {
        return a.getBounds().overlaps(b.getBounds());
    }

    public boolean containsPoint(Collidable collidable, float x, float y) {
        return collidable.getBounds().contains(x, y);
    }

    private Rectangle shrink(Rectangle r) {
        float newW = r.width  * HITBOX_SHRINK;
        float newH = r.height * HITBOX_SHRINK;
        return new Rectangle(
            r.x + (r.width  - newW) / 2f,
            r.y + (r.height - newH) / 2f,
            newW, newH
        );
    }
}