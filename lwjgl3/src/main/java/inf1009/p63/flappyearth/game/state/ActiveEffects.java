package inf1009.p63.flappyearth.game.state;

public class ActiveEffects {

    private float shieldTimer   = 0f;
    private float slowTimeTimer = 0f;
    private float screenShakeTimer = 0f;
    private float screenShakeMagnitude = 0f;

    public void update(float delta) {
        if (shieldTimer   > 0) shieldTimer   = Math.max(0, shieldTimer   - delta);
        if (slowTimeTimer > 0) slowTimeTimer = Math.max(0, slowTimeTimer - delta);
        if (screenShakeTimer > 0) {
            screenShakeTimer = Math.max(0, screenShakeTimer - delta);
            if (screenShakeTimer == 0f) {
                screenShakeMagnitude = 0f;
            }
        }
    }

    public boolean isShieldActive()          { return shieldTimer > 0; }
    public float   getShieldTimer()          { return shieldTimer; }
    public void    activateShield(float d)   { shieldTimer = d; }

    public boolean isSlowTimeActive()        { return slowTimeTimer > 0; }
    public float   getSlowTimeTimer()        { return slowTimeTimer; }
    public void    activateSlowTime(float d) { slowTimeTimer = d; }

    public void activateScreenShake(float duration, float magnitude) {
        screenShakeTimer = duration;
        screenShakeMagnitude = magnitude;
    }

    public boolean isScreenShaking() { return screenShakeTimer > 0f; }
    public float getScreenShakeMagnitude() { return screenShakeMagnitude; }

    public void reset() {
        shieldTimer   = 0f;
        slowTimeTimer = 0f;
        screenShakeTimer = 0f;
        screenShakeMagnitude = 0f;
    }
}
