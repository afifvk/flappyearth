package inf1009.p63.flappyearth.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import inf1009.p63.flappyearth.engine.core.GameMaster;
import inf1009.p63.flappyearth.game.core.FlappyEarthSetup;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static void createApplication() {
        new Lwjgl3Application(new GameMaster(new FlappyEarthSetup()), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("FlappyEarth");
        // VSync and frame rate settings
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setWindowedMode(640, 480);
        config.setMaximized(true);
        return config;
    }
}
