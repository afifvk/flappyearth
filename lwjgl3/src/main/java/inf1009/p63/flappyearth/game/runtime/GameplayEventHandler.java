package inf1009.p63.flappyearth.game.runtime;

import inf1009.p63.flappyearth.engine.core.EngineContext;
import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.engine.services.EventBus;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import inf1009.p63.flappyearth.game.entities.Obstacle;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.BadCollectedEvent;
import inf1009.p63.flappyearth.game.events.BadHitEvent;
import inf1009.p63.flappyearth.game.events.GameEvents;
import inf1009.p63.flappyearth.game.factories.EntityFactory;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Handles game events and applies side effects via strategy pattern.
 * Single Responsibility: subscribe to events and delegate effects to specialized handlers.
 */
public class GameplayEventHandler {

    private static final float PLASTIC_BOTTLE_DURATION = 5f;
    private static final float OIL_BLOT_DURATION = 5f;
    private static final float FACTORY_SMOKE_DURATION = 5f;
    private static final float FACTORY_JUMP_INTERVAL_SECONDS = 0.5f;
    private static final float TRASH_PILE_SHAKE_DURATION = 5f;
    private static final float TRASH_PILE_SHAKE_MAGNITUDE = 50f;

    private final EngineContext context;
    private final EntityStore entityStore;
    private final ActiveEffects activeEffects;
    private final Supplier<Player> playerSupplier;
    private final Supplier<EntityFactory> entityFactorySupplier;

    private final Map<String, Consumer<DebuffContext>> debuffStrategies;

    private EventBus.EventListener goodCollectedProgressListener;
    private EventBus.EventListener badCollectedListener;
    private EventBus.EventListener badHitListener;
    private EventBus.EventListener obstaclePassedListener;

    private final List<Obstacle> obstaclesScratch = new ArrayList<>();

    /**
     * Context passed to debuff strategy handlers.
     */
    private static class DebuffContext {
        final Player player;
        final ActiveEffects effects;
        final EngineContext context;

        DebuffContext(Player player, ActiveEffects effects, EngineContext context) {
            this.player = player;
            this.effects = effects;
            this.context = context;
        }
    }

    public GameplayEventHandler(EngineContext context,
                                EntityStore entityStore,
                                ActiveEffects activeEffects,
                                Supplier<Player> playerSupplier,
                                Supplier<EntityFactory> entityFactorySupplier) {
        this.context = context;
        this.entityStore = entityStore;
        this.activeEffects = activeEffects;
        this.playerSupplier = playerSupplier;
        this.entityFactorySupplier = entityFactorySupplier;
        this.debuffStrategies = buildDebuffStrategies();
    }

    /**
     * Build strategy map for collectible effects.
     * Open/Closed Principle: new effects added here without modifying subscribe logic.
     */
    private Map<String, Consumer<DebuffContext>> buildDebuffStrategies() {
        Map<String, Consumer<DebuffContext>> strategies = new HashMap<>();

        strategies.put("PLASTIC_BOTTLE", ctx -> {
            if (ctx.player != null) {
                ctx.player.applyReverseFlightDebuff(PLASTIC_BOTTLE_DURATION);
            }
        });

        strategies.put("OIL_SPILL", ctx -> {
            if (ctx.effects != null) {
                ctx.effects.activateOilBlot(OIL_BLOT_DURATION);
            }
        });

        strategies.put("FACTORY", ctx -> {
            if (ctx.player != null) {
                ctx.player.applyJumpIntervalDebuff(FACTORY_SMOKE_DURATION, FACTORY_JUMP_INTERVAL_SECONDS);
            }
        });

        strategies.put("TRASH_PILE", ctx -> {
            if (ctx.effects != null) {
                ctx.effects.activateTrashRattle(TRASH_PILE_SHAKE_DURATION, TRASH_PILE_SHAKE_MAGNITUDE);
            }
        });

        return strategies;
    }

    public void subscribe(GameSession gameSession) {
        unsubscribe();
        EventBus eventBus = context.getEventBus();

        goodCollectedProgressListener = data -> {
            gameSession.getEnvironmentProgress().addGoodCollectible();
            gameSession.getScoreManager().addPoint();
            context.getAudioManager().playSound(AudioKeys.GOOD_COLLECT);
        };

        badCollectedListener = data -> {
            if (!(data instanceof BadCollectedEvent)) {
                return;
            }
            context.getAudioManager().playSound(AudioKeys.BAD_COLLECT);
            BadCollectedEvent event = (BadCollectedEvent) data;
            Player player = playerSupplier.get();

            // Delegate to strategy map (Open/Closed Principle)
            Consumer<DebuffContext> strategy = debuffStrategies.get(event.collectibleType);
            if (strategy != null) {
                strategy.accept(new DebuffContext(player, activeEffects, context));
            }
        };

        badHitListener = data -> {
            context.getAudioManager().playSound(AudioKeys.OBSTACLE_HIT);
            Player player = playerSupplier.get();
            if (player != null) {
                entityStore.bringToFront(player);
                player.takeDamage(1);
            }

            if (data instanceof BadHitEvent) {
                BadHitEvent event = (BadHitEvent) data;
                int hitId = event.entityId;
                Obstacle hitObstacle = null;
                entityStore.collectByType(Obstacle.class, obstaclesScratch);
                for (Obstacle obstacle : obstaclesScratch) {
                    if (obstacle.getId() == hitId) {
                        hitObstacle = obstacle;
                        break;
                    }
                }

                if (hitObstacle != null) {
                    float vx = context.getRandomManager().range(-120f, 120f);
                    float vy = context.getRandomManager().range(150f, 300f);
                    hitObstacle.startCrash(vx, vy, 1.2f);

                    EntityFactory entityFactory = entityFactorySupplier.get();
                    if (entityFactory != null) {
                        entityFactory.spawnDebrisForObstacle(entityStore, hitObstacle);
                    }
                }
            }

            if (activeEffects != null) {
                activeEffects.activateScreenShake(0.35f, 12f);
            }
        };

        obstaclePassedListener = data -> {
            gameSession.getScoreManager().addPoint();
            context.getAudioManager().playSound(AudioKeys.OBSTACLE_PASS);
        };

        eventBus.subscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);
        eventBus.subscribe(GameEvents.BAD_COLLECTED, badCollectedListener);
        eventBus.subscribe(GameEvents.BAD_HIT, badHitListener);
        eventBus.subscribe(GameEvents.OBSTACLE_PASSED, obstaclePassedListener);
    }

    public void unsubscribe() {
        EventBus eventBus = context.getEventBus();
        if (goodCollectedProgressListener != null) {
            eventBus.unsubscribe(GameEvents.GOOD_COLLECTED, goodCollectedProgressListener);
            goodCollectedProgressListener = null;
        }
        if (badCollectedListener != null) {
            eventBus.unsubscribe(GameEvents.BAD_COLLECTED, badCollectedListener);
            badCollectedListener = null;
        }
        if (badHitListener != null) {
            eventBus.unsubscribe(GameEvents.BAD_HIT, badHitListener);
            badHitListener = null;
        }
        if (obstaclePassedListener != null) {
            eventBus.unsubscribe(GameEvents.OBSTACLE_PASSED, obstaclePassedListener);
            obstaclePassedListener = null;
        }
    }
}
