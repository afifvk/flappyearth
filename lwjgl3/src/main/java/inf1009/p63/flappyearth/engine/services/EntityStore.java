package inf1009.p63.flappyearth.engine.services;

import java.util.ArrayList;
import java.util.List;

import inf1009.p63.flappyearth.engine.model.EntityBase;
import inf1009.p63.flappyearth.engine.interfaces.Collidable;
import inf1009.p63.flappyearth.engine.interfaces.Movable;
import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.interfaces.Updatable;

public class EntityStore {

    private final List<EntityBase> entities   = new ArrayList<>();
    private final List<EntityBase> addQueue   = new ArrayList<>();
    private final List<EntityBase> removeQueue = new ArrayList<>();

    public void queueAdd(EntityBase entity) {
        addQueue.add(entity);
    }

    public void queueRemove(EntityBase entity) {
        removeQueue.add(entity);
    }

    public void flush() {
        entities.addAll(addQueue);
        addQueue.clear();
        entities.removeAll(removeQueue);
        removeQueue.clear();
    }

    public List<EntityBase> getAll() {
        return entities;
    }

    public List<Updatable> getUpdatables() {
        List<Updatable> result = new ArrayList<>();
        for (EntityBase e : entities) {
            if (e instanceof Updatable) result.add((Updatable) e);
        }
        return result;
    }

    public void collectUpdatables(List<Updatable> out) {
        out.clear();
        for (EntityBase e : entities) {
            if (e instanceof Updatable) out.add((Updatable) e);
        }
    }

    public List<Renderable> getRenderables() {
        List<Renderable> result = new ArrayList<>();
        for (EntityBase e : entities) {
            if (e instanceof Renderable) result.add((Renderable) e);
        }
        return result;
    }

    public void collectRenderables(List<Renderable> out) {
        out.clear();
        for (EntityBase e : entities) {
            if (e instanceof Renderable) out.add((Renderable) e);
        }
    }

    public List<Collidable> getCollidables() {
        List<Collidable> result = new ArrayList<>();
        for (EntityBase e : entities) {
            if (e instanceof Collidable) result.add((Collidable) e);
        }
        return result;
    }

    public List<Movable> getMovables() {
        List<Movable> result = new ArrayList<>();
        for (EntityBase e : entities) {
            if (e instanceof Movable) result.add((Movable) e);
        }
        return result;
    }

    public void collectMovables(List<Movable> out) {
        out.clear();
        for (EntityBase e : entities) {
            if (e instanceof Movable) out.add((Movable) e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends EntityBase> List<T> getByType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (EntityBase e : entities) {
            if (type.isInstance(e)) result.add((T) e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T extends EntityBase> void collectByType(Class<T> type, List<T> out) {
        out.clear();
        for (EntityBase e : entities) {
            if (type.isInstance(e)) out.add((T) e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends EntityBase> T getFirstByType(Class<T> type) {
        for (EntityBase e : entities) {
            if (type.isInstance(e)) return (T) e;
        }
        return null;
    }

    public List<EntityBase> getByTag(String tag) {
        List<EntityBase> result = new ArrayList<>();
        if (tag == null) return result;
        for (EntityBase e : entities) {
            if (tag.equals(e.getTag())) result.add(e);
        }
        return result;
    }

    public int countByTag(String tag) {
        if (tag == null) return 0;
        int count = 0;
        for (EntityBase e : entities) {
            if (tag.equals(e.getTag())) count++;
        }
        return count;
    }

    public EntityBase getFirstByTag(String tag) {
        if (tag == null) return null;
        for (EntityBase e : entities) {
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

    /** Move an entity to render last (front). Safe when entity is not present. */
    public void bringToFront(EntityBase e) {
        if (e == null) return;
        // If in addQueue, prefer to leave it; try to operate on main list.
        if (entities.remove(e)) {
            entities.add(e);
        }
    }
}
