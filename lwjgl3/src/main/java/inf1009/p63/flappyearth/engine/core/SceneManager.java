package inf1009.p63.flappyearth.engine.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class SceneManager {

    private Scene current;
    private final Stack<Scene> stack = new Stack<>();
    private final Map<String, Scene> scenes = new HashMap<>();

    public void registerScene(String sceneId, Scene scene) {
        if (sceneId == null || sceneId.trim().isEmpty()) {
            throw new IllegalArgumentException("Scene ID cannot be null or blank");
        }
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }
        scenes.put(sceneId, scene);
    }

    public Scene getScene(String sceneId) {
        return scenes.get(sceneId);
    }

    public void switchTo(String sceneId) {
        Scene next = getScene(sceneId);
        if (next == null) {
            throw new IllegalArgumentException("Scene not found for ID: " + sceneId);
        }
        setScene(next);
    }

    public void setScene(Scene next) {
        if (current != null) {
            current.onExit();
        }
        while (!stack.isEmpty()) {
            Scene s = stack.pop();
            s.onExit();
        }
        current = next;
        if (current != null) {
            current.onEnter();
        }
    }

    public void pushScene(Scene next) {
        if (current != null) {
            stack.push(current);
        }
        current = next;
        if (current != null) {
            current.onEnter();
        }
    }

    public void popScene() {
        if (current != null) {
            current.onExit();
        }
        current = stack.isEmpty() ? null : stack.pop();
    }

    public void update(float delta) {
        if (current != null) {
            current.update(delta);
        }
    }

    public void render() {
        if (current != null) {
            current.render();
        }
    }

    public Scene getCurrent() {
        return current;
    }

    public void dispose() {
        if (current != null) {
            current.onExit();
        }
        while (!stack.isEmpty()) {
            Scene s = stack.pop();
            s.onExit();
        }

        Set<Scene> disposedScenes = new HashSet<>();
        for (Scene scene : scenes.values()) {
            if (disposedScenes.add(scene)) {
                scene.disposeResources();
            }
        }

        scenes.clear();
        current = null;
    }
}
