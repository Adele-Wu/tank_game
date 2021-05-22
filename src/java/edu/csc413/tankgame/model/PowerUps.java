package edu.csc413.tankgame.model;

public class PowerUps extends Entity {

    public PowerUps(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {

    }

    @Override
    public double getXBound() {
        return getX()+50;
    }

    @Override
    public double getYBound() {
        return getY()+50;
    }

    @Override
    public boolean checkBounds() {
        return false;
    }
}
