package inf1009.p63.flappyearth.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import inf1009.p63.flappyearth.engine.core.GameMaster;
import inf1009.p63.flappyearth.game.config.DisplaySettings;
import inf1009.p63.flappyearth.game.config.DisplaySettingsFactory;
import inf1009.p63.flappyearth.game.setup.FlappyEarthSetup;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }
    private static void createApplication() {
        DisplaySettings settings = DisplaySettingsFactory.createFromRuntime();
        new Lwjgl3Application(new GameMaster(new FlappyEarthSetup(settings)), getDefaultConfiguration(settings));
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration(DisplaySettings settings) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(settings.getTargetFps());
        config.setTitle(settings.getTitle());
        config.useVsync(settings.isVSync());
        config.setResizable(settings.isResizable());
        config.setWindowedMode(settings.getLaunchWidth(), settings.getLaunchHeight());
        if (settings.isStartMaximized()) {
            config.setMaximized(true);
        }
        return config;
    }
}
