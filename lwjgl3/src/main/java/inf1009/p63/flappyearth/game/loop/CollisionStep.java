package inf1009.p63.flappyearth.game.loop;

import inf1009.p63.flappyearth.engine.interfaces.StepManager;
import inf1009.p63.flappyearth.engine.managers.CollisionManager;
import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Collectible;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.BadHitEvent;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.events.GoodCollectedEvent;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.List;

public class CollisionStep implements StepManager {

    private final EntityManager    entityManager;
    private final CollisionManager collisionManager;
    private final EventManager     eventManager;
    private final GameState state;
    private final ActiveEffects    activeEffects;

    public CollisionStep(EntityManager entityManager,
                                CollisionManager collisionManager,
                                EventManager eventManager,
                                GameState state,
                                ActiveEffects activeEffects) {
        this.entityManager    = entityManager;
        this.collisionManager = collisionManager;
        this.eventManager     = eventManager;
        this.state            = state;
        this.activeEffects    = activeEffects;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        // Check collisions with obstacles
        List<Obstacle> obstacles = entityManager.getByType(Obstacle.class);
        for (Obstacle obs : obstacles) {
            if (collisionManager.overlaps(player, obs)) {
                // Shield blocks damage
                if (!activeEffects.isShieldActive()) {
                    eventManager.publish(GameEvents.BAD_HIT,
                            new BadHitEvent(obs.getObstacleType().name(), obs.getId()));
                }
                return;
            }
        }

        // Check collisions with collectibles
        List<Collectible> collectibles = entityManager.getByType(Collectible.class);
        for (Collectible col : collectibles) {
            if (collisionManager.overlaps(player, col)) {
                if (col.getTag().equals(Tags.COLLECTIBLE_GOOD)) {
                    eventManager.publish(GameEvents.GOOD_COLLECTED,
                            new GoodCollectedEvent(col.getCollectibleType().name(), col.getId()));
                } else if (col.getTag().equals(Tags.COLLECTIBLE_BAD)) {
                    // Bad collectibles are currently just consumed with no damage.
                }
                entityManager.queueRemove(col);
            }
        }
    }
}
