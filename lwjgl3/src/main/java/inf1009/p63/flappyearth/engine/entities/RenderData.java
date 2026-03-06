package inf1009.p63.flappyearth.engine.entities;

public class RenderData {
    public final String assetKey;
    public final float x, y, width, height;
    public final float r, g, b;
    public final boolean isFlipped;

    public RenderData(String assetKey, float x, float y, float width, float height,
                      float r, float g, float b, boolean isFlipped) {
        this.assetKey = assetKey;
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.r = r; this.g = g; this.b = b;
        this.isFlipped = isFlipped;
    }

    public RenderData(String assetKey, float x, float y, float width, float height,
                      float r, float g, float b) {
        this(assetKey, x, y, width, height, r, g, b, false);
    }

    public RenderData(String assetKey, float x, float y, float width, float height) {
        this(assetKey, x, y, width, height, 1f, 1f, 1f, false);
    }
}
