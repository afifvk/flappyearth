package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.math.Rectangle;
import inf1009.p63.flappyearth.engine.interfaces.Collidable;

import java.util.List;

public class CollisionManager {

    public Collidable checkFirst(Collidable a, List<? extends Collidable> others) {
        // Get first collision hit
        Rectangle boundsA = a.getBounds();
        for (Collidable other : others) {
            if (boundsA.overlaps(other.getBounds())) {
                return other;
            }
        }
        return null;
    }

    public boolean overlapsAny(Collidable a, List<? extends Collidable> others) {
        // Check if any overlap exists
        return checkFirst(a, others) != null;
    }

    // Rectangle-based collision test
    public boolean overlaps(Collidable a, Collidable b) {
        return a.getBounds().overlaps(b.getBounds());
    }
}