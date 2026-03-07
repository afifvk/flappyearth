package inf1009.p63.flappyearth.game.state;

public class GameState {

    private boolean alive           = true;
    private boolean deathSequenceActive = false;
    private float deathFallSpeedMultiplier = 1f;
    private float   invincibleTimer = 0f;
    private boolean controlsEnabled = true;
    private boolean spawningEnabled = true;

    private static final int MAX_HEARTS = 3;
    private int hearts = MAX_HEARTS;

    public enum SceneRequest { NONE, GAME_OVER, MENU }
    private SceneRequest requestedScene = SceneRequest.NONE;

    public void reset() {
        alive           = true;
        deathSequenceActive = false;
        deathFallSpeedMultiplier = 1f;
        requestedScene  = SceneRequest.NONE;
        invincibleTimer = 0f;
        hearts          = MAX_HEARTS;
        controlsEnabled = true;
        spawningEnabled = true;
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

    public boolean isDeathSequenceActive() { return deathSequenceActive; }
    public void startDeathSequence(float deathFallSpeedMultiplier) {
        this.alive = false;
        this.deathSequenceActive = true;
        this.deathFallSpeedMultiplier = deathFallSpeedMultiplier;
        this.requestedScene = SceneRequest.NONE;
    }
    public void finishDeathSequence() {
        this.deathSequenceActive = false;
        this.requestedScene = SceneRequest.GAME_OVER;
    }
    public float getDeathFallSpeedMultiplier() { return deathFallSpeedMultiplier; }

    public boolean isControlsEnabled() { return controlsEnabled; }
    public void setControlsEnabled(boolean controlsEnabled) { this.controlsEnabled = controlsEnabled; }

    public boolean isSpawningEnabled() { return spawningEnabled; }
    public void setSpawningEnabled(boolean spawningEnabled) { this.spawningEnabled = spawningEnabled; }

    public SceneRequest getRequestedScene()                     { return requestedScene; }
    public void         setRequestedScene(SceneRequest request) { this.requestedScene = request; }
    public void         clearSceneRequest()                     { this.requestedScene = SceneRequest.NONE; }
}