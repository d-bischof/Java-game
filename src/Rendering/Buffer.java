package Rendering;

public class Buffer {

    private char[][] frontBuffer;
    private static char[][] backBuffer;
    private static int width;
    private static int height;

    public Buffer(int width, int height) {
        Buffer.width = width;
        Buffer.height = height;
        frontBuffer = new char[height][width];
        backBuffer = new char[height][width];
    }

    public static void SetBuffer(int x, int y, char c) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            backBuffer[y][x] = c;
        }
    }

    public void swapBuffers() {

        char[][] temp = frontBuffer;
        frontBuffer = backBuffer;
        backBuffer = temp;

    }

    public static void clearBackBuffer() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                backBuffer[y][x] = ' ';
            }
        }
    }

    public void render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(frontBuffer[i][j]);
            }
            sb.append("\n");
        }
        System.out.print("\033[H\033[2J"); // ANSI clear screen
        System.out.print(sb.toString());
    }
    
}
