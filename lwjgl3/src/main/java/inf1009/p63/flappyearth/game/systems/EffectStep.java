package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.TimeManager;
import inf1009.p63.flappyearth.game.state.ActiveEffects;

public class EffectStep implements LoopStep {

    private final ActiveEffects activeEffects;
    private final TimeManager   timeManager;

    public EffectStep(ActiveEffects activeEffects,
                             TimeManager timeManager) {
        this.activeEffects = activeEffects;
        this.timeManager   = timeManager;
    }

    @Override
    public void execute(float delta) {
        activeEffects.update(delta);
        timeManager.resetSlowFactor();
    }
}
