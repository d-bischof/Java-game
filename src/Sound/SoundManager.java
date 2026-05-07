package Sound;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Map<String, Clip> sounds = new HashMap<>();

    public void loadSound(String name, String filepath) {
        try {
            File soundFile = new File(filepath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            sounds.put(name, clip);
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + filepath);
            e.printStackTrace();
        }
    }

    public void play(String name) {
        Clip clip = sounds.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playLoop(String name) {
        Clip clip = sounds.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(String name) {
        Clip clip = sounds.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
