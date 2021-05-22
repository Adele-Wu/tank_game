package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import java.util.Random;

public class SmartAiTank extends Tank {
    private static int health = 5;
    public SmartAiTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
        Entity playerTank = gameWorld.getEntity(Constants.PLAYER_TANK_ID);

        double x = playerTank.getX() - getX();
        double y = playerTank.getY() - getY();

        double distance = Math.sqrt(x*x + y*y);
        double angleToPlayer = Math.atan2(y, x);
        double angleDifference = getAngle() - angleToPlayer;

        if(distance > 250.0) {
            moveForward(Constants.TANK_MOVEMENT_SPEED);
        }

        angleDifference -= Math.floor(angleDifference / Math.toRadians(360.0) + 0.5) * Math.toRadians(360.0);

        if (angleDifference < -Math.toRadians(3.0)) {
            turnRight(Constants.TANK_TURN_SPEED);

        } else if (angleDifference > Math.toRadians(3.0)) {
            turnLeft(Constants.TANK_TURN_SPEED);
        }

        decrementCoolDown();

        Random randShoot = new Random();
        if(randShoot.nextFloat() < 0.02) {
            fireShell(gameWorld);
        }
        //fireShell(gameWorld);

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