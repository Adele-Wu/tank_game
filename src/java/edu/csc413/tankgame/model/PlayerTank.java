package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.KeyboardReader;

public class PlayerTank extends Tank {
    private static int health = 5;

    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
        xprevious = getX();
        yprevious = getY();

        KeyboardReader keyboard = KeyboardReader.instance();
        if (keyboard.upPressed()) {
            moveForward(Constants.TANK_MOVEMENT_SPEED);
        }
        if (keyboard.downPressed()) {
            moveBackward(Constants.TANK_MOVEMENT_SPEED);
        }
        if (keyboard.leftPressed()) {
            turnLeft(Constants.TANK_TURN_SPEED);
        }
        if (keyboard.rightPressed()) {
            turnRight(Constants.TANK_TURN_SPEED);
        }
        if (keyboard.spacePressed()) {
            fireShell(gameWorld);
        }
        decrementCoolDown();
    }

    public static int getHealth() {
        return health;
    }

    public void decreaseHealth() {
        health--;
    }

    public static void increaseHealth() {
        health++;
    }

    public static void setHealth(int h) {
        health = h;
    }
}
