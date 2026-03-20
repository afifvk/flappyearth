package inf1009.p63.flappyearth.game.setup;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.game.config.AudioKeys;

public final class GameAudioRegistry {

    private GameAudioRegistry() {}

    public static void registerAll(EngineContext context) {
        context.getAudioManager().registerSound(AudioKeys.OBSTACLE_HIT, context.getAssetManager().get(AudioKeys.OBSTACLE_HIT, Sound.class));
        context.getAudioManager().registerSound(AudioKeys.DEATH, context.getAssetManager().get(AudioKeys.DEATH, Sound.class));
        context.getAudioManager().registerSound(AudioKeys.GAME_OVER, context.getAssetManager().get(AudioKeys.GAME_OVER, Sound.class));
        context.getAudioManager().registerSound(AudioKeys.OBSTACLE_PASS, context.getAssetManager().get(AudioKeys.OBSTACLE_PASS, Sound.class));
        context.getAudioManager().registerSound(AudioKeys.FLAP, context.getAssetManager().get(AudioKeys.FLAP, Sound.class), 0.8f);
        context.getAudioManager().registerSound(AudioKeys.UI_CLICK, context.getAssetManager().get(AudioKeys.UI_CLICK, Sound.class));
        context.getAudioManager().registerSound(AudioKeys.GOOD_COLLECT, context.getAssetManager().get(AudioKeys.GOOD_COLLECT, Sound.class));
        context.getAudioManager().registerSound(AudioKeys.BAD_COLLECT, context.getAssetManager().get(AudioKeys.BAD_COLLECT, Sound.class), 1.1f);
        context.getAudioManager().registerSound(AudioKeys.STAGE_TRANSITION, context.getAssetManager().get(AudioKeys.STAGE_TRANSITION, Sound.class));

        context.getAudioManager().registerMusic(AudioKeys.MUSIC_MENU, context.getAssetManager().get(AudioKeys.MUSIC_MENU, Music.class));
        context.getAudioManager().registerMusic(AudioKeys.MUSIC_VICTORY, context.getAssetManager().get(AudioKeys.MUSIC_VICTORY, Music.class));
        context.getAudioManager().registerMusic(AudioKeys.MUSIC_GAME, context.getAssetManager().get(AudioKeys.MUSIC_GAME, Music.class));
    }
}
