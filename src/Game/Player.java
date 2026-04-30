package Game;

import Rendering.Renderer;

public class Player {

    private float x;
    private float y;
    private final float jumpForce = 10.0f;
    private final float gravity = 20.0f;

    //public boolean isJumping;
    public boolean isGrounded;

    public float vx;
    public float vy;

    Player(float x, float y) {
        this.x = x;
        this.y = y;

        //isJumping = false;
        isGrounded = true;
    }

    public void update(float dt) {
        vy += gravity * dt;

        x += vx * dt;
        y += vy * dt;

        if (y >= 15.0f) {
            y = 15.0f;
            vy = 0.0f;
            vx = 0.0f;
            isGrounded = true;
        } else {
            isGrounded = false;
        }
    }

    public void jump() {

        vy = -jumpForce;
        isGrounded = false;
        //isJumping = false;
    }

    public void render() {
        Renderer.pixel((int)x, (int)y, '@');
    }

}
