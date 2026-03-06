package inf1009.p63.flappyearth.engine.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    @FunctionalInterface
    public interface EventListener {
        void onEvent(Object data);
    }

    private final Map<String, List<EventListener>> listeners = new HashMap<>();

    // Register listener for event type
    public void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    // Unregister listener
    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> list = listeners.get(eventType);
        if (list != null) list.remove(listener);
    }

    // Send event to all listening subscribers
    public void publish(String eventType, Object data) {
        List<EventListener> list = listeners.get(eventType);
        if (list == null) return;
        for (EventListener listener : new ArrayList<>(list)) {
            listener.onEvent(data);
        }
    }

    public void clearAll() {
        listeners.clear();
    }
}
