package edu.csc413.tankgame.view;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class Sprite {
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();

    private final BufferedImage entityImage;
    private final AffineTransform affineTransform;

    Sprite(String imageFile) {
        if (!imageCache.containsKey(imageFile)) {
            URL imageUrl = getClass().getClassLoader().getResource(imageFile);
            if (imageUrl == null) {
                throw new RuntimeException("Image file not found: " + imageFile);
            }
            try {
                imageCache.put(imageFile, ImageIO.read(imageUrl));
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        entityImage = imageCache.get(imageFile);
        affineTransform = new AffineTransform();
    }

    BufferedImage getEntityImage() {
        return entityImage;
    }

    AffineTransform getAffineTransform() {
        return affineTransform;
    }

    void setLocationAndAngle(double x, double y, double angle) {
        affineTransform.setToTranslation(x, y);
        affineTransform.rotate(angle, entityImage.getWidth() / 2.0, entityImage.getHeight() / 2.0);
    }
}
