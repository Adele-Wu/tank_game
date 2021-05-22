package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class Shell extends Entity {
    private static int uniqueID = 0;

    public Shell(String id, double x, double y, double angle) {
        super(id + "-shell-" + uniqueID, x, y, angle);
        uniqueID++;
    }

    @Override
    public void move(GameWorld gameWorld) {
        moveForward(Constants.SHELL_MOVEMENT_SPEED);
    }

    @Override
    public double getXBound() {
        return getX() + Constants.SHELL_WIDTH;
    }

    @Override
    public double getYBound() {
        return getY() + Constants.SHELL_HEIGHT;
    }

    @Override
    public boolean checkBounds() {
        if (getX() < Constants.SHELL_X_LOWER_BOUND || getX() > Constants.SHELL_X_UPPER_BOUND ||
                getY() < Constants.SHELL_Y_LOWER_BOUND || getY() > Constants.SHELL_Y_UPPER_BOUND) {
            return true;
        }
        return false;
    }

}
