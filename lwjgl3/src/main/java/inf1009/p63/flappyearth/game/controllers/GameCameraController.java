package inf1009.p63.flappyearth.game.controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import inf1009.p63.flappyearth.game.config.GameplayDimensions;
import inf1009.p63.flappyearth.game.entities.Player;

public class GameCameraController {

    private final OrthographicCamera camera;
    private final GameplayDimensions dimensions;
    private boolean locked;
    private float lockedX;

    public GameCameraController(OrthographicCamera camera, GameplayDimensions dimensions) {
        this.camera = camera;
        this.dimensions = dimensions;
    }

    public void onEnter() {
        locked = false;
        lockedX = 0f;
    }

    public void lockAt(float x) {
        locked = true;
        lockedX = x;
    }

    public void apply(Player player) {
        float worldW = dimensions.getWorldWidth();
        float worldH = dimensions.getWorldHeight();
        if (locked) {
            camera.position.set(lockedX, worldH / 2f, 0f);
        } else if (player != null) {
            camera.position.set(
                    player.getBounds().x + (worldW * dimensions.getCameraFollowLeadRatio()),
                    worldH / 2f,
                    0f);
        }
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
