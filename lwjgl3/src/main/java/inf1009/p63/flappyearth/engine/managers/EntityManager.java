package inf1009.p63.flappyearth.engine.managers;

import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.interfaces.Collidable;
import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.engine.interfaces.Updatable;
import inf1009.p63.flappyearth.engine.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    private final List<Entity> entities   = new ArrayList<>();
    private final List<Entity> addQueue   = new ArrayList<>();
    private final List<Entity> removeQueue = new ArrayList<>();

    public void queueAdd(Entity entity) {
        addQueue.add(entity);
    }

    public void queueRemove(Entity entity) {
        removeQueue.add(entity);
    }

    public void flush() {
        // Apply queued add/remove operations - deferred to end of frame
        entities.addAll(addQueue);
        addQueue.clear();
        entities.removeAll(removeQueue);
        removeQueue.clear();
    }

    public List<Entity> getAll() {
        return entities;
    }

    // Get entities implementing Updatable interface
    public List<Updatable> getUpdatables() {
        List<Updatable> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e instanceof Updatable) result.add((Updatable) e);
        }
        return result;
    }

    // Get entities implementing Renderable interface
    public List<Renderable> getRenderables() {
        List<Renderable> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e instanceof Renderable) result.add((Renderable) e);
        }
        return result;
    }

    // Get entities implementing iCollidable interface
    public List<Collidable> getCollidables() {
        List<Collidable> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e instanceof Collidable) result.add((Collidable) e);
        }
        return result;
    }

    // Get entities implementing iMovable interface
    public List<Movable> getMovables() {
        List<Movable> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e instanceof Movable) result.add((Movable) e);
        }
        return result;
    }

    // Get entities by exact class type
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> getByType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Entity e : entities) {
            if (type.isInstance(e)) result.add((T) e);
        }
        return result;
    }

    // Get first entity by exact class type
    @SuppressWarnings("unchecked")
    public <T extends Entity> T getFirstByType(Class<T> type) {
        for (Entity e : entities) {
            if (type.isInstance(e)) return (T) e;
        }
        return null;
    }

    // Get entities by tag
    public List<Entity> getByTag(String tag) {
        List<Entity> result = new ArrayList<>();
        if (tag == null) return result;
        for (Entity e : entities) {
            if (tag.equals(e.getTag())) result.add(e);
        }
        return result;
    }

    // Get first entity by tag
    public Entity getFirstByTag(String tag) {
        if (tag == null) return null;
        for (Entity e : entities) {
            if (tag.equals(e.getTag())) return e;
        }
        return null;
    }

    public void clear() {
        entities.clear();
        addQueue.clear();
        removeQueue.clear();
    }

    public void dispose() {
        clear();
    }
}
