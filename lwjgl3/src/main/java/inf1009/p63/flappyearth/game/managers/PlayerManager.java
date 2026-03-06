package inf1009.p63.flappyearth.game.managers;

import inf1009.p63.flappyearth.engine.managers.EntityManager;
import inf1009.p63.flappyearth.engine.managers.EventManager;
import inf1009.p63.flappyearth.game.config.GameConfig;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.GameEvents;

public class PlayerManager {

    private Player player;
    private final EntityManager entityManager;

    public PlayerManager(EntityManager entityManager, EventManager eventManager,
                         GameConfig config) {
        this.entityManager = entityManager;

        // Listen for flap input and make player jump
        eventManager.subscribe(GameEvents.FLAP_REQUESTED, data -> {
            if (player != null) player.flap();
        });
    }

    public void spawnPlayer(float startX, float startY, GameConfig config) {
        player = new Player(startX, startY, config.playerSpeed,
                            config.gravity, config.jumpImpulse);
        entityManager.queueAdd(player);
    }

    public Player getPlayer() {
        return player;
    }
}
