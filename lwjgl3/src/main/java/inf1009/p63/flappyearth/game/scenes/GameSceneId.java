package inf1009.p63.flappyearth.game.scenes;

public enum GameSceneId {
    MENU,
    SETTINGS,
    STAGE_ONE,
    STAGE_TWO,
    STAGE_THREE,
    GAME_OVER;

    public String id() {
        return name();
    }
}
