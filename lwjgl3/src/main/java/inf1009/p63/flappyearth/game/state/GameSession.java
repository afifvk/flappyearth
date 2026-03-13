package inf1009.p63.flappyearth.game.state;

import inf1009.p63.flappyearth.game.managers.ScoreManager;

public class GameSession {

    private final GameState gameState = new GameState();
    private final ActiveEffects activeEffects = new ActiveEffects();
    private final ScoreManager scoreManager = new ScoreManager();
    private final EnvironmentProgress environmentProgress;

    public GameSession(int finalTargetGoodCollectibles) {
        this.environmentProgress = new EnvironmentProgress(finalTargetGoodCollectibles);
    }

    public GameState getGameState() {
        return gameState;
    }

    public ActiveEffects getActiveEffects() {
        return activeEffects;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public EnvironmentProgress getEnvironmentProgress() {
        return environmentProgress;
    }

    public void resetForNewRun() {
        gameState.reset();
        activeEffects.reset();
        scoreManager.reset();
        environmentProgress.reset();
    }

public void prepareForStageEntry() {
    gameState.setAlive(true);
    gameState.clearSceneRequest();
    gameState.setInvincible(0f);
    activeEffects.reset();
}
}
