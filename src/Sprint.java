import java.lang.Thread;
import Rendering.Renderer;
import Rendering.Buffer;

class Sprint {

    public static void main(String[] args) {
        float player_x = 10.0f;
        float player_y = 15.0f;

        Renderer.drawline(1, 2, 1, 20, '|');
        Renderer.drawline(1, 1, 80, 1, '-');
        Renderer.drawline(1, 21, 80, 21, '-');
        Renderer.drawline(80, 2, 80, 20, '|');

        var buffer = new Buffer(80, 21);

        for(;;) {
            player_x += 0.1f;

            Renderer.clear();

            Renderer.pixel((int)player_x, (int)player_y, '@');

            buffer.swapBuffers();

            buffer.render();
            //Renderer.drawline(2, 2, 79, 20, '*');
            //Renderer.putChar((int)player_x, (int)player_y, '@');
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}