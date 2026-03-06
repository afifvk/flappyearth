package inf1009.p63.flappyearth.game.managers;

public class ScoreManager {

    private float distanceTraveled = 0f;
    private static final float PIXELS_PER_METRE = 100f;

    public void   addDistance(float pixels)    { distanceTraveled += pixels; }
    // Convert pixels to metres for score
    public int    getCurrentScore()            { return (int)(distanceTraveled / PIXELS_PER_METRE); }
    public String getScoreText()               { return "Score: " + getCurrentScore(); }

    public void reset() {
        distanceTraveled = 0f;
    }
}
