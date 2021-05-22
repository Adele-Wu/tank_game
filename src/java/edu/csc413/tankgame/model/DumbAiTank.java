package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

import java.util.Random;

public class DumbAiTank extends Tank {
    private static int health = 5;
    public DumbAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {

        moveForward(Constants.TANK_MOVEMENT_SPEED);
        turnLeft(Constants.TANK_TURN_SPEED);

        decrementCoolDown();

        Random randShoot = new Random();
        if(randShoot.nextFloat() < 0.03) {
            fireShell(gameWorld);
        }
    }

    public static int getHealth() {
        return health;
    }
    public void decreaseHealth() {
        health--;
    }
    public static void setHealth(int h) {
        health = h;
    }
}
