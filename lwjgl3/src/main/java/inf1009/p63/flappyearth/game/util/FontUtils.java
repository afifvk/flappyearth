package inf1009.p63.flappyearth.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public final class FontUtils {

    private FontUtils() {}

    public static BitmapFont loadFontOrDefault(String internalPath, int size) {
        if (!Gdx.files.internal(internalPath).exists()) {
            return new BitmapFont();
        }

        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal(internalPath));
            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
            params.size = size;
            params.minFilter = Texture.TextureFilter.Linear;
            params.magFilter = Texture.TextureFilter.Linear;
            return generator.generateFont(params);
        } catch (Exception e) {
            return new BitmapFont();
        } finally {
            if (generator != null) generator.dispose();
        }
    }
}
