package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.CollisionManager;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.EventBus;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Collectible;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.BadCollectedEvent;
import inf1009.p63.flappyearth.game.events.BadHitEvent;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.events.GoodCollectedEvent;
import inf1009.p63.flappyearth.game.state.GameState;

import java.util.ArrayList;
import java.util.List;

public class CollisionStep implements LoopStep {

    private final EntityStore    entityStore;
    private final CollisionManager collisionManager;
    private final EventBus     eventBus;
    private final GameState        state;
    private final List<Obstacle> obstaclesScratch = new ArrayList<>();
    private final List<Collectible> collectiblesScratch = new ArrayList<>();

    private static final float INVINCIBILITY_DURATION = 1.0f;

    public CollisionStep(EntityStore entityStore,
                           CollisionManager collisionManager,
                           EventBus eventBus,
                           GameState state) {
        this.entityStore    = entityStore;
        this.collisionManager = collisionManager;
        this.eventBus     = eventBus;
        this.state            = state;
    }

    @Override
    public void execute(float delta) {
        if (!state.isAlive()) return;

        state.tickInvincibility(delta);

        Player player = (Player) entityStore.getFirstByTag(Tags.PLAYER);
        if (player == null) return;

        if (!state.isInvincible()) {
            entityStore.collectByType(Obstacle.class, obstaclesScratch);
            float playerCenterX = player.getBounds().x + (player.getBounds().width / 2f);
            float playerCenterY = player.getBounds().y + (player.getBounds().height / 2f);
            for (Obstacle obs : obstaclesScratch) {
                // Pipe hits use the bird center to keep collisions forgiving.
                if (collisionManager.containsPoint(obs, playerCenterX, playerCenterY)) {
                    markObstacleColumnAsScored(obstaclesScratch, obs);
                    state.loseHeart();
                    state.setInvincible(INVINCIBILITY_DURATION);
                    eventBus.publish(GameEvents.BAD_HIT,
                            new BadHitEvent(obs.getObstacleType().name(), obs.getId()));
                    if (state.isDead()) {
                        state.startDeathSequence(1.25f);
                    }
                    return;
                }
            }

            // Check for passing obstacles (score). Only count each obstacle once and
            // only count the top pipe so pairs are only counted once.
            for (Obstacle obs : obstaclesScratch) {
                if (obs.isFlipped() && !obs.isPassed()) {
                    // If player's left x has moved beyond the obstacle's right edge
                    if (player.getX() > obs.getX() + obs.getWidth()) {
                        obs.setPassed(true);
                        eventBus.publish(GameEvents.OBSTACLE_PASSED,
                                new inf1009.p63.flappyearth.game.events.ObstaclePassedEvent(obs.getId()));
                    }
                }
            }
        }

        entityStore.collectByType(Collectible.class, collectiblesScratch);
        for (Collectible col : collectiblesScratch) {
            // Collectibles still use full overlap so grazing them counts.
            if (collisionManager.overlapsExact(player, col)) {
                if (col.getTag().equals(Tags.COLLECTIBLE_GOOD)) {
                    eventBus.publish(GameEvents.GOOD_COLLECTED,
                            new GoodCollectedEvent(col.getCollectibleType().name(), col.getId()));
                } else if (col.getTag().equals(Tags.COLLECTIBLE_BAD)) {
                    eventBus.publish(GameEvents.BAD_COLLECTED,
                            new BadCollectedEvent(col.getCollectibleType().name(), col.getId()));
                    entityStore.queueRemove(col);
                    return;
                }
                entityStore.queueRemove(col);
            }
        }
    }

    private void markObstacleColumnAsScored(List<Obstacle> obstacles, Obstacle hitObstacle) {
        float hitColumnX = hitObstacle.getX();
        for (Obstacle obstacle : obstacles) {
            if (Math.abs(obstacle.getX() - hitColumnX) < 0.001f) {
                obstacle.setPassed(true);
            }
        }
    }
}
