package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private Sound flapSound;
    private Sound collectGoodSound;
    private Sound hitBadSound;
    private Sound pointSound;
    private Sound dieSound;
    private Sound gameOverSound;
    private Music backgroundMusic;

    private Sound collectBadSound;
    private Sound stageTransitionSound;
    private Sound buttonClickSound;

    private Music menuMusic;
    private Music victoryMusic;

    // Tracks which music track is currently active so resumeMusic() knows what to resume
    private enum ActiveTrack { NONE, BACKGROUND, MENU, VICTORY }
    private ActiveTrack activeTrack = ActiveTrack.NONE;

    private float masterVolume = 1.0f;
    private boolean muted = false;

    public SoundManager() {}

    public void playFlap()            { if (flapSound != null)        flapSound.play(0.8f * getEffectiveVolume()); }
    public void playCollectGood()     { if (collectGoodSound != null) collectGoodSound.play(1f * getEffectiveVolume()); }
    public void playHitBad()          { if (hitBadSound != null)      hitBadSound.play(1f * getEffectiveVolume()); }
    public void playPoint()           { if (pointSound != null)       pointSound.play(1f * getEffectiveVolume()); }
    public void playDie()             { if (dieSound != null)         dieSound.play(1f * getEffectiveVolume()); }
    public void playGameOver()        { if (gameOverSound != null)    gameOverSound.play(1f * getEffectiveVolume()); }
    public void playCollectBad()      { if (collectBadSound != null)  collectBadSound.play(1f * getEffectiveVolume()); }
    public void playStageTransition() { if (stageTransitionSound != null) stageTransitionSound.play(1f * getEffectiveVolume()); }
    public void playButtonClick()     { if (buttonClickSound != null) buttonClickSound.play(1f * getEffectiveVolume()); }

    // ── music control ────────────────────────────────────────────────────────

    private void stopAllMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
        if (menuMusic != null)       menuMusic.stop();
        if (victoryMusic != null)    victoryMusic.stop();
    }

    public void startMusic() {
        stopAllMusic();
        activeTrack = ActiveTrack.BACKGROUND;
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(getEffectiveVolume());
            backgroundMusic.play();
        }
    }

    public void startMenuMusic() {
        stopAllMusic();
        activeTrack = ActiveTrack.MENU;
        if (menuMusic != null) {
            menuMusic.setLooping(true);
            menuMusic.setVolume(getEffectiveVolume());
            menuMusic.play();
        }
    }

    public void startVictoryMusic() {
        stopAllMusic();
        activeTrack = ActiveTrack.VICTORY;
        if (victoryMusic != null) {
            victoryMusic.setLooping(true);
            victoryMusic.setVolume(getEffectiveVolume());
            victoryMusic.play();
        }
    }

    public void stopMusic() {
        stopAllMusic();
        activeTrack = ActiveTrack.NONE;
    }

    // ── PAUSE / RESUME ───────────────────────────────────────────────────────
    // pauseMusic() suspends playback without losing the position in the track.
    // resumeMusic() continues from exactly where it stopped.

    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) backgroundMusic.pause();
        if (menuMusic != null       && menuMusic.isPlaying())       menuMusic.pause();
        if (victoryMusic != null    && victoryMusic.isPlaying())    victoryMusic.pause();
    }

    public void resumeMusic() {
        switch (activeTrack) {
            case BACKGROUND:
                if (backgroundMusic != null) backgroundMusic.play();
                break;
            case MENU:
                if (menuMusic != null) menuMusic.play();
                break;
            case VICTORY:
                if (victoryMusic != null) victoryMusic.play();
                break;
            default:
                break;
        }
    }
    // ─────────────────────────────────────────────────────────────────────────

    public void setFlapSound(Sound sound)            { this.flapSound = sound; }
    public void setCollectGoodSound(Sound sound)     { this.collectGoodSound = sound; }
    public void setHitBadSound(Sound sound)          { this.hitBadSound = sound; }
    public void setPointSound(Sound sound)           { this.pointSound = sound; }
    public void setDieSound(Sound sound)             { this.dieSound = sound; }
    public void setGameOverSound(Sound sound)        { this.gameOverSound = sound; }
    public void setCollectBadSound(Sound sound)      { this.collectBadSound = sound; }
    public void setStageTransitionSound(Sound sound) { this.stageTransitionSound = sound; }
    public void setButtonClickSound(Sound sound)     { this.buttonClickSound = sound; }

    public void setBackgroundMusic(Music music) {
        this.backgroundMusic = music;
        if (this.backgroundMusic != null) this.backgroundMusic.setVolume(getEffectiveVolume());
    }
    public void setMenuMusic(Music music) {
        this.menuMusic = music;
        if (this.menuMusic != null) this.menuMusic.setVolume(getEffectiveVolume());
    }
    public void setVictoryMusic(Music music) {
        this.victoryMusic = music;
        if (this.victoryMusic != null) this.victoryMusic.setVolume(getEffectiveVolume());
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = Math.max(0f, Math.min(1f, masterVolume));
        if (backgroundMusic != null) backgroundMusic.setVolume(getEffectiveVolume());
        if (menuMusic != null)       menuMusic.setVolume(getEffectiveVolume());
        if (victoryMusic != null)    victoryMusic.setVolume(getEffectiveVolume());
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (backgroundMusic != null) backgroundMusic.setVolume(getEffectiveVolume());
        if (menuMusic != null)       menuMusic.setVolume(getEffectiveVolume());
        if (victoryMusic != null)    victoryMusic.setVolume(getEffectiveVolume());
    }

    public boolean isMuted() { return muted; }

    public float getMasterVolume() { return masterVolume; }

    private float getEffectiveVolume() {
        return muted ? 0f : masterVolume;
    }

    public void dispose() {
        if (flapSound != null)           flapSound.dispose();
        if (collectGoodSound != null)    collectGoodSound.dispose();
        if (hitBadSound != null)         hitBadSound.dispose();
        if (pointSound != null)          pointSound.dispose();
        if (dieSound != null)            dieSound.dispose();
        if (gameOverSound != null)       gameOverSound.dispose();
        if (backgroundMusic != null)     backgroundMusic.dispose();
        if (collectBadSound != null)     collectBadSound.dispose();
        if (stageTransitionSound != null) stageTransitionSound.dispose();
        if (buttonClickSound != null)    buttonClickSound.dispose();
        if (menuMusic != null)           menuMusic.dispose();
        if (victoryMusic != null)        victoryMusic.dispose();
    }
}
