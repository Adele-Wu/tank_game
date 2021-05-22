package edu.csc413.tankgame.view;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {
    private static Clip clip = null;
    private static Clip clip2 = null;
    private static Clip clip3 = null;
    private static Clip clip4 = null;

    static {
        File whoosh = new File("resources/whoosh.wav");
        File boom = new File("resources/boom.wav");

        AudioInputStream audioStream = null;
        AudioInputStream audioStream2 = null;

        try {
            audioStream = AudioSystem.getAudioInputStream(whoosh);
            audioStream2 = AudioSystem.getAudioInputStream(boom);

        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        try {
            clip = AudioSystem.getClip();
            clip2 = AudioSystem.getClip();

            clip.open(audioStream);
            clip2.open(audioStream2);

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void clipSound1() {
        clip.setFramePosition(0);
        clip.start();
    }

    public static void clipSound2() {
        clip2.setFramePosition(0);
        clip2.start();
    }
}
