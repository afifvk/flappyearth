package inf1009.p63.flappyearth.game.systems;

import inf1009.p63.flappyearth.engine.interfaces.LoopStep;
import inf1009.p63.flappyearth.engine.services.CollisionManager;
import inf1009.p63.flappyearth.engine.services.EventBus;
import inf1009.p63.flappyearth.game.config.Tags;
import inf1009.p63.flappyearth.game.entities.Collectible;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.BadCollectedEvent;
import inf1009.p63.flappyearth.game.events.BadHitEvent;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.events.GoodCollectedEvent;
import inf1009.p63.flappyearth.game.runtime.GameplayRuntimeContext;

import java.util.ArrayList;
import java.util.List;

public class CollisionStep implements LoopStep {

    private final GameplayRuntimeContext runtimeContext;
    private final CollisionManager collisionManager;
    private final EventBus     eventBus;
    private final List<Obstacle> obstaclesScratch = new ArrayList<>();
    private final List<Collectible> collectiblesScratch = new ArrayList<>();

    private static final float INVINCIBILITY_DURATION = 1.0f;

    public CollisionStep(GameplayRuntimeContext runtimeContext,
                           CollisionManager collisionManager,
                           EventBus eventBus) {
        this.runtimeContext = runtimeContext;
        this.collisionManager = collisionManager;
        this.eventBus     = eventBus;
    }

    @Override
    public void execute(float delta) {
        if (!runtimeContext.gameState().isAlive()) return;

        runtimeContext.gameState().tickInvincibility(delta);

        Player player = runtimeContext.player();
        if (player == null) return;

        if (!runtimeContext.gameState().isInvincible()) {
            runtimeContext.entityStore().collectByType(Obstacle.class, obstaclesScratch);
            float playerCenterX = player.getBounds().x + (player.getBounds().width / 2f);
            float playerCenterY = player.getBounds().y + (player.getBounds().height / 2f);
            for (Obstacle obs : obstaclesScratch) {
                // Pipe hits use the bird center to keep collisions forgiving.
                if (collisionManager.containsPoint(obs, playerCenterX, playerCenterY)) {
                    markObstacleColumnAsScored(obstaclesScratch, obs);
                    runtimeContext.gameState().loseHeart();
                    runtimeContext.gameState().setInvincible(INVINCIBILITY_DURATION);
                    eventBus.publish(GameEvents.BAD_HIT,
                            new BadHitEvent(obs.getObstacleType().name(), obs.getId()));
                    if (runtimeContext.gameState().isDead()) {
                        runtimeContext.gameState().startDeathSequence(1.25f);
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

        runtimeContext.entityStore().collectByType(Collectible.class, collectiblesScratch);
        for (Collectible col : collectiblesScratch) {
            // Collectibles still use full overlap so grazing them counts.
            if (collisionManager.overlapsExact(player, col)) {
                if (col.getTag().equals(Tags.COLLECTIBLE_GOOD)) {
                    eventBus.publish(GameEvents.GOOD_COLLECTED,
                            new GoodCollectedEvent(col.getCollectibleType().name(), col.getId()));
                } else if (col.getTag().equals(Tags.COLLECTIBLE_BAD)) {
                    eventBus.publish(GameEvents.BAD_COLLECTED,
                            new BadCollectedEvent(col.getCollectibleType().name(), col.getId()));
                    runtimeContext.entityStore().queueRemove(col);
                    return;
                }
                runtimeContext.entityStore().queueRemove(col);
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
