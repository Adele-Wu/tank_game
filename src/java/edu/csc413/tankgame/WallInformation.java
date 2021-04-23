package edu.csc413.tankgame;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The WallInformation class is used to provide information for where a single wall in the game should be drawn, along
 * with its image information. It's important to note that WallInformation should not be used as an Entity class -- it
 * merely packages the information it reads out of a .txt file for convenience. You will need to define your own Wall
 * class that stores the relevant information.
 */
public class WallInformation {
    private static final String WALL_IMAGE_FILE_PREFIX = "wall-";
    private static final String WALL_IMAGE_FILE_SUFFIX = ".png";
    private static final String WALLS_SETUP_FILE = "walls.txt";

    private final String imageFile;
    private final double x;
    private final double y;

    private WallInformation(String imageFile, double x, double y) {
        this.imageFile = imageFile;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the image file name for this wall. This is the image file name that should be passed along to the
     * RunGameView when the sprite is drawn onscreen.
     */
    public String getImageFile() {
        return imageFile;
    }

    /** Returns the x coordinate of the wall. */
    public double getX() {
        return x;
    }

    /** Returns the y coordinate of the wall. */
    public double getY() {
        return y;
    }

    /**
     * A static method that reads through the walls.txt file and creates a list of WallInformation objects from it. Each
     * individual WallInformation object supports the methods described above.
     */
    public static List<WallInformation> readWalls() {
        ArrayList<ArrayList<Integer>> walls;
        URL fileUrl = WallInformation.class.getClassLoader().getResource(WALLS_SETUP_FILE);
        if (fileUrl == null) {
            throw new RuntimeException("Unable to find the resource: " + WALLS_SETUP_FILE);
        }

        try {
            walls =
                    Files.lines(Path.of(fileUrl.toURI()))
                            .map(WallInformation::lineToList)
                            .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException | URISyntaxException exception) {
            throw new RuntimeException(exception);
        }

        ArrayList<WallInformation> wallInformationList = new ArrayList<>();
        for (int row = 0; row < walls.size(); row++) {
            for (int col = 0; col < walls.get(row).size(); col++) {
                if (walls.get(row).get(col) != 0) {
                    double x = col * Constants.WALL_WIDTH;
                    double y = row * Constants.WALL_HEIGHT;
                    wallInformationList.add(
                            new WallInformation(
                                    String.format(
                                            "%s%d%s",
                                            WALL_IMAGE_FILE_PREFIX,
                                            walls.get(row).get(col),
                                            WALL_IMAGE_FILE_SUFFIX),
                                    x,
                                    y));
                }
            }
        }
        return wallInformationList;
    }

    private static ArrayList<Integer> lineToList(String line) {
        return Arrays.stream(line.split("\\s")).map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
    }
}
