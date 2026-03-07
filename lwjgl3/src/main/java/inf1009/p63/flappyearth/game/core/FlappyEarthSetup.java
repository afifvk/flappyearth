package inf1009.p63.flappyearth.game.core;

import com.badlogic.gdx.graphics.Texture;
import inf1009.p63.flappyearth.engine.core.GameContextManager;
import inf1009.p63.flappyearth.engine.core.GameSetup;
import inf1009.p63.flappyearth.engine.core.SceneManager;
import inf1009.p63.flappyearth.game.input.KeyboardInputDevice;
import inf1009.p63.flappyearth.game.input.TouchInputDevice;
import inf1009.p63.flappyearth.game.scenes.GameOverScene;
import inf1009.p63.flappyearth.game.scenes.GameScene;
import inf1009.p63.flappyearth.game.scenes.GameSceneId;
import inf1009.p63.flappyearth.game.scenes.MenuScene;

public class FlappyEarthSetup implements GameSetup {

    @Override
    public void configure(GameContextManager contextManager, SceneManager sceneManager) {
        contextManager.getInputOutputManager().registerInputDevice(new KeyboardInputDevice());
        contextManager.getInputOutputManager().registerInputDevice(new TouchInputDevice());

        contextManager.getAssetManager().load("pipe.png", Texture.class);
        contextManager.getAssetManager().load("background.png", Texture.class);
        contextManager.getAssetManager().load("flappy00.png", Texture.class);
        contextManager.getAssetManager().load("flappy01.png", Texture.class);
        contextManager.getAssetManager().load("flappy02.png", Texture.class);
        contextManager.getAssetManager().load("flappy03.png", Texture.class);
        contextManager.getAssetManager().load("flappy04.png", Texture.class);
        contextManager.getAssetManager().load("flappy05.png", Texture.class);
        contextManager.getAssetManager().finishLoading();

        sceneManager.registerScene(GameSceneId.MENU.id(), new MenuScene(sceneManager, contextManager));
        sceneManager.registerScene(GameSceneId.GAME.id(), new GameScene(sceneManager, contextManager));
        sceneManager.registerScene(GameSceneId.GAME_OVER.id(), new GameOverScene(sceneManager, contextManager));
    }

    @Override
    public String getInitialSceneId() {
        return GameSceneId.MENU.id();
    }
}
