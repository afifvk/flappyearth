package inf1009.p63.flappyearth.game.state;

public class ScoreManager {

    private int score = 0;

    public void addPoint()        { score++; }
    public int  getCurrentScore() { return score; }
    public String getScoreText()  { return "Score: " + score; }

    public void reset() { score = 0; }
}