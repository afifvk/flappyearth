package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.game.state.ScoreManager;
import inf1009.p63.flappyearth.game.state.GameState;

public class ScoreStep implements LoopStep {

    private static final float SCORE_TICK_SECONDS = 0.1f;

    private final GameState    state;
    private final ScoreManager scoreManager;
    private float scoreTimer = 0f;

    public ScoreStep(GameState state,
                               ScoreManager scoreManager) {
        this.state        = state;
        this.scoreManager = scoreManager;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        scoreTimer += delta;
        while (scoreTimer >= SCORE_TICK_SECONDS) {
            scoreManager.addPoint();
            scoreTimer -= SCORE_TICK_SECONDS;
        }
    }
}