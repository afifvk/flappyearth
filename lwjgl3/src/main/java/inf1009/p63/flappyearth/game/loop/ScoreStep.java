package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.managers.ScoreManager;
import inf1009.p63.flappyearth.game.state.GameState;

public class ScoreStep implements StepManager {

    private final EntityManager    entityManager;
    private final GameState state;
    private final ScoreManager     scoreManager;

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
        
        // Increase score based on forward movement
        scoreManager.addDistance(player.getVelocityX() * delta);
    }
}
