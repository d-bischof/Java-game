package Rendering;

public class Renderer {

    public static void clear() {
        Buffer.clearBackBuffer();
    }

    public static void pixel(int x, int y, char c) {
        Buffer.SetBuffer(x, y, c);
    }

    public static void putChar(int x, int y, char c) {
        System.out.print("\033[" + y + ";" + x + "H" + c);
        System.out.print("\033[?25l"); 
    }

    public static void drawline(int x1, int y1, int x2, int y2, char c) {

        if (x1 == x2 ) {
            int startY;
            int EndY;

            if (y1 > y2) {
                startY = y2;
                EndY = y1;
            }
            else {
                startY = y1;
                EndY = y2;
            }

            for (int y = startY; y <= EndY; y++) {
                putChar(x1, y, c);
            }
        }
        else {

            int startX;
            int EndX;


            if (x1 > x2) {
                startX = x2;
                EndX = x1;
            }
            else {
                startX = x1;
                EndX = x2;
            }

            int dx = EndX - startX;
            int dy = y2 - y1;
            float m = (float) dy / dx;

            for (int x = startX; x <= EndX; x++) {
                int y = (int) (m * (x - startX) + y1);
                putChar(x, y, c);
            }
        }

    }

}
