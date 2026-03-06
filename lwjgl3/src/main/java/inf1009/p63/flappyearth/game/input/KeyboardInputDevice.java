package inf1009.p63.flappyearth.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf1009.p63.flappyearth.engine.interfaces.InputDevice;

import java.util.HashMap;
import java.util.Map;

public class KeyboardInputDevice implements InputDevice {

    private final Map<String, Integer> keyBindings = new HashMap<>();

    public KeyboardInputDevice() {
        keyBindings.put(GameInputAction.FLAP.id(), Input.Keys.SPACE);
    }

    @Override
    public boolean isActionJustPressed(String actionId) {
        Integer keycode = keyBindings.get(actionId);
        return keycode != null && Gdx.input.isKeyJustPressed(keycode);
    }

    @Override
    public boolean isActionPressed(String actionId) {
        Integer keycode = keyBindings.get(actionId);
        return keycode != null && Gdx.input.isKeyPressed(keycode);
    }
}
