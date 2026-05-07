package Game;

import Rendering.Renderer;

public class Player {

    private float x;
    private float y;
    private final float jumpForce = 12.5f;
    private final float gravity = 30.0f;
    public boolean alive;

    private float ground;

    //public boolean isJumping;
    public boolean isGrounded;

    public float vx;
    public float vy;

    Player(float x, float y) {
        this.x = x;
        this.y = y;
        ground = y;

        //isJumping = false;
        alive = true;
        isGrounded = true;
    }

    public int X() {
        return (int)x;
    }

    public int Y() {
        return (int)y;
    }

    public void update(float dt) {
        vy += gravity * dt;

        x += vx * dt;
        y += vy * dt;

        if (y >= ground) {
            y = ground;
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
