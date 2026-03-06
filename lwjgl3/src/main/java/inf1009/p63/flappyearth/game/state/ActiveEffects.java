package inf1009.p63.flappyearth.game.state;

public class ActiveEffects {

    private float shieldTimer   = 0f;
    private float slowTimeTimer = 0f;

    public void update(float delta) {
        // Count down effect timers
        if (shieldTimer   > 0) shieldTimer   = Math.max(0, shieldTimer   - delta);
        if (slowTimeTimer > 0) slowTimeTimer = Math.max(0, slowTimeTimer - delta);
    }

    public boolean isShieldActive()          { return shieldTimer > 0; }
    public float   getShieldTimer()          { return shieldTimer; }
    public void    activateShield(float d)   { shieldTimer = d; }

    public boolean isSlowTimeActive()        { return slowTimeTimer > 0; }
    public float   getSlowTimeTimer()        { return slowTimeTimer; }
    public void    activateSlowTime(float d) { slowTimeTimer = d; }

    public void reset() {
        shieldTimer   = 0f;
        slowTimeTimer = 0f;
    }
}
