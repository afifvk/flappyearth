package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private Sound flapSound;
    private Sound collectGoodSound;
    private Sound hitBadSound;
    private Sound gameOverSound;
    private Music backgroundMusic;

    public SoundManager() {
    }

    public void playFlap()        { if (flapSound        != null) flapSound.play(0.8f); }
    public void playCollectGood() { if (collectGoodSound != null) collectGoodSound.play(1f); }
    public void playHitBad()      { if (hitBadSound      != null) hitBadSound.play(1f); }
    public void playGameOver()    { if (gameOverSound    != null) gameOverSound.play(1f); }

    public void startMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
    }
    public void stopMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }

    public void setFlapSound(Sound sound)           { this.flapSound = sound; }
    public void setCollectGoodSound(Sound sound)    { this.collectGoodSound = sound; }
    public void setHitBadSound(Sound sound)         { this.hitBadSound = sound; }
    public void setGameOverSound(Sound sound)       { this.gameOverSound = sound; }
    public void setBackgroundMusic(Music music)     { this.backgroundMusic = music; }

    public void dispose() {
        if (flapSound        != null) flapSound.dispose();
        if (collectGoodSound != null) collectGoodSound.dispose();
        if (hitBadSound      != null) hitBadSound.dispose();
        if (gameOverSound    != null) gameOverSound.dispose();
        if (backgroundMusic  != null) backgroundMusic.dispose();
    }
}
