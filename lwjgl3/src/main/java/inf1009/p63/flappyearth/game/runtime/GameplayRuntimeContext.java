package inf1009.p63.flappyearth.game.runtime;

import inf1009.p63.flappyearth.engine.services.EntityStore;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.state.ActiveEffects;
import inf1009.p63.flappyearth.game.state.GameSession;
import inf1009.p63.flappyearth.game.state.GameState;

/**
 * Game-side runtime references shared by gameplay systems.
 * Keeps gameplay wiring explicit without leaking any game concepts into the engine layer.
 */
public class GameplayRuntimeContext {

    private final EntityStore entityStore;
    private final GameSession gameSession;
    private final GameplayDimensions dimensions;
    private Player player;

    public GameplayRuntimeContext(EntityStore entityStore,
                                  GameSession gameSession,
                                  GameplayDimensions dimensions) {
        this.entityStore = entityStore;
        this.gameSession = gameSession;
        this.dimensions = dimensions;
    }

    public EntityStore entityStore() {
        return entityStore;
    }

    public GameSession gameSession() {
        return gameSession;
    }

    public GameState gameState() {
        return gameSession.getGameState();
    }

    public ActiveEffects activeEffects() {
        return gameSession.getActiveEffects();
    }

    public GameplayDimensions dimensions() {
        return dimensions;
    }

    public Player player() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
