package inf1009.p63.flappyearth.engine.interfaces;

public interface Movable {
    void movement(float delta);
    void setVelocity(float vx, float vy);
    float getVelocityX();
    float getVelocityY();
}
