package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.math.MathUtils;

public class RandomManager {

    public float range(float min, float max) {
        return MathUtils.random(min, max);
    }

    public int range(int min, int max) {
        return MathUtils.random(min, max);
    }

    public boolean chance(float probability) {
        return MathUtils.random() < probability;
    }

    public <T> T pick(T[] array) {
        return array[MathUtils.random(array.length - 1)];
    }
}
