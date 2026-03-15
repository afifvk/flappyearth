package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private Sound flapSound;
    private Sound collectGoodSound;
    private Sound hitBadSound;
    private Sound pointSound;
    private Sound gameOverSound;
    private Music backgroundMusic;
    private float masterVolume = 1.0f;

    public SoundManager() {
    }

    public void playFlap()        { if (flapSound        != null) flapSound.play(0.8f * masterVolume); }
    public void playCollectGood() { if (collectGoodSound != null) collectGoodSound.play(1f * masterVolume); }
    public void playHitBad()      { if (hitBadSound      != null) hitBadSound.play(1f * masterVolume); }
    public void playPoint()       { if (pointSound       != null) pointSound.play(1f * masterVolume); }
    public void playGameOver()    { if (gameOverSound    != null) gameOverSound.play(1f * masterVolume); }

    public void startMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(masterVolume);
            backgroundMusic.play();
        }
    }
    public void stopMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }

    public void setFlapSound(Sound sound)           { this.flapSound = sound; }
    public void setCollectGoodSound(Sound sound)    { this.collectGoodSound = sound; }
    public void setHitBadSound(Sound sound)         { this.hitBadSound = sound; }
    public void setPointSound(Sound sound)          { this.pointSound = sound; }
    public void setGameOverSound(Sound sound)       { this.gameOverSound = sound; }
    public void setBackgroundMusic(Music music)     {
        this.backgroundMusic = music;
        if (this.backgroundMusic != null) {
            this.backgroundMusic.setVolume(masterVolume);
        }
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = Math.max(0f, Math.min(1f, masterVolume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(this.masterVolume);
        }
    }

    public float getMasterVolume() {
        return masterVolume;
    }

    public void dispose() {
        if (flapSound        != null) flapSound.dispose();
        if (collectGoodSound != null) collectGoodSound.dispose();
        if (hitBadSound      != null) hitBadSound.dispose();
        if (pointSound       != null) pointSound.dispose();
        if (gameOverSound    != null) gameOverSound.dispose();
        if (backgroundMusic  != null) backgroundMusic.dispose();
    }
}
