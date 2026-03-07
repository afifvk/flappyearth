package inf1009.p63.flappyearth.game.controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import inf1009.p63.flappyearth.game.entities.Player;

public class GameCameraController {

    private final OrthographicCamera camera;
    private boolean locked;
    private float lockedX;

    public GameCameraController(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void onEnter() {
        locked = false;
        lockedX = 0f;
    }

    public void lockAt(float x) {
        locked = true;
        lockedX = x;
    }

    public void apply(float screenW, float screenH, Player player) {
        if (locked) {
            camera.position.set(lockedX, screenH / 2f, 0f);
        } else if (player != null) {
            camera.position.set(player.getBounds().x + screenW / 4f, screenH / 2f, 0f);
        }
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
