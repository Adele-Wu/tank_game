package edu.csc413.tankgame.view;

import java.awt.image.BufferedImage;
import java.util.Optional;

class Animation {
    private final AnimationResource animationResource;
    private final int frameDelay;
    private final double x;
    private final double y;

    private int currentFrame = 0;
    private int counter = 0;

    Animation(AnimationResource animationResource, int frameDelay, double x, double y) {
        this.animationResource = animationResource;
        this.frameDelay = frameDelay;
        this.x = x;
        this.y = y;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    Optional<BufferedImage> getImage() {
        if (currentFrame == animationResource.getNumFrames()) {
            return Optional.empty();
        }

        BufferedImage bufferedImage = animationResource.getFrame(currentFrame);
        counter++;
        if (counter >= frameDelay) {
            counter = 0;
            currentFrame++;
        }

        return Optional.of(bufferedImage);
    }
}
