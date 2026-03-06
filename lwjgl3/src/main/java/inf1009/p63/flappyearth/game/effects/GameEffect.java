package inf1009.p63.flappyearth.game.effects;

import inf1009.p63.flappyearth.game.state.ActiveEffects;

public interface GameEffect {
    // Apply effect when collectible is collected (modifies only ActiveEffects, not score)
    void apply(ActiveEffects effects);
}
