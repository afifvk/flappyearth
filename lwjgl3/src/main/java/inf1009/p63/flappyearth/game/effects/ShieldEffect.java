package inf1009.p63.flappyearth.game.effects;

import inf1009.p63.flappyearth.game.state.ActiveEffects;

public class ShieldEffect implements GameEffect {

    private final float duration;

    public ShieldEffect(float duration) {
        this.duration = duration;
    }

    @Override
    public void apply(ActiveEffects effects) {
        effects.activateShield(duration);
    }
}
