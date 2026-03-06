package inf1009.p63.flappyearth.game.input;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.engine.interfaces.InputDevice;

public class TouchInputDevice implements InputDevice {

    @Override
    public boolean isActionJustPressed(String actionId) {
        return GameInputAction.FLAP.id().equals(actionId) && Gdx.input.justTouched();
    }

    @Override
    public boolean isActionPressed(String actionId) {
        return GameInputAction.FLAP.id().equals(actionId) && Gdx.input.isTouched();
    }
}
