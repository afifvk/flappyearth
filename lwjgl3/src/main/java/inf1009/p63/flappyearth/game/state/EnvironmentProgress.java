package inf1009.p63.flappyearth.game.state;

public class EnvironmentProgress {

    private int goodCollectiblesCollected;
    private final int maxGoodCollectibles;

    public EnvironmentProgress(int maxGoodCollectibles) {
        if (maxGoodCollectibles <= 0) {
            throw new IllegalArgumentException("maxGoodCollectibles must be > 0");
        }
        this.maxGoodCollectibles = maxGoodCollectibles;
        this.goodCollectiblesCollected = 0;
    }

    public void addGoodCollectible() {
        if (goodCollectiblesCollected < maxGoodCollectibles) {
            goodCollectiblesCollected++;
        }
    }

    public int getGoodCollectiblesCollected() {
        return goodCollectiblesCollected;
    }

    public int getMaxGoodCollectibles() {
        return maxGoodCollectibles;
    }

    public float getProgressRatio() {
        return goodCollectiblesCollected / (float) maxGoodCollectibles;
    }

    public void reset() {
        goodCollectiblesCollected = 0;
    }
}
