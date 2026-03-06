package inf1009.p63.flappyearth.game.scenes;

public enum GameSceneId {
    MENU,
    GAME,
    GAME_OVER;

    public String id() {
        return name();
    }
}
