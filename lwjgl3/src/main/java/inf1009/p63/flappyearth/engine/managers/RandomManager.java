package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.math.MathUtils;

public class RandomManager {

    // Random float in range [min, max]
    public float range(float min, float max) {
        return MathUtils.random(min, max);
    }

    // Random int in range [min, max]
    public int range(int min, int max) {
        return MathUtils.random(min, max);
    }

    // True/false based on probability 0-1
    public boolean chance(float probability) {
        return MathUtils.random() < probability;
    }

    // Pick random element from array
    public <T> T pick(T[] array) {
        return array[MathUtils.random(array.length - 1)];
    }
}
