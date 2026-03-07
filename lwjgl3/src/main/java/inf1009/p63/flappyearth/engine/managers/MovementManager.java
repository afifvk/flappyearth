package inf1009.p63.flappyearth.engine.managers;

import inf1009.p63.flappyearth.engine.interfaces.Movable;

import java.util.List;

public class MovementManager {

    public void moveAll(List<Movable> movables, float delta) {
        for (Movable m : movables) {
            m.movement(delta);
        }
    }
}
