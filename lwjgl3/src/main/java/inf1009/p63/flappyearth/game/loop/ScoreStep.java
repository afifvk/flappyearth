package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.managers.ScoreManager;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.List;

public class ScoreStep implements StepManager {

    private final EntityManager entityManager;
    private final GameState     state;
    private final ScoreManager  scoreManager;

    public ScoreStep(EntityManager entityManager,
                     GameState state,
                     ScoreManager scoreManager) {
        this.entityManager = entityManager;
        this.state         = state;
        this.scoreManager  = scoreManager;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        float playerRightEdge = player.getBounds().x + player.getBounds().width;

        List<Obstacle> obstacles = entityManager.getByType(Obstacle.class);
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isFlipped()) continue;
            if (!obstacle.isPassed() &&
                    obstacle.getBounds().x + obstacle.getBounds().width < playerRightEdge) {
                obstacle.setPassed(true);
                scoreManager.addPoint();
            }
        }
    }
}