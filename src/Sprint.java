import java.lang.Thread;
import Rendering.Renderer;
import Rendering.Buffer;

class Sprint {

    public static void main(String[] args) {
        float player_x = 10.0f;
        float player_y = 15.0f;


        var buffer = new Buffer(81, 22);

        System.out.print("\033[?25l");

        for(;;) {

            player_x += 0.1f;

            Renderer.clear();
            //render here

            Renderer.drawline(1, 2, 1, 20, '|');
            Renderer.drawline(1, 1, 80, 1, '-');
            Renderer.drawline(1, 21, 80, 21, '-');
            Renderer.drawline(80, 2, 80, 20, '|');

            Renderer.pixel((int)player_x, (int)player_y, '@');

            buffer.swapBuffers();

            buffer.render();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}