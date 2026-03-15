package inf1009.p63.flappyearth.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.engine.managers.InputOutputManager;
import inf1009.p63.flappyearth.engine.managers.SoundManager;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.input.GameInputAction;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.state.GameState;

public class EndingSceneController {

    private static final float ENDING_SPAWN_DELAY_SECONDS = 3.0f;
    private boolean active;
    private boolean awaitingContinue;
    private boolean spawnWarmup;
    private float spawnWarmupTimer;
    private final GameplayDimensions dimensions;
    private boolean musicPlayed;

    public EndingSceneController(GameplayDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public void onEnter(boolean isFinalStage, GameState gameState) {
        active = isFinalStage;
        awaitingContinue = active;
        spawnWarmup = false;
        spawnWarmupTimer = 0f;
        musicPlayed = false;

        gameState.setControlsEnabled(!active);
        gameState.setSpawningEnabled(!active);
    }

    public boolean update(float delta,
                          Player player,
                          InputOutputManager inputOutputManager,
                          SceneManager sceneManager,
                          GameState gameState,
                          SoundManager soundManager) {
        if (!active) return false;

        if (awaitingContinue && !musicPlayed && soundManager != null) {
            soundManager.startVictoryMusic();
            musicPlayed = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GameOverScene gameOverScene = (GameOverScene) sceneManager.getScene(GameSceneId.GAME_OVER.id());
            gameOverScene.setVictoryEnding(true);
            sceneManager.switchTo(GameSceneId.GAME_OVER.id());
            return true;
        }

        if ((awaitingContinue || spawnWarmup) && player != null) {
            autoPilot(player);
        }

        if (awaitingContinue && inputOutputManager.isActionJustPressed(GameInputAction.FLAP.id())) {
            awaitingContinue = false;
            spawnWarmup = true;
            spawnWarmupTimer = ENDING_SPAWN_DELAY_SECONDS;
            gameState.setControlsEnabled(false);
            
            if (soundManager != null) {
                soundManager.startMusic();
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

    private void autoPilot(Player player) {
        float targetY = dimensions.getWorldHeight() * dimensions.getEndingTargetHeightRatio();
        float playerCenterY = player.getBounds().y + (player.getBounds().height / 2f);
        if (playerCenterY < targetY - dimensions.getEndingFlapWindow()) {
            player.flap();
        }
    }
}
