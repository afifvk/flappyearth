package inf1009.p63.flappyearth.engine.services;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {

    private final com.badlogic.gdx.assets.AssetManager gdxAssets;

    public AssetManager() {
        gdxAssets = new com.badlogic.gdx.assets.AssetManager(
                new InternalFileHandleResolver());
    }

    public <T> void load(String path, Class<T> type) {
        gdxAssets.load(path, type);
    }

    public void finishLoading() {
        gdxAssets.finishLoading();
    }

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
