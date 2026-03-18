package inf1009.p63.flappyearth.game.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class CollectibleSourceBoundsResolver {

    private final Map<String, int[]> boundsCache = new HashMap<>();

    public int[] resolve(String assetKey, Texture texture) {
        if (assetKey == null || !assetKey.startsWith("textures/entities/collectibles/")) {
            return new int[] {0, 0, texture.getWidth(), texture.getHeight()};
        }

        int[] cached = boundsCache.get(assetKey);
        if (cached != null) {
            return cached;
        }

        int[] full = new int[] {0, 0, texture.getWidth(), texture.getHeight()};
        if (!Gdx.files.internal(assetKey).exists()) {
            boundsCache.put(assetKey, full);
            return full;
        }

        Pixmap pixmap = null;
        try {
            pixmap = new Pixmap(Gdx.files.internal(assetKey));
            int minX = pixmap.getWidth();
            int minY = pixmap.getHeight();
            int maxX = -1;
            int maxY = -1;

            for (int y = 0; y < pixmap.getHeight(); y++) {
                for (int x = 0; x < pixmap.getWidth(); x++) {
                    int pixel = pixmap.getPixel(x, y);
                    int alpha = pixel & 0x000000ff;
                    if (alpha == 0) {
                        continue;
                    }
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }

            int[] bounds;
            if (maxX < minX || maxY < minY) {
                bounds = full;
            } else {
                bounds = new int[] {minX, minY, (maxX - minX) + 1, (maxY - minY) + 1};
            }
            boundsCache.put(assetKey, bounds);
            return bounds;
        } catch (Exception e) {
            boundsCache.put(assetKey, full);
            return full;
        } finally {
            if (pixmap != null) {
                pixmap.dispose();
            }
        }
    }

    public void clear() {
        boundsCache.clear();
    }
}
