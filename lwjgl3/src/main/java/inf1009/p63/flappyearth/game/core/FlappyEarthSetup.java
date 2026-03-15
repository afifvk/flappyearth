package inf1009.p63.flappyearth.game.core;

import java.util.Arrays;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.GameSetup;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.config.AssetKeys;
import inf1009.p63.flappyearth.game.config.DisplaySettings;
import inf1009.p63.flappyearth.game.config.DisplaySettingsFactory;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.input.KeyboardInputDevice;
import inf1009.p63.flappyearth.game.input.TouchInputDevice;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.scenes.MenuScene;
import inf1009.p63.flappyearth.game.scenes.SettingsScene;
import inf1009.p63.flappyearth.game.scenes.StageConfig;
import inf1009.p63.flappyearth.game.scenes.StagePlan;
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
    public void configure(GameContextManager contextManager, SceneManager sceneManager) {
        contextManager.getInputOutputManager().registerInputDevice(new KeyboardInputDevice());
        contextManager.getInputOutputManager().registerInputDevice(new TouchInputDevice());

        StagePlan stagePlan = new StagePlan(Arrays.asList(
                new StageConfig(GameSceneId.STAGE_ONE.id(), "A Broken World", "Collect 1 good item", 0.20f, 0.45f, 0.95f, 0.70f, 1f),
                new StageConfig(GameSceneId.STAGE_TWO.id(), "Glimmer of Hope", "Collect 1 more item", 0.98f, 0.94f, 0.62f, 0.50f, 1.15f),
                new StageConfig(GameSceneId.STAGE_THREE.id(), "Nature's Fruition", "Collect 1 more item", 0.70f, 0.86f, 0.58f, 0.25f, 1.22f),
                new StageConfig(GameSceneId.STAGE_FOUR.id(), "A Blooming Renaissance", "Final stage", 0.46f, 0.72f, 0.50f, 0f, 1.30f)
        ));
        GameplayDimensions dimensions = GameplayDimensions.fromDisplaySettings(displaySettings);

        GameSession gameSession = new GameSession(stagePlan.getFinalTargetGoodCollectibles());

        contextManager.getAssetManager().load("backgrounds/pipe.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/stage1_background.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/stage2_background.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/stage3_background.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/stage4_background.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/smoke.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/smog_cloud.png", Texture.class);
        contextManager.getAssetManager().load("bird/flappy00.png", Texture.class);
        contextManager.getAssetManager().load("bird/flappy01.png", Texture.class);
        contextManager.getAssetManager().load("bird/flappy02.png", Texture.class);
        contextManager.getAssetManager().load("bird/flappy03.png", Texture.class);
        contextManager.getAssetManager().load("bird/flappy04.png", Texture.class);
        contextManager.getAssetManager().load("bird/flappy05.png", Texture.class);

        contextManager.getAssetManager().load("sound/bird_hit.mp3", Sound.class);
        contextManager.getAssetManager().load("sound/bird_die.mp3", Sound.class);
        contextManager.getAssetManager().load("sound/game_over.mp3", Sound.class);
        contextManager.getAssetManager().load("sound/bird_point.mp3", Sound.class);
        contextManager.getAssetManager().load("sound/bird_whoosh.mp3", Sound.class);

        contextManager.getAssetManager().load("sound/menu_music.mp3", Music.class);
        contextManager.getAssetManager().load("sound/victory_music.mp3", Music.class);
        contextManager.getAssetManager().load("sound/game_music.mp3", Music.class); 
        contextManager.getAssetManager().load("sound/button_click.mp3", Sound.class); 
        contextManager.getAssetManager().load("sound/collect_bad.mp3", Sound.class); 
        contextManager.getAssetManager().load("sound/stage_transition.mp3", Sound.class); 

        // Collectibles
        contextManager.getAssetManager().load("collectibles/bc_garbage.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/bc_oilspill.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/bc_factory.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/gc_recylingbin.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/bc_plasticbottle.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/gc_solarpanel.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/gc_sapling.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/gc_greenhouse.png", Texture.class);
        contextManager.getAssetManager().load("collectibles/gc_windturbine.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/heart_full.png", Texture.class);
        contextManager.getAssetManager().load("backgrounds/heart_empty.png", Texture.class);

        // UI backgrounds
        contextManager.getAssetManager().load("ui/menu_background.png", Texture.class);
        contextManager.getAssetManager().load(AssetKeys.ENDGAME_BG, Texture.class);
        contextManager.getAssetManager().load("ui/gamefailed_background.png", Texture.class);
        contextManager.getAssetManager().load(AssetKeys.INSTRUCTIONS_BG, Texture.class);
        contextManager.getAssetManager().load("ui/settings_background.png", Texture.class);
        contextManager.getAssetManager().load("ui/pause_background.png", Texture.class);

        // Menu buttons
        contextManager.getAssetManager().load("buttons/A_Start1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Start2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Settings1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Settings2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Quit1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Quit2.png", Texture.class);
        
        // Settings buttons
        contextManager.getAssetManager().load("buttons/A_Back1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Back2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Helps1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Helps2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/brightness_down1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/brightness_down2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/brightness_up1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/brightness_up2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/volume_down1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/volume_down2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/volume_up1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/volume_up2.png", Texture.class);
        
        // Pause buttons
        contextManager.getAssetManager().load("buttons/A_Resume1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Resume2.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Restart1.png", Texture.class);
        contextManager.getAssetManager().load("buttons/A_Restart2.png", Texture.class);
        
        contextManager.getAssetManager().finishLoading();

        Sound hit = contextManager.getAssetManager().get("sound/bird_hit.mp3", Sound.class);
        Sound die = contextManager.getAssetManager().get("sound/bird_die.mp3", Sound.class);
        Sound point = contextManager.getAssetManager().get("sound/bird_point.mp3", Sound.class);
        Sound whoosh = contextManager.getAssetManager().get("sound/bird_whoosh.mp3", Sound.class);

        contextManager.getSoundManager().setHitBadSound(hit);
        contextManager.getSoundManager().setGameOverSound(die);
        contextManager.getSoundManager().setPointSound(point);
        contextManager.getSoundManager().setFlapSound(whoosh);

        contextManager.getSoundManager().setMenuMusic(contextManager.getAssetManager().get("sound/menu_music.mp3", Music.class));
        contextManager.getSoundManager().setVictoryMusic(contextManager.getAssetManager().get("sound/victory_music.mp3", Music.class));
        contextManager.getSoundManager().setBackgroundMusic(contextManager.getAssetManager().get("sound/game_music.mp3", Music.class));

        contextManager.getSoundManager().setButtonClickSound(contextManager.getAssetManager().get("sound/button_click.mp3", Sound.class));
        contextManager.getSoundManager().setCollectBadSound(contextManager.getAssetManager().get("sound/collect_bad.mp3", Sound.class));
        contextManager.getSoundManager().setStageTransitionSound(contextManager.getAssetManager().get("sound/stage_transition.mp3", Sound.class));

        sceneManager.registerScene(GameSceneId.MENU.id(), new MenuScene(sceneManager, contextManager, gameSession, stagePlan));
        sceneManager.registerScene(GameSceneId.SETTINGS.id(), new SettingsScene(sceneManager, contextManager));

        for (StageConfig stage : stagePlan.getStages()) {
            sceneManager.registerScene(stage.getSceneId(),
                    new GameScene(
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
