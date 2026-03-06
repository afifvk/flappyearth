package inf1009.p63.flappyearth.engine.managers;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {

    private final com.badlogic.gdx.assets.AssetManager gdxAssets;

    public AssetManager() {
        // Initialize GDX asset manager
        gdxAssets = new com.badlogic.gdx.assets.AssetManager(
                new InternalFileHandleResolver());
    }

    // Queue asset for loading
    public <T> void load(String path, Class<T> type) {
        gdxAssets.load(path, type);
    }

    // Wait for all queued assets to finish loading
    public void finishLoading() {
        gdxAssets.finishLoading();
    }

    // Update loading progress, return true when done
    public boolean update() {
        return gdxAssets.update();
    }

    public <T> T get(String path, Class<T> type) {
        return gdxAssets.get(path, type);
    }

    public Texture getTexture(String path) {
        return gdxAssets.get(path, Texture.class);
    }

    public boolean isLoaded(String path) {
        return gdxAssets.isLoaded(path);
    }

    public void unload(String path) {
        if (gdxAssets.isLoaded(path)) {
            gdxAssets.unload(path);
        }
    }

    public void dispose() {
        gdxAssets.dispose();
    }
}
