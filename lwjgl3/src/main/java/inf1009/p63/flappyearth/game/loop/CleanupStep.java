package inf1009.p63.flappyearth.game.loop;

import com.badlogic.gdx.Gdx;
import inf1009.p63.flappyearth.engine.entities.Entity;
import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Player;

import java.util.List;

public class CleanupStep implements StepManager {

    private static final float CLEANUP_MARGIN = 200f;

    private final EntityManager entityManager;

    public CleanupStep(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void execute(float delta) {
        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        float cullX = player.getBounds().x - Gdx.graphics.getWidth() - CLEANUP_MARGIN;

        List<Entity> all = entityManager.getAll();
        for (Entity e : all) {
            if (e instanceof Player) continue;
            if (e.getBounds().x + e.getBounds().width < cullX) {
                entityManager.queueRemove(e);
            }
        }
    }
}
