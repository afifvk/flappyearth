package inf1009.p63.flappyearth.game.setup;

import java.util.Arrays;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import inf1009.p63.flappyearth.engine.core.GameSetup;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.AssetKeys;
import inf1009.p63.flappyearth.game.config.DisplaySettings;
import inf1009.p63.flappyearth.game.config.DisplaySettingsFactory;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.input.KeyboardInputDevice;
import inf1009.p63.flappyearth.game.input.TouchInputDevice;
import inf1009.p63.flappyearth.game.scenes.CreditsScene;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameplayScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.scenes.MenuScene;
import inf1009.p63.flappyearth.game.scenes.SettingsScene;
import inf1009.p63.flappyearth.game.config.StageConfig;
import inf1009.p63.flappyearth.game.config.StagePlan;
import inf1009.p63.flappyearth.game.state.GameSession;

public class FlappyEarthSetup implements GameSetup {

    private final DisplaySettings displaySettings;

    public FlappyEarthSetup() {
        this(DisplaySettingsFactory.createFromRuntime());
    }

    public FlappyEarthSetup(DisplaySettings displaySettings) {
        this.displaySettings = displaySettings;
    }

    @Override
    public void configure(EngineContext contextManager, SceneManager sceneManager) {
        contextManager.getInputManager().registerInputDevice(new KeyboardInputDevice());
        contextManager.getInputManager().registerInputDevice(new TouchInputDevice());

        StagePlan stagePlan = new StagePlan(Arrays.asList(
                new StageConfig(GameSceneId.STAGE_ONE.id(), "A Broken World", "Collect 1 good item", 0.20f, 0.45f, 0.95f, 0.70f, 1f,
                        "Restore the Earth one step at a time", "Collect good collectibles", "Good luck"),
                new StageConfig(GameSceneId.STAGE_TWO.id(), "Glimmer of Hope", "Collect 1 more item", 0.98f, 0.94f, 0.62f, 0.50f, 1.15f,
                        "Good Job!"),
                new StageConfig(GameSceneId.STAGE_THREE.id(), "Nature's Fruition", "Collect 1 more item", 0.70f, 0.86f, 0.58f, 0.25f, 1.22f,
                        "Keep it up!"),
                new StageConfig(GameSceneId.STAGE_FOUR.id(), "A Blooming Renaissance", "Final stage", 0.46f, 0.72f, 0.50f, 0f, 1.30f)
        ));
        GameplayDimensions dimensions = GameplayDimensions.fromDisplaySettings(displaySettings);

        GameSession gameSession = new GameSession(stagePlan.getFinalTargetGoodCollectibles());

        contextManager.getAssetManager().load("textures/entities/obstacles/pipe.png", Texture.class);
        contextManager.getAssetManager().load("textures/backgrounds/stage/stage1.png", Texture.class);
        contextManager.getAssetManager().load("textures/backgrounds/stage/stage2.png", Texture.class);
        contextManager.getAssetManager().load("textures/backgrounds/stage/stage3.png", Texture.class);
        contextManager.getAssetManager().load("textures/backgrounds/stage/stage4.png", Texture.class);
        contextManager.getAssetManager().load("textures/effects/smoke.png", Texture.class);
        contextManager.getAssetManager().load("textures/effects/smog_cloud.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/bird/frame_00.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/bird/frame_01.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/bird/frame_02.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/bird/frame_03.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/bird/frame_04.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/bird/frame_05.png", Texture.class);

        contextManager.getAssetManager().load("audio/sfx/bird/hit.mp3", Sound.class);
        contextManager.getAssetManager().load("audio/sfx/bird/die.mp3", Sound.class);
        contextManager.getAssetManager().load("audio/sfx/gameplay/game_over.mp3", Sound.class);
        contextManager.getAssetManager().load("audio/sfx/bird/point.mp3", Sound.class);
        contextManager.getAssetManager().load("audio/sfx/bird/whoosh.mp3", Sound.class);

        contextManager.getAssetManager().load("audio/music/menu.mp3", Music.class);
        contextManager.getAssetManager().load("audio/music/victory.mp3", Music.class);
        contextManager.getAssetManager().load("audio/music/game.mp3", Music.class); 
        contextManager.getAssetManager().load("audio/sfx/ui/button_click.mp3", Sound.class); 
        contextManager.getAssetManager().load("audio/sfx/gameplay/collect_good.mp3", Sound.class); 
        contextManager.getAssetManager().load("audio/sfx/gameplay/collect_bad.mp3", Sound.class); 
        contextManager.getAssetManager().load("audio/sfx/gameplay/stage_transition.mp3", Sound.class); 

        // Collectibles
        contextManager.getAssetManager().load("textures/entities/collectibles/bad/garbage.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/bad/oil_spill.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/bad/factory.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/good/recycling_bin.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/bad/plastic_bottle.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/good/solar_panel.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/good/sapling.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/good/greenhouse.png", Texture.class);
        contextManager.getAssetManager().load("textures/entities/collectibles/good/wind_turbine.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/hearts/heart_full.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/hearts/heart_empty.png", Texture.class);

        // UI backgrounds
        contextManager.getAssetManager().load("textures/backgrounds/menu.png", Texture.class);
        contextManager.getAssetManager().load(AssetKeys.ENDGAME_BG, Texture.class);
        contextManager.getAssetManager().load("textures/backgrounds/game_over.png", Texture.class);
        contextManager.getAssetManager().load(AssetKeys.INSTRUCTIONS_BG, Texture.class);
        contextManager.getAssetManager().load(AssetKeys.CREDITS_PAGE_BG, Texture.class);
        contextManager.getAssetManager().load("textures/backgrounds/settings.png", Texture.class);
        contextManager.getAssetManager().load("textures/backgrounds/pause.png", Texture.class);

        // Menu buttons
        contextManager.getAssetManager().load("textures/ui/buttons/start_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/start_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/settings_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/settings_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/credits_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/credits_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/quit_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/quit_2.png", Texture.class);
        
        // Settings buttons
        contextManager.getAssetManager().load("textures/ui/buttons/back_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/back_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/help_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/help_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/brightness_down_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/brightness_down_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/brightness_up_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/brightness_up_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/volume_down_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/volume_down_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/volume_up_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/sliders/volume_up_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/misc/mute.png", Texture.class);
        
        // Pause buttons
        contextManager.getAssetManager().load("textures/ui/buttons/resume_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/resume_2.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/restart_1.png", Texture.class);
        contextManager.getAssetManager().load("textures/ui/buttons/restart_2.png", Texture.class);
        
        contextManager.getAssetManager().finishLoading();

    contextManager.getAudioManager().registerSound(AudioKeys.OBSTACLE_HIT,     contextManager.getAssetManager().get(AudioKeys.OBSTACLE_HIT,     Sound.class));
    contextManager.getAudioManager().registerSound(AudioKeys.DEATH,             contextManager.getAssetManager().get(AudioKeys.DEATH,             Sound.class));
    contextManager.getAudioManager().registerSound(AudioKeys.GAME_OVER,         contextManager.getAssetManager().get(AudioKeys.GAME_OVER,         Sound.class));
    contextManager.getAudioManager().registerSound(AudioKeys.OBSTACLE_PASS,     contextManager.getAssetManager().get(AudioKeys.OBSTACLE_PASS,     Sound.class));
    contextManager.getAudioManager().registerSound(AudioKeys.FLAP,              contextManager.getAssetManager().get(AudioKeys.FLAP,              Sound.class), 0.8f);
    contextManager.getAudioManager().registerSound(AudioKeys.UI_CLICK,          contextManager.getAssetManager().get(AudioKeys.UI_CLICK,          Sound.class));
    contextManager.getAudioManager().registerSound(AudioKeys.GOOD_COLLECT,      contextManager.getAssetManager().get(AudioKeys.GOOD_COLLECT,      Sound.class));
    contextManager.getAudioManager().registerSound(AudioKeys.BAD_COLLECT,       contextManager.getAssetManager().get(AudioKeys.BAD_COLLECT,       Sound.class), 1.1f);
    contextManager.getAudioManager().registerSound(AudioKeys.STAGE_TRANSITION,  contextManager.getAssetManager().get(AudioKeys.STAGE_TRANSITION,  Sound.class));
    contextManager.getAudioManager().registerMusic(AudioKeys.MUSIC_MENU,        contextManager.getAssetManager().get(AudioKeys.MUSIC_MENU,        Music.class));
    contextManager.getAudioManager().registerMusic(AudioKeys.MUSIC_VICTORY,     contextManager.getAssetManager().get(AudioKeys.MUSIC_VICTORY,     Music.class));
    contextManager.getAudioManager().registerMusic(AudioKeys.MUSIC_GAME,        contextManager.getAssetManager().get(AudioKeys.MUSIC_GAME,        Music.class));

        sceneManager.registerScene(GameSceneId.MENU.id(), new MenuScene(sceneManager, contextManager, gameSession, stagePlan));
        sceneManager.registerScene(GameSceneId.SETTINGS.id(), new SettingsScene(sceneManager, contextManager));
        sceneManager.registerScene(GameSceneId.CREDITS.id(), new CreditsScene(sceneManager, contextManager));

        for (StageConfig stage : stagePlan.getStages()) {
            sceneManager.registerScene(stage.getSceneId(),
                    new GameplayScene(
                            sceneManager,
                            contextManager,
                            gameSession,
                            stagePlan,
                            stage,
                            dimensions));
        }

        sceneManager.registerScene(
                GameSceneId.GAME_OVER.id(),
                new GameOverScene(sceneManager, contextManager, gameSession, stagePlan));
    }

    @Override
    public String getInitialSceneId() {
        return GameSceneId.MENU.id();
    }
}
