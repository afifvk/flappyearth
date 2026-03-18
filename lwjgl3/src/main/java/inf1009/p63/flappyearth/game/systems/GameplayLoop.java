package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;

import java.util.ArrayList;
import java.util.List;

public class GameplayLoop {

    private final List<LoopStep> steps = new ArrayList<>();

    public void addStep(LoopStep step) {
        steps.add(step);
    }

    public void update(float delta) {
        for (LoopStep step : steps) {
            step.execute(delta);
        }
    }
}
