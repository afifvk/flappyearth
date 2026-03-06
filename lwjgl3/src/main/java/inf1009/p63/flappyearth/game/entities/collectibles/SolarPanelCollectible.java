package inf1009.p63.flappyearth.game.entities.collectibles;

public class SolarPanelCollectible extends GoodCollectible {
    public SolarPanelCollectible(float x, float y) {
        super(x, y, 30, 30, "solar_panel", CollectibleType.SOLAR_PANEL);
    }
    @Override
    public float getSmokeVisibility() {
        return 0.2f;
}
}
