
import Rendering.Renderer;

class Sprint {

    public static void main(String[] args) {
        float player_x = 10.0f;
        float player_y = 15.0f;

        for(;;) {
            Renderer.clear();

            Renderer.drawline(1, 2, 1, 20, '|');
            Renderer.drawline(1, 1, 80, 1, '-');
            Renderer.drawline(1, 21, 80, 21, '-');
            Renderer.drawline(80, 2, 80, 20, '|');

            //Renderer.drawline(2, 2, 79, 20, '*');

            Renderer.putChar((int)player_x, (int)player_y, '@');

        }
    }

}