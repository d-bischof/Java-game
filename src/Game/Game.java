package Game;

import Rendering.Renderer;
import Rendering.Buffer;


public class Game {

    boolean running;
    Buffer buffer;
    float player_x;
    float player_y;  

    public Game() {

        running = true;
        buffer = new Buffer(81, 22);

        player_x = 10.0f;
        player_y = 15.0f;

        System.out.print("\033[?25l");

    }

    private void pollEvents() {

    }

    private void update() {

        player_x += 0.1f;

    }

    private void render() {

        Renderer.clear();

        Renderer.drawline(1, 2, 1, 20, '|');
        Renderer.drawline(1, 1, 80, 1, '-');
        Renderer.drawline(1, 21, 80, 21, '-');
        Renderer.drawline(80, 2, 80, 20, '|');

        Renderer.pixel((int)player_x, (int)player_y, '@');

        buffer.swapBuffers();

        buffer.render();
    }

    public void run() {

        while(running) {

            update();
            render();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
