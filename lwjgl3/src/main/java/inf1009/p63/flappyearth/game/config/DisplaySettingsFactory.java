package inf1009.p63.flappyearth.game.config;

public final class DisplaySettingsFactory {

	private static final String PROFILE_PROPERTY = "flappyearth.display.profile";
	private static final String PROFILE_ENV = "FLAPPYEARTH_DISPLAY_PROFILE";

	private static final String TITLE_PROPERTY = "flappyearth.display.title";
	private static final String TITLE_ENV = "FLAPPYEARTH_TITLE";

	private static final String FPS_PROPERTY = "flappyearth.display.fps";
	private static final String FPS_ENV = "FLAPPYEARTH_FPS";

	private static final String VSYNC_PROPERTY = "flappyearth.display.vsync";
	private static final String VSYNC_ENV = "FLAPPYEARTH_VSYNC";

	private static final String RESIZABLE_PROPERTY = "flappyearth.display.resizable";
	private static final String RESIZABLE_ENV = "FLAPPYEARTH_RESIZABLE";

	private static final String WINDOW_WIDTH_PROPERTY = "flappyearth.display.width";
	private static final String WINDOW_WIDTH_ENV = "FLAPPYEARTH_WIDTH";

	private static final String WINDOW_HEIGHT_PROPERTY = "flappyearth.display.height";
	private static final String WINDOW_HEIGHT_ENV = "FLAPPYEARTH_HEIGHT";

	private static final String MAXIMIZED_PROPERTY = "flappyearth.display.maximized";
	private static final String MAXIMIZED_ENV = "FLAPPYEARTH_MAXIMIZED";

	private static final String WORLD_WIDTH_PROPERTY = "flappyearth.world.width";
	private static final String WORLD_WIDTH_ENV = "FLAPPYEARTH_WORLD_WIDTH";

	private static final String WORLD_HEIGHT_PROPERTY = "flappyearth.world.height";
	private static final String WORLD_HEIGHT_ENV = "FLAPPYEARTH_WORLD_HEIGHT";

	private static final int DEFAULT_TARGET_FPS = 60;
	private static final String DEFAULT_TITLE = "FlappyEarth";
	private static final int DEFAULT_MAXIMIZED_BOOT_WIDTH = 640;
	private static final int DEFAULT_MAXIMIZED_BOOT_HEIGHT = 480;
	private static final int DEFAULT_TEAM_WINDOW_WIDTH = 1280;
	private static final int DEFAULT_TEAM_WINDOW_HEIGHT = 720;

	// Baseline reference used to keep world scale and gameplay feel consistent across window sizes.
	private static final int DEFAULT_REFERENCE_WORLD_WIDTH = 2240;
	private static final int DEFAULT_REFERENCE_WORLD_HEIGHT = 1260;

	private DisplaySettingsFactory() {
	}

	public static DisplaySettings createFromRuntime() {
		DisplayProfile profile = resolveProfile();

		int launchWidth = profile == DisplayProfile.PLAYER_MAXIMIZED
				? DEFAULT_MAXIMIZED_BOOT_WIDTH
				: DEFAULT_TEAM_WINDOW_WIDTH;
		int launchHeight = profile == DisplayProfile.PLAYER_MAXIMIZED
				? DEFAULT_MAXIMIZED_BOOT_HEIGHT
				: DEFAULT_TEAM_WINDOW_HEIGHT;
		boolean startMaximized = profile == DisplayProfile.PLAYER_MAXIMIZED;

		launchWidth = readInt(WINDOW_WIDTH_PROPERTY, WINDOW_WIDTH_ENV, launchWidth);
		launchHeight = readInt(WINDOW_HEIGHT_PROPERTY, WINDOW_HEIGHT_ENV, launchHeight);
		startMaximized = readBoolean(MAXIMIZED_PROPERTY, MAXIMIZED_ENV, startMaximized);

		int worldWidth = readInt(WORLD_WIDTH_PROPERTY, WORLD_WIDTH_ENV, DEFAULT_REFERENCE_WORLD_WIDTH);
		int worldHeight = readInt(WORLD_HEIGHT_PROPERTY, WORLD_HEIGHT_ENV, DEFAULT_REFERENCE_WORLD_HEIGHT);

		return new DisplaySettings(
				readString(TITLE_PROPERTY, TITLE_ENV, DEFAULT_TITLE),
				readInt(FPS_PROPERTY, FPS_ENV, DEFAULT_TARGET_FPS),
				readBoolean(VSYNC_PROPERTY, VSYNC_ENV, true),
				readBoolean(RESIZABLE_PROPERTY, RESIZABLE_ENV, true),
				launchWidth,
				launchHeight,
				startMaximized,
				Math.max(1f, worldWidth),
				Math.max(1f, worldHeight));
	}

	private static DisplayProfile resolveProfile() {
		String raw = readString(PROFILE_PROPERTY, PROFILE_ENV, DisplayProfile.TEAM_WINDOWED.name());
		try {
			return DisplayProfile.valueOf(raw.trim().toUpperCase());
		} catch (RuntimeException ex) {
			return DisplayProfile.PLAYER_MAXIMIZED;
		}
	}

	private static String readString(String property, String env, String defaultValue) {
		String value = System.getProperty(property);
		if (value == null || value.trim().isEmpty()) {
			value = System.getenv(env);
		}
		if (value == null || value.trim().isEmpty()) {
			return defaultValue;
		}
		return value.trim();
	}

	private static int readInt(String property, String env, int defaultValue) {
		String raw = readString(property, env, null);
		if (raw == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(raw.trim());
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	private static boolean readBoolean(String property, String env, boolean defaultValue) {
		String raw = readString(property, env, null);
		if (raw == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(raw.trim());
	}

	private enum DisplayProfile {
		PLAYER_MAXIMIZED,
		TEAM_WINDOWED
	}
}
