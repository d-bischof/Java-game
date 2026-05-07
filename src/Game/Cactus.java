package Game;

import Rendering.Renderer;

public class Cactus {

    private float x;
    private float y;
    private int height;
    public static float speed = 20.0f;
    private float Xfloat;

    float vx = -speed;
    float vy;

    Cactus(float x, float y, int height) {
        this.Xfloat = x;
        this.x = Math.round(x);
        this.y = y;
        this.height = height;
    }

    float X() {
        return x;
    }

    void update(double dt) {
        /*
        Xfloat -= speed * dt;
        x = Math.round(Xfloat);
        */
        x += vx * dt;
    }

    boolean collidesWith(int px, int py) {
        return px == (int)this.x && py <= (int)y && py > (int)(y - height);
    }

    void render() {
        int Renderx = Math.round(x);
        for (int i = 0; i < height; i++) {
            Renderer.pixel(Renderx, (int)(y - i), '#');

        }
    }


}
