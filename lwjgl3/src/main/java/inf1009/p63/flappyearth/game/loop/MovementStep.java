package inf1009.p63.flappyearth.game.loop;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.MovementManager;
import inf1009.p63.flappyearth.engine.managers.TimeManager;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.List;

public class MovementStep implements StepManager {

    private final EntityManager  entityManager;
    private final MovementManager movementManager;
    private final TimeManager    timeManager;
    private final GameState state;

    public MovementStep(EntityManager entityManager,
                               MovementManager movementManager,
                               TimeManager timeManager,
                               GameState state) {
        this.entityManager   = entityManager;
        this.movementManager = movementManager;
        this.timeManager     = timeManager;
        this.state           = state;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        entityManager.flush();

        float scaledDelta = timeManager.scale(delta);
        List<Movable> movables = entityManager.getMovables();
        movementManager.moveAll(movables, scaledDelta);

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player != null) {
            float screenH = Gdx.graphics.getHeight();
            float playerY = player.getBounds().y;
            float playerH = player.getBounds().height;

            if (playerY + playerH > screenH) {
                player.getBounds().y = screenH - playerH;
            }
        }
    }
}
