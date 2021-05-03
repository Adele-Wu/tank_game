package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class Shell extends Entity {
    private static int uniqueID = 0;
    public Shell (String id, double x, double y, double angle) {
        super(id+"-shell-"+uniqueID, x, y, angle);
        uniqueID++;
    }

    @Override
    public void move(GameWorld gameWorld) {
        moveForward(Constants.SHELL_MOVEMENT_SPEED);
    }

    @Override
    public void checkBounds(GameWorld gameWorld) {
        if (getX() < Constants.SHELL_X_LOWER_BOUND || getX() > Constants.SHELL_X_UPPER_BOUND ||
            getY() < Constants.SHELL_Y_LOWER_BOUND || getY() > Constants.SHELL_Y_UPPER_BOUND) {
            // System.out.println(uniqueID + " out of bounds");
            for (Entity entity : gameWorld.getEntities()) {
                //REMOVE THIS SHELL
                gameWorld.addToGarbage(entity);
            }
        }
    }
}
