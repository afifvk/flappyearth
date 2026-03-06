package inf1009.p63.flappyearth.engine.managers;

import inf1009.p63.flappyearth.engine.interfaces.InputDevice;

import java.util.ArrayList;
import java.util.List;

public class InputOutputManager {

    private final List<InputDevice> inputDevices = new ArrayList<>();

    public void registerInputDevice(InputDevice inputDevice) {
        if (inputDevice != null) {
            inputDevices.add(inputDevice);
        }
    }

    public void clearInputDevices() {
        inputDevices.clear();
    }

    public boolean isActionJustPressed(String actionId) {
        for (InputDevice inputDevice : inputDevices) {
            if (inputDevice.isActionJustPressed(actionId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActionPressed(String actionId) {
        for (InputDevice inputDevice : inputDevices) {
            if (inputDevice.isActionPressed(actionId)) {
                return true;
            }
        }
        return false;
    }
}
