package inf1009.p63.flappyearth.game.state;

public class ActiveEffects {

    private float shieldTimer   = 0f;
    private float slowTimeTimer = 0f;
    private float screenShakeTimer = 0f;
    private float screenShakeMagnitude = 0f;
    private float oilBlotTimer = 0f;
    private float oilBlotDuration = 0f;
    private float trashRattleTimer = 0f;
    private float trashRattleDuration = 0f;
    private float smokeSurgeTimer = 0f;
    private float smokeSurgeMinimumAlpha = 0f;

    public void update(float delta) {
        if (shieldTimer   > 0) shieldTimer   = Math.max(0, shieldTimer   - delta);
        if (slowTimeTimer > 0) slowTimeTimer = Math.max(0, slowTimeTimer - delta);
        if (screenShakeTimer > 0) {
            screenShakeTimer = Math.max(0, screenShakeTimer - delta);
            if (screenShakeTimer == 0f) {
                screenShakeMagnitude = 0f;
            }
        }
        if (oilBlotTimer > 0f) {
            oilBlotTimer = Math.max(0f, oilBlotTimer - delta);
            if (oilBlotTimer == 0f) {
                oilBlotDuration = 0f;
            }
        }
        if (trashRattleTimer > 0f) {
            trashRattleTimer = Math.max(0f, trashRattleTimer - delta);
            if (trashRattleTimer == 0f) {
                trashRattleDuration = 0f;
            }
        }
        if (smokeSurgeTimer > 0f) {
            smokeSurgeTimer = Math.max(0f, smokeSurgeTimer - delta);
            if (smokeSurgeTimer == 0f) {
                smokeSurgeMinimumAlpha = 0f;
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
        screenShakeTimer = Math.max(screenShakeTimer, duration);
        screenShakeMagnitude = Math.max(screenShakeMagnitude, magnitude);
    }

    public boolean isScreenShaking() { return screenShakeTimer > 0f; }
    public float getScreenShakeMagnitude() { return screenShakeMagnitude; }

    public void activateOilBlot(float duration) {
        oilBlotTimer = Math.max(oilBlotTimer, duration);
        oilBlotDuration = Math.max(oilBlotDuration, duration);
    }

    public boolean isOilBlotActive() { return oilBlotTimer > 0f; }

    public float getOilBlotTimer() { return oilBlotTimer; }

    public float getOilBlotIntensity() {
        if (oilBlotTimer <= 0f || oilBlotDuration <= 0f) {
            return 0f;
        }
        return oilBlotTimer / oilBlotDuration;
    }

    public void activateTrashRattle(float duration, float magnitude) {
        activateScreenShake(duration, magnitude);
        trashRattleTimer = Math.max(trashRattleTimer, duration);
        trashRattleDuration = Math.max(trashRattleDuration, duration);
    }

    public boolean isTrashRattleActive() {
        return trashRattleTimer > 0f;
    }

    public float getTrashRattleTimer() {
        return trashRattleTimer;
    }

    public float getTrashRattleProgress() {
        if (trashRattleTimer <= 0f || trashRattleDuration <= 0f) {
            return 0f;
        }
        return trashRattleTimer / trashRattleDuration;
    }

    public void activateSmokeSurge(float duration, float minimumAlpha) {
        smokeSurgeTimer = Math.max(smokeSurgeTimer, duration);
        smokeSurgeMinimumAlpha = Math.max(smokeSurgeMinimumAlpha, clamp01(minimumAlpha));
    }

    public float getSmokeOverlayAlpha(float baseAlpha) {
        return smokeSurgeTimer > 0f ? Math.max(clamp01(baseAlpha), smokeSurgeMinimumAlpha) : clamp01(baseAlpha);
    }

    public void reset() {
        shieldTimer   = 0f;
        slowTimeTimer = 0f;
        screenShakeTimer = 0f;
        screenShakeMagnitude = 0f;
        oilBlotTimer = 0f;
        oilBlotDuration = 0f;
        trashRattleTimer = 0f;
        trashRattleDuration = 0f;
        smokeSurgeTimer = 0f;
        smokeSurgeMinimumAlpha = 0f;
    }

    private float clamp01(float value) {
        return Math.max(0f, Math.min(1f, value));
    }
}
