package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.engine.managers.InputOutputManager;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.input.GameInputAction;
import inf1009.p63.flappyearth.game.state.GameState;

public class InputStep implements StepManager {

    private final InputOutputManager input;
    private final EventManager       eventManager;
    private final GameState   state;

    public InputStep(InputOutputManager input, EventManager eventManager,
                            GameState state) {
        this.input        = input;
        this.eventManager = eventManager;
        this.state        = state;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        // Publish flap event when player presses button
        if (input.isActionJustPressed(GameInputAction.FLAP.id())) {
            eventManager.publish(GameEvents.FLAP_REQUESTED, null);
        }
    }
}
