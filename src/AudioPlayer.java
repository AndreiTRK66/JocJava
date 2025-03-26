import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    public static void playSound(String filename) {
        try {
            File soundFile = new File("resources/audio/" + filename);
            if (!soundFile.exists()) return;

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static Clip currentLoop;

public static void loopSound(String filename) {
    try {
        File soundFile = new File("resources/audio/" + filename);
        if (!soundFile.exists()) return;

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.loop(Clip.LOOP_CONTINUOUSLY);

        if (currentLoop != null && currentLoop.isRunning()) currentLoop.stop();
        currentLoop = clip;
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void stopAll() {
    if (currentLoop != null && currentLoop.isRunning()) {
        currentLoop.stop();
        currentLoop.close();
    }
}

}