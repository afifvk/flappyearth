package inf1009.p63.flappyearth.game.entities;

import com.badlogic.gdx.math.Rectangle;
import inf1009.p63.flappyearth.engine.entities.Entity;
import inf1009.p63.flappyearth.engine.entities.RenderData;
import inf1009.p63.flappyearth.engine.interfaces.Collidable;
import inf1009.p63.flappyearth.engine.interfaces.Renderable;
import inf1009.p63.flappyearth.engine.interfaces.Updatable;

public abstract class GameEntity extends Entity implements Updatable, Renderable, Collidable {

    private static int idCounter = 0;

    private final int    id;
    private final String assetKey;
    private float        colorR, colorG, colorB;

    public GameEntity(float x, float y, float width, float height, String assetKey) {
        this(x, y, width, height, assetKey, null);
    }

    public GameEntity(float x, float y, float width, float height, String assetKey, String tag) {
        super(x, y, width, height, tag);
        this.id       = ++idCounter;
        this.assetKey = assetKey;
        this.colorR   = 1f;
        this.colorG   = 1f;
        this.colorB   = 1f;
    }


    @Override
    public RenderData getRenderData() {
        Rectangle b = getBounds();
        boolean flipped = false;
        if (this instanceof Obstacle) {
            flipped = ((Obstacle) this).isFlipped();
        }
        return new RenderData(assetKey, b.x, b.y, b.width, b.height,
                              colorR, colorG, colorB, flipped);
    }


    @Override
    public abstract void update(float delta);

    public int    getId()       { return id; }
    public String getAssetKey() { return assetKey; }
    public float  getColorR()   { return colorR; }
    public float  getColorG()   { return colorG; }
    public float  getColorB()   { return colorB; }

    public void setColor(float r, float g, float b) {
        colorR = r; colorG = g; colorB = b;
    }
}
