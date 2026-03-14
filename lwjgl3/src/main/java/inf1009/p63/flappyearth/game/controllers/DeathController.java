package inf1009.p63.flappyearth.game.controllers;

import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.state.GameState;

public class DeathController {

    private static final float GAME_OVER_DELAY_SECONDS = 1.0f;

    private final SceneManager sceneManager;
    private final GameSession gameSession;

    private boolean gameOverDelayActive;
    private float gameOverDelayTimer;

    public DeathController(SceneManager sceneManager, GameSession gameSession) {
        this.sceneManager = sceneManager;
        this.gameSession = gameSession;
    }

    public void onEnter() {
        gameOverDelayActive = false;
        gameOverDelayTimer = 0f;
    }

    public void update(float delta,
                       Player player,
                       GameCameraController cameraController,
                       boolean safeEndingWindow) {
        GameState gameState = gameSession.getGameState();

        if (gameState.isDeathSequenceActive()) {
            if (player == null) return;

            if (!player.isDeathFallActive()) {
                startDeathSequence(player, gameState.getDeathFallSpeedMultiplier(), cameraController);
            }

            player.update(delta);
            player.movement(delta);

            if (!gameOverDelayActive && isFullyBelowScreen(player)) {
                gameOverDelayActive = true;
                gameOverDelayTimer = GAME_OVER_DELAY_SECONDS;
            }
            if (gameOverDelayActive) {
                gameOverDelayTimer = Math.max(0f, gameOverDelayTimer - delta);
            }
            if (gameOverDelayActive && gameOverDelayTimer <= 0f) {
                gameState.finishDeathSequence();
            }
            return;
        }

        if (!safeEndingWindow && player != null && isFullyBelowScreen(player)) {
            startDeathSequence(player, 1f, cameraController);
        }
    }

    public boolean routeToGameOverIfRequested() {
        GameState gameState = gameSession.getGameState();
        if (gameState.getRequestedScene() != GameState.SceneRequest.GAME_OVER) {
            return false;
        }

        GameOverScene gameOverScene = (GameOverScene) sceneManager.getScene(GameSceneId.GAME_OVER.id());
        gameOverScene.setScore(gameSession.getScoreManager().getCurrentScore());
        gameOverScene.setVictoryEnding(false);
        sceneManager.switchTo(GameSceneId.GAME_OVER.id());
        return true;
    }

    private void startDeathSequence(Player player,
                                    float deathFallSpeedMultiplier,
                                    GameCameraController cameraController) {
        if (player == null) return;

        GameState gameState = gameSession.getGameState();
        if (!gameState.isDeathSequenceActive()) {
            gameState.startDeathSequence(deathFallSpeedMultiplier);
        }

        player.startDeathFall(gameState.getDeathFallSpeedMultiplier());
        cameraController.lockAt(cameraController.getCamera().position.x);

        if (!gameOverDelayActive && isFullyBelowScreen(player)) {
            gameOverDelayActive = true;
            gameOverDelayTimer = GAME_OVER_DELAY_SECONDS;
        }
    }

    private boolean isFullyBelowScreen(Player player) {
        return player.getBounds().y + player.getBounds().height < 0f;
    }
}
