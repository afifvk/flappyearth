package inf1009.p63.flappyearth.game.setup;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.game.config.AssetKeys;

public final class GameAssetRegistry {

    private GameAssetRegistry() {}

    public static void loadAll(EngineContext context) {
        loadTextures(context);
        loadAudio(context);
        context.getAssetManager().finishLoading();
    }

    private static void loadTextures(EngineContext context) {
        context.getAssetManager().load("textures/entities/obstacles/pipe.png", Texture.class);
        context.getAssetManager().load("textures/backgrounds/stage/stage1.png", Texture.class);
        context.getAssetManager().load("textures/backgrounds/stage/stage2.png", Texture.class);
        context.getAssetManager().load("textures/backgrounds/stage/stage3.png", Texture.class);
        context.getAssetManager().load("textures/backgrounds/stage/stage4.png", Texture.class);
        context.getAssetManager().load("textures/effects/smoke.png", Texture.class);
        context.getAssetManager().load("textures/effects/smog_cloud.png", Texture.class);
        context.getAssetManager().load("textures/entities/bird/frame_00.png", Texture.class);
        context.getAssetManager().load("textures/entities/bird/frame_01.png", Texture.class);
        context.getAssetManager().load("textures/entities/bird/frame_02.png", Texture.class);
        context.getAssetManager().load("textures/entities/bird/frame_03.png", Texture.class);
        context.getAssetManager().load("textures/entities/bird/frame_04.png", Texture.class);
        context.getAssetManager().load("textures/entities/bird/frame_05.png", Texture.class);

        context.getAssetManager().load("textures/entities/collectibles/bad/garbage.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/bad/oil_spill.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/bad/factory.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/bad/plastic_bottle.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/good/recycling_bin.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/good/solar_panel.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/good/sapling.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/good/greenhouse.png", Texture.class);
        context.getAssetManager().load("textures/entities/collectibles/good/wind_turbine.png", Texture.class);
        context.getAssetManager().load("textures/ui/hearts/heart_full.png", Texture.class);
        context.getAssetManager().load("textures/ui/hearts/heart_empty.png", Texture.class);

        context.getAssetManager().load("textures/backgrounds/menu.png", Texture.class);
        context.getAssetManager().load(AssetKeys.ENDGAME_BG, Texture.class);
        context.getAssetManager().load("textures/backgrounds/game_over.png", Texture.class);
        context.getAssetManager().load(AssetKeys.INSTRUCTIONS_BG, Texture.class);
        context.getAssetManager().load(AssetKeys.CREDITS_PAGE_BG, Texture.class);
        context.getAssetManager().load("textures/backgrounds/settings.png", Texture.class);
        context.getAssetManager().load("textures/backgrounds/pause.png", Texture.class);

        context.getAssetManager().load("textures/ui/buttons/start_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/start_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/settings_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/settings_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/credits_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/credits_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/quit_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/quit_2.png", Texture.class);

        context.getAssetManager().load("textures/ui/buttons/back_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/back_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/help_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/help_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/brightness_down_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/brightness_down_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/brightness_up_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/brightness_up_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/volume_down_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/volume_down_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/volume_up_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/sliders/volume_up_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/misc/mute.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/resume_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/resume_2.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/restart_1.png", Texture.class);
        context.getAssetManager().load("textures/ui/buttons/restart_2.png", Texture.class);
    }

    private static void loadAudio(EngineContext context) {
        context.getAssetManager().load("audio/sfx/bird/hit.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/bird/die.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/gameplay/game_over.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/bird/point.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/bird/whoosh.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/ui/button_click.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/gameplay/collect_good.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/gameplay/collect_bad.mp3", Sound.class);
        context.getAssetManager().load("audio/sfx/gameplay/stage_transition.mp3", Sound.class);

        context.getAssetManager().load("audio/music/menu.mp3", Music.class);
        context.getAssetManager().load("audio/music/victory.mp3", Music.class);
        context.getAssetManager().load("audio/music/game.mp3", Music.class);
    }
}
