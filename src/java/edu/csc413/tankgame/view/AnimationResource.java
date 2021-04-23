package edu.csc413.tankgame.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

class AnimationResource {
    private final BufferedImage[] frames;

    AnimationResource(String imagePrefix, String imageSuffix, int numFrames) {
        frames = new BufferedImage[numFrames];
        for (int i = 0; i < numFrames; i++) {
            String imageFile = String.format("%s%d%s", imagePrefix, i, imageSuffix);
            URL imageUrl = getClass().getClassLoader().getResource(imageFile);
            if (imageUrl == null) {
                throw new RuntimeException(String.format("Couldn't find the file '%s'", imageFile));
            }
            try {
                frames[i] = ImageIO.read(imageUrl);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    int getNumFrames() {
        return frames.length;
    }

    BufferedImage getFrame(int index) {
        return frames[index];
    }
}
