package inf1009.p63.flappyearth.game.state;

public class GameState {

    private boolean alive           = true;
    private boolean gameOverPending = false;
    private float   invincibleTimer = 0f;

    private static final int MAX_HEARTS = 3;
    private int hearts = MAX_HEARTS;

    public enum SceneRequest { NONE, GAME_OVER, MENU }
    private SceneRequest requestedScene = SceneRequest.NONE;

    public void reset() {
        alive           = true;
        gameOverPending = false;
        requestedScene  = SceneRequest.NONE;
        invincibleTimer = 0f;
        hearts          = MAX_HEARTS;
    }

    public void    tickInvincibility(float delta) { if (invincibleTimer > 0) invincibleTimer -= delta; }
    public boolean isInvincible()                 { return invincibleTimer > 0; }
    public void    setInvincible(float duration)  { invincibleTimer = duration; }

    public void    loseHeart()    { if (hearts > 0) hearts--; }
    public boolean isDead()       { return hearts <= 0; }
    public int     getHearts()    { return hearts; }
    public int     getMaxHearts() { return MAX_HEARTS; }

    public boolean isAlive()                 { return alive; }
    public void    setAlive(boolean alive)   { this.alive = alive; }

    public boolean isGameOverPending()                 { return gameOverPending; }
    public void    setGameOverPending(boolean pending) { this.gameOverPending = pending; }

    public SceneRequest getRequestedScene()                     { return requestedScene; }
    public void         setRequestedScene(SceneRequest request) { this.requestedScene = request; }
}