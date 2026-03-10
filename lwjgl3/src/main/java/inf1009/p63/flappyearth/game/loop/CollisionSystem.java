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

public class CollisionSystem implements StepManager {

    private final EntityManager    entityManager;
    private final CollisionManager collisionManager;
    private final EventManager     eventManager;
    private final GameState        state;
    private final ActiveEffects    activeEffects;

    private static final float INVINCIBILITY_DURATION = 1.0f;

    public CollisionSystem(EntityManager entityManager,
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

        state.tickInvincibility(delta);

        Player player = (Player) entityManager.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        if (!state.isInvincible() && !activeEffects.isShieldActive()) {
            List<Obstacle> obstacles = entityManager.getByType(Obstacle.class);
            float playerCenterX = player.getBounds().x + (player.getBounds().width / 2f);
            float playerCenterY = player.getBounds().y + (player.getBounds().height / 2f);
            for (Obstacle obs : obstacles) {
                // Pipe hits use the bird center to keep collisions forgiving.
                if (collisionManager.containsPoint(obs, playerCenterX, playerCenterY)) {
                    state.loseHeart();
                    state.setInvincible(INVINCIBILITY_DURATION);
                    eventManager.publish(GameEvents.BAD_HIT,
                            new BadHitEvent(obs.getObstacleType().name(), obs.getId()));
                    if (state.isDead()) {
                        state.startDeathSequence(1.25f);
                    }
                    return;
                }
            }

            // Check for passing obstacles (score). Only count each obstacle once and
            // only count the top pipe so pairs are only counted once.
            for (Obstacle obs : obstacles) {
                if (obs.isFlipped() && !obs.isPassed()) {
                    // If player's left x has moved beyond the obstacle's right edge
                    if (player.getX() > obs.getX() + obs.getWidth()) {
                        obs.setPassed(true);
                        eventManager.publish(GameEvents.OBSTACLE_PASSED,
                                new inf1009.p63.flappyearth.game.events.ObstaclePassedEvent(obs.getId()));
                    }
                }
            }
        }

        List<Collectible> collectibles = entityManager.getByType(Collectible.class);
        for (Collectible col : collectibles) {
            // Collectibles still use full overlap so grazing them counts.
            if (collisionManager.overlapsExact(player, col)) {
                if (col.getTag().equals(Tags.COLLECTIBLE_GOOD)) {
                    eventManager.publish(GameEvents.GOOD_COLLECTED,
                            new GoodCollectedEvent(col.getCollectibleType().name(), col.getId()));
                } else if (col.getTag().equals(Tags.COLLECTIBLE_BAD)) {
                    entityManager.queueRemove(col);
                    return;
                }
                entityManager.queueRemove(col);
            }
        }
    }
}