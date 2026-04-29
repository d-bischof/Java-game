package Game;
import Rendering.Buffer;
import Rendering.Renderer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
    boolean running;
    Buffer buffer;
    private long lastFrameTime;
    private volatile int lastkey = -1;
    private Thread inputThread;
    private float dt;
    private boolean jump = false;
    boolean grounded = true;
    private float gravity = 10.0f;
    float player_x;
    float player_y;
    float jumpForce = 15.0f;
    float p_vel_x;
    float p_vel_y;
    float p_acc_y;

    private ConcurrentLinkedQueue<Integer> keyQueue = new ConcurrentLinkedQueue<>();
    // Load native library
    static {
        try {
            System.loadLibrary("WindowsInput");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load WindowsInput library: " + e.getMessage());
        }
    }

    // Native methods
    private native void enableRawInput();
    private native int readKey();
    private native void disableRawInput();

    public Game() {
        running = true;
        buffer = new Buffer(81, 22);
        player_x = 10.0f;
        player_y = 15.0f;
        lastFrameTime = System.currentTimeMillis();
        System.out.print("\033[?25l");
        enableRawInput();
        startInputThread();
    }

    private void startInputThread() {
        inputThread = new Thread(() -> {
            while (running) {
                int key = readKey();
                if (key != -1) {
                    keyQueue.add(key);
                }

                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            disableRawInput();
        });
        inputThread.setDaemon(true);
        inputThread.start();
    }

    private void pollEvents() {
        if (!keyQueue.isEmpty()) {
            int key = keyQueue.poll();
            handleKeyPress(key);
        }
    }

    private void handleKeyPress(int key) {
        System.out.println("key: " + key + " char: " + (char)key);
        switch(key) {
            case ' ':
                jump = true;
                System.out.println("JUMP");
                break;
        }
    }

    private void update() {

        p_vel_y += gravity * dt;

        player_x += p_vel_x * dt;
        player_y += p_vel_y * dt;

        if (player_y >= 15.0f) {
            player_y = 15.0f;
            p_vel_y = 0.0f;
            p_vel_x = 0.0f;
            grounded = true;
        } else {
            grounded = false;
        }

        if (grounded && jump) {
            p_vel_y = -jumpForce;
            grounded = false;
            jump = false;
        }
    }

    private void render() {
        Renderer.clear();
        Renderer.drawline(1, 2, 1, 20, '|');
        Renderer.drawline(1, 1, 80, 1, '-');
        Renderer.drawline(1, 21, 80, 21, '-');
        Renderer.drawline(80, 2, 80, 20, '|');
        Renderer.pixel((int) player_x, (int) player_y, '@');
        buffer.swapBuffers();
        buffer.render();
    }

    public void run() {
        while (running) {
            long currentTime = System.currentTimeMillis();
            dt = (currentTime - lastFrameTime) / 1000.0f;
            lastFrameTime = currentTime;

            pollEvents();
            update();
            render();

            try {
                Thread.sleep(16);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
