package edu.sjsu.android.accelorometer;

public class Particle {
    private static final float COR = 0.7f;
    public float posX;
    public float posY;
    public float velX;
    public float velY;

    public void updatePosition(float x, float y, float z, long timestamp){
        float dt = (System.nanoTime() - timestamp) / 1000000000.0f;
        velX += -x * dt;
        velY += -y * dt;
        posX += velX * dt;
        posY += velY * dt;
    }

    public void resolveCollisionWithBounds(float horizontalBound, float verticalBound){
        if(posX > horizontalBound){
            posX = horizontalBound;
            velX = -velX * COR;
        }
        else if(posX < -horizontalBound){
            posX = -horizontalBound;
            velX = -velX * COR;
        }

        if(posY > verticalBound){
            posY = verticalBound;
            velY = -velY * COR;
        }
        else if(posY < -verticalBound) {
            posY = -verticalBound;
            velY = -velY * COR;
        }
    }
}
