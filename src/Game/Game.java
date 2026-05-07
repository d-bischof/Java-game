package Game;

import Rendering.Buffer;
import Sound.SoundManager;
//import sun.net.www.content.text.plain;
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

    private float score;
    private int scoreMarker;
    private final float score_Increase;
    private int ground = 6;

    private Player player;

    private static int HI_score;

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

        HI_score = 0;

        score = 0;
        score_Increase = 3.0f;
        scoreMarker = 0;

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

            case '\n':
            case '\r':
                if (!player.alive) {
                    resetGame();
                }
                break;
        }
    }

    private void update() {
        if (player.alive) {
            player.update(dt);

            cactusManager.update(dt);

            score += score_Increase * dt;

            //System.out.printf("Score: %d\n", (int)score);

            int intScore = (int)score;
            if (intScore != 0 && intScore % 50 == 0 && intScore > scoreMarker) {
                soundmanager.play("point");
                scoreMarker = intScore;
            }

            for (var c : cactusManager.getCacti()) {
                if (c.collidesWith(player.X(), player.Y())) {
                    //System.out.println("game over! press enter to restart");
                    if ((int)score > HI_score) HI_score = (int)score;
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

        String s_score = String.format("%05d", (int)score);

        if (HI_score > 0) {
            String s_hi_score = String.format("HI%05d", HI_score);
            for (int i = 0; i < s_hi_score.length(); i++) {
                Renderer.pixel(65 + i, 1, s_hi_score.charAt(i));

            }
        }

        for (int i = 0; i < s_score.length(); i++) {
            Renderer.pixel(75 + i, 1, s_score.charAt(i));
        }

        String g_over = "Game Over! - PRESS ENTER TO RESTART";
        if (!player.alive) {
            for (int i = 0; i < g_over.length(); i++) {
                Renderer.pixel(i + 23, 3, g_over.charAt(i));
            }
        }

        player.render();
        cactusManager.render();

        buffer.swapBuffers();
        buffer.render();
    }

    private void resetGame() {
        score = 0.0f;
        scoreMarker = 0;
        player = new Player(10.0f, ground);
        cactusManager = new CactusManager(ground, buf_Width - 1);

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
