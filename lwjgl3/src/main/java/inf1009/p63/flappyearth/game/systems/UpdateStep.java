package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.interfaces.Updatable;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.TimeManager;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.ArrayList;
import java.util.List;

public class UpdateStep implements LoopStep {

    private final EntityStore    entityStore;
    private final TimeManager      timeManager;
    private final GameState state;
    private final List<Updatable> updatablesScratch = new ArrayList<>();

    public UpdateStep(EntityStore entityStore,
                             TimeManager timeManager,
                             GameState state) {
        this.entityStore = entityStore;
        this.timeManager   = timeManager;
        this.state         = state;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        float scaledDelta = timeManager.scale(delta);
        entityStore.collectUpdatables(updatablesScratch);
        for (Updatable u : updatablesScratch) {
            u.update(scaledDelta);
        }
    }
}
