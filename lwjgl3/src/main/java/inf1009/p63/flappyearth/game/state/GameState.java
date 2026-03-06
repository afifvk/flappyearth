package inf1009.p63.flappyearth.game.state;

public class GameState {

    private boolean alive           = true;
    private boolean gameOverPending = false;

    public enum SceneRequest { NONE, GAME_OVER, MENU }
    private SceneRequest requestedScene = SceneRequest.NONE;

    public void reset() {
        alive           = true;
        gameOverPending = false;
        requestedScene  = SceneRequest.NONE;
    }

    public boolean isAlive()                  { return alive; }
    public void    setAlive(boolean alive)    { this.alive = alive; }

    public boolean isGameOverPending()                  { return gameOverPending; }
    public void    setGameOverPending(boolean pending)  { this.gameOverPending = pending; }

    public SceneRequest getRequestedScene()                     { return requestedScene; }
    public void         setRequestedScene(SceneRequest request) { this.requestedScene = request; }
}
