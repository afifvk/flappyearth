package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.interfaces.Updatable;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.TimeManager;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.List;

public class UpdateStep implements StepManager {

    private final EntityManager    entityManager;
    private final TimeManager      timeManager;
    private final GameState state;

    public UpdateStep(EntityManager entityManager,
                             TimeManager timeManager,
                             GameState state) {
        this.entityManager = entityManager;
        this.timeManager   = timeManager;
        this.state         = state;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        float scaledDelta = timeManager.scale(delta);
        List<Updatable> updatables = entityManager.getUpdatables();
        for (Updatable u : updatables) {
            u.update(scaledDelta);
        }
    }
}
