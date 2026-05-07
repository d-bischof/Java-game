package Game;

import Rendering.Buffer;
import Sound.SoundManager;
import Rendering.Renderer;
import java.util.concurrent.ConcurrentLinkedQueue;

import Game.Cactus;
import Game.CactusManager;

import java.util.ArrayList;

public class Game {
    boolean running;
    Buffer buffer;
    private long lastFrameTime;
    private volatile int lastkey = -1;
    private Thread inputThread;
    private float dt;
    private int buf_Height = 10;
    private int buf_Width = 81;

    private int ground = 6;


    private Player player;

    private CactusManager cactusManager;

    private SoundManager soundmanager;

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
        buffer = new Buffer(buf_Width, buf_Height);
        player = new Player(10.0f, ground);

        cactusManager = new CactusManager(ground, buf_Width - 1);
        soundmanager = new SoundManager();
        soundmanager.loadSound("point", "C:\\Users\\wey\\Desktop\\java-game\\sounds\\pickupCoin.wav");
        soundmanager.loadSound("jump", "C:\\Users\\wey\\Desktop\\java-game\\sounds\\jump.wav");
        soundmanager.loadSound("death", "C:\\Users\\wey\\Desktop\\java-game\\sounds\\hitHurt.wav");

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
        //System.out.println("ascii: " + key + " char: " + (char)key);
        switch(key) {
            case ' ': if (player.isGrounded) {
                player.jump();
                soundmanager.play("jump");
            }
            break;
        }
    }

    private void update() {
        if (player.alive) {
            player.update(dt);

            cactusManager.update(dt);

            for (var c : cactusManager.getCacti()) {
                if (c.collidesWith(player.X(), player.Y())) {
                    System.out.println("GAME OVER! ");
                    soundmanager.play("death");
                    player.alive = false;
                    break;
                }
            }

        }
    }

    private void render() {
        Renderer.clear();
        //render here
        //borders
        Renderer.drawline(0, 0, 0, buf_Height - 1, '|');
        Renderer.drawline(0, 0, buf_Width - 1, 0, '-');
        Renderer.drawline(0, buf_Height - 1, buf_Width - 1, buf_Height - 1, '-');
        Renderer.drawline(buf_Width - 1, 1, buf_Width -1 , buf_Height - 2, '|');

        player.render();
        cactusManager.render();

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
