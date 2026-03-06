package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;

import java.util.ArrayList;
import java.util.List;

public class GameLoop {

    private final List<StepManager> steps = new ArrayList<>();

    public void addStep(StepManager step) {
        steps.add(step);
    }

    // Execute all game steps in order
    public void update(float delta) {
        for (StepManager step : steps) {
            step.execute(delta);
        }
    }
}
