package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.EventBus;
import inf1009.p63.flappyearth.engine.services.InputManager;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.input.GameInputAction;
import inf1009.p63.flappyearth.game.state.GameState;

public class InputStep implements LoopStep {

    private final InputManager input;
    private final EventBus       eventManager;
    private final GameState   state;

    public InputStep(InputManager input, EventBus eventManager,
                            GameState state) {
        this.input        = input;
        this.eventManager = eventManager;
        this.state        = state;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;
        if (!state.isControlsEnabled()) return;

        if (input.isActionJustPressed(GameInputAction.FLAP.id())) {
            eventManager.publish(GameEvents.FLAP_REQUESTED, null);
        }
    }
}
