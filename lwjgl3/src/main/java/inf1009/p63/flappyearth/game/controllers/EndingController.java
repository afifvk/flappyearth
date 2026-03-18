package inf1009.p63.flappyearth.game.controllers;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import com.badlogic.gdx.Input;

import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.engine.services.InputManager;
import inf1009.p63.flappyearth.engine.services.AudioManager;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.input.GameInputAction;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.state.GameState;

public class EndingController {

    private static final float ENDING_SPAWN_DELAY_SECONDS = 3.0f;
    private static final float[] LINE_REVEAL_TIMES = { 0.5f, 2.0f, 3.5f, 5.0f };
    private static final int   TOTAL_ENDING_LINES  = 4;
    private boolean active;
    private boolean awaitingContinue;
    private boolean spawnWarmup;
    private float spawnWarmupTimer;
    private final GameplayDimensions dimensions;
    private boolean musicPlayed;
    private float   lineRevealTimer;
    private int     revealedLines;

    public EndingController(GameplayDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public void onEnter(boolean isFinalStage, GameState gameState) {
        active = isFinalStage;
        awaitingContinue = active;
        spawnWarmup = false;
        spawnWarmupTimer = 0f;
        musicPlayed      = false;
        lineRevealTimer  = 0f;
        revealedLines    = 0;

        gameState.setControlsEnabled(!active);
        gameState.setSpawningEnabled(!active);
    }

    public boolean update(float delta,
                          Player player,
                          InputManager inputOutputManager,
                          SceneManager sceneManager,
                          GameState gameState,
                          AudioManager soundManager) {
        if (!active) return false;

        if (awaitingContinue && !musicPlayed && soundManager != null) {
            soundManager.playMusic(AudioKeys.MUSIC_VICTORY);
            musicPlayed = true;
        }

        if (awaitingContinue) {
            lineRevealTimer += delta;
            revealedLines = 0;
            for (float t : LINE_REVEAL_TIMES) {
                if (lineRevealTimer >= t) revealedLines++;
            }
        }

        if (isInputAllowed() && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GameOverScene gameOverScene = (GameOverScene) sceneManager.getScene(GameSceneId.GAME_OVER.id());
            gameOverScene.setVictoryEnding(true);
            sceneManager.switchTo(GameSceneId.GAME_OVER.id());
            return true;
        }

        if ((awaitingContinue || spawnWarmup) && player != null) {
            autoPilot(player);
        }

        if (awaitingContinue && isInputAllowed() && inputOutputManager.isActionJustPressed(GameInputAction.FLAP.id())) {
            awaitingContinue = false;
            spawnWarmup = true;
            spawnWarmupTimer = ENDING_SPAWN_DELAY_SECONDS;
            gameState.setControlsEnabled(false);
            
            if (soundManager != null) {
                soundManager.playMusic(AudioKeys.MUSIC_GAME);
            }
        }

        if (spawnWarmup) {
            spawnWarmupTimer = Math.max(0f, spawnWarmupTimer - delta);
            if (spawnWarmupTimer <= 0f) {
                spawnWarmup = false;
                gameState.setControlsEnabled(true);
                gameState.setSpawningEnabled(true);
            }
        }

        return false;
    }

    public boolean isActive() { return active; }
    public boolean isAwaitingContinue() { return awaitingContinue; }
    public boolean isSpawnWarmup() { return spawnWarmup; }
    public float getSpawnWarmupTimer() { return spawnWarmupTimer; }
    public boolean isSafeEndingWindow() { return active && (awaitingContinue || spawnWarmup); }
    public int     getRevealedLines()   { return revealedLines; }
    public boolean isInputAllowed()     { return revealedLines >= TOTAL_ENDING_LINES; }

    private void autoPilot(Player player) {
        float targetY = dimensions.getWorldHeight() * dimensions.getEndingTargetHeightRatio();
        float playerCenterY = player.getBounds().y + (player.getBounds().height / 2f);
        if (playerCenterY < targetY - dimensions.getEndingFlapWindow()) {
            player.flap();
        }
    }
}
