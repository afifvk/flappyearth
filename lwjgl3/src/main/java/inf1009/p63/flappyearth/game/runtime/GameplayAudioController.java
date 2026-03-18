package inf1009.p63.flappyearth.game.runtime;

import inf1009.p63.flappyearth.engine.services.AudioManager;
import inf1009.p63.flappyearth.engine.services.EventBus;
import inf1009.p63.flappyearth.game.config.AudioKeys;
import inf1009.p63.flappyearth.game.entities.Player;
import inf1009.p63.flappyearth.game.events.GameEvents;

import java.util.function.Supplier;

public class GameplayAudioController {

    private final EventBus eventBus;
    private final AudioManager audioManager;
    private EventBus.EventListener flapRequestedListener;

    public GameplayAudioController(EventBus eventBus, AudioManager audioManager) {
        this.eventBus = eventBus;
        this.audioManager = audioManager;
    }

    public void onEnter(Supplier<Player> playerSupplier) {
        flapRequestedListener = data -> {
            Player player = playerSupplier.get();
            if (player != null && player.flap()) {
                audioManager.playSound(AudioKeys.FLAP);
            }
        };
        eventBus.subscribe(GameEvents.FLAP_REQUESTED, flapRequestedListener);
    }

    public void onExit() {
        if (flapRequestedListener != null) {
            eventBus.unsubscribe(GameEvents.FLAP_REQUESTED, flapRequestedListener);
            flapRequestedListener = null;
        }
    }
}
