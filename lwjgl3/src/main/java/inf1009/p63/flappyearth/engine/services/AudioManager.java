package inf1009.p63.flappyearth.engine.services;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private final Map<String, Sound> sounds       = new HashMap<>();
    private final Map<String, Float> soundVolumes = new HashMap<>();
    private final Map<String, Music> musicTracks  = new HashMap<>();
    private Music currentMusic;
    private float masterVolume = 1.0f;
    private boolean muted = false;

    public AudioManager() {}

    // ── Registration ─────────────────────────────────────────────────────────

    public void registerSound(String key, Sound sound) {
        registerSound(key, sound, 1.0f);
    }

    public void registerSound(String key, Sound sound, float volumeMultiplier) {
        sounds.put(key, sound);
        soundVolumes.put(key, volumeMultiplier);
    }

    public void registerMusic(String key, Music music) {
        musicTracks.put(key, music);
    }

    // ── Generic play API ─────────────────────────────────────────────────────

    public void playSound(String key) {
        Sound s = sounds.get(key);
        if (s == null) return;
        float mult = soundVolumes.getOrDefault(key, 1.0f);
        s.play(mult * getEffectiveVolume());
    }

    public void playMusic(String key) {
        stopAllTracks();
        Music m = musicTracks.get(key);
        if (m == null) return;
        m.setLooping(true);
        m.setVolume(getEffectiveVolume());
        m.play();
        currentMusic = m;
    }

    public void stopMusic() {
        stopAllTracks();
    }

    // pauseMusic() suspends without losing track position; resumeMusic() continues from where it stopped.
    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) currentMusic.pause();
    }

    public void resumeMusic() {
        if (currentMusic != null) currentMusic.play();
    }

    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    // ── Volume / mute ─────────────────────────────────────────────────────────

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = Math.max(0f, Math.min(1f, masterVolume));
        if (currentMusic != null) currentMusic.setVolume(getEffectiveVolume());
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (currentMusic != null) currentMusic.setVolume(getEffectiveVolume());
    }

    public boolean isMuted() { return muted; }

    public float getMasterVolume() { return masterVolume; }

    // ── Cleanup ───────────────────────────────────────────────────────────────

    public void dispose() {
        stopAllTracks();
        sounds.clear();
        soundVolumes.clear();
        musicTracks.clear();
        currentMusic = null;
    }

    private void stopAllTracks() {
        musicTracks.values().forEach(Music::stop);
        currentMusic = null;
    }

    private float getEffectiveVolume() {
        return muted ? 0f : masterVolume;
    }
}
