package edu.csc413.tankgame.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

/**
 * The view class that displays sprites on screen while the game is actually running. This will be the only view class
 * you need to interact with for the project. See the documentation for addSprite, removeSprite, and
 * setSpriteLocationAndAngle below for methods you will need to call.
 */
public class RunGameView extends JPanel {
    // Constants for the image file names for various sprites.
    public static final String PLAYER_TANK_IMAGE_FILE = "player-tank.png";
    public static final String AI_TANK_IMAGE_FILE = "ai-tank.png";
    public static final String SHELL_IMAGE_FILE = "shell.png";

    static final Dimension SCREEN_DIMENSIONS = new Dimension(1024, 768);

    private static final String SHELL_EXPLOSION_FILE_PREFIX = "shell-explosion-";
    private static final String SHELL_EXPLOSION_FILE_SUFFIX = ".png";

    // These constants may be used if you add support for animations on screen. See the addAnimation method.
    public static final AnimationResource SHELL_EXPLOSION_ANIMATION =
            new AnimationResource(SHELL_EXPLOSION_FILE_PREFIX, SHELL_EXPLOSION_FILE_SUFFIX, 6);
    public static final int SHELL_EXPLOSION_FRAME_DELAY = 3;
    public static final double SHELL_EXPLOSION_WIDTH = 32.0;
    public static final double SHELL_EXPLOSION_HEIGHT = 32.0;

    private static final String BIG_EXPLOSION_FILE_PREFIX = "big-explosion-";
    private static final String BIG_EXPLOSION_FILE_SUFFIX = ".png";

    // These constants may be used if you add support for animations on screen. See the addAnimation method.
    public static final AnimationResource BIG_EXPLOSION_ANIMATION =
            new AnimationResource(BIG_EXPLOSION_FILE_PREFIX, BIG_EXPLOSION_FILE_SUFFIX, 7);
    public static final int BIG_EXPLOSION_FRAME_DELAY = 4;
    public static final double BIG_EXPLOSION_WIDTH = 64.0;
    public static final double BIG_EXPLOSION_HEIGHT = 64.0;

    private final BufferedImage worldImage;
    private final Map<String, Sprite> spritesById = new HashMap<>();
    private final List<Animation> animations = new LinkedList<>();

    public RunGameView() {
        worldImage = new BufferedImage(SCREEN_DIMENSIONS.width, SCREEN_DIMENSIONS.height, BufferedImage.TYPE_INT_RGB);
        setBackground(Color.BLACK);
    }

    public void reset() {
        synchronized (spritesById) {
            spritesById.clear();
        }
    }

    /**
     * Adds a new image on screen with the given unique ID. Once added, this sprite will be tracked by the RunGameView
     * until the sprite is explicitly removed with removeSprite, or until reset is called.
     */
    public void addSprite(
            String id, String entityImageFile, double initialX, double initialY, double initialAngle) {
        synchronized (spritesById) {
            if (spritesById.containsKey(id)) {
                throw new RuntimeException(
                        "A sprite with id '" + id + "' already exists. Use setSpriteLocationAndAngle instead.");
            }
            Sprite sprite = new Sprite(entityImageFile);
            sprite.setLocationAndAngle(initialX, initialY, initialAngle);
            spritesById.put(id, sprite);
        }
    }

    /**
     * Removes the sprite with the given ID from the screen. If no sprite with the ID exists, nothing will happen. This
     * should be used when an image should no longer be shown, like if an entity is destroyed, or if a shell goes off
     * screen.
     */
    public void removeSprite(String id) {
        synchronized (spritesById) {
            spritesById.remove(id);
        }
    }

    /**
     * Updates the location and angle of the sprite with the given ID. The sprite must have already been added
     * previously with addSprite.
     */
    public void setSpriteLocationAndAngle(String id, double x, double y, double angle) {
        synchronized (spritesById) {
            if (!spritesById.containsKey(id)) {
                throw new RuntimeException("No sprite with id '" + id + "' was added. addSprite must be called first.");
            }
            spritesById.get(id).setLocationAndAngle(x, y, angle);
        }
    }

    /**
     * Adds an animation (specified with an AnimationResource -- see constants above for choices) at the given position
     * with the provided delay between each frame in the animation. Once the animation finishes, the RunGameView will
     * automatically remove the animation.
     */
    public void addAnimation(AnimationResource animationResource, int frameDelay, double x, double y) {
        synchronized (animations) {
            animations.add(new Animation(animationResource, frameDelay, x, y));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D buffer = worldImage.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0, 0, SCREEN_DIMENSIONS.width, SCREEN_DIMENSIONS.height);

        synchronized (spritesById) {
            for (Sprite sprite : spritesById.values()) {
                buffer.drawImage(sprite.getEntityImage(), sprite.getAffineTransform(), null);
            }
        }

        synchronized (animations) {
            ListIterator<Animation> animationIterator = animations.listIterator();
            while (animationIterator.hasNext()) {
                Animation animation = animationIterator.next();
                Optional<BufferedImage> nextImage = animation.getImage();
                if (nextImage.isPresent()) {
                    buffer.drawImage(nextImage.get(), (int) animation.getX(), (int) animation.getY(), null);
                } else {
                    animationIterator.remove();
                }
            }
        }

        g.drawImage(worldImage, 0, 0, null);
    }
}
