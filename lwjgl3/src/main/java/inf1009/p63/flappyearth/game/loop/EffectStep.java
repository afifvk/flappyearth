package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.TimeManager;
import inf1009.p63.flappyearth.game.state.ActiveEffects;

public class EffectStep implements StepManager {

    private final ActiveEffects activeEffects;
    private final TimeManager   timeManager;

    public EffectStep(ActiveEffects activeEffects,
                             TimeManager timeManager) {
        this.activeEffects = activeEffects;
        this.timeManager   = timeManager;
    }

    @Override
    public void execute(float delta) {
        // Update active effect timers
        activeEffects.update(delta);

        // Slow-motion effect modifies game speed
        if (activeEffects.isSlowTimeActive()) {
            timeManager.setSlowFactor(0.4f);
        } else {
            timeManager.resetSlowFactor();
        }
    }
}
