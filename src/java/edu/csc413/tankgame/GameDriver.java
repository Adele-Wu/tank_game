package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import edu.csc413.tankgame.view.Sounds;

public class GameDriver {
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameWorld gameWorld;

    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed);
        runGameView = mainView.getRunGameView();
        gameWorld = new GameWorld();
    }

    public void start() {
        mainView.setScreen(MainView.Screen.START_GAME_SCREEN);
    }

    private void startMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case StartMenuView.START_BUTTON_ACTION_COMMAND -> runGame();
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }

    private void runGame() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        Runnable gameRunner = () -> {
            setUpGame();
            while (updateGame()) {
                runGameView.repaint();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }

                KeyboardReader keyboard = KeyboardReader.instance();
                if (keyboard.escapePressed() || hasDied()){
                    break;
                }
            }
            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            resetGame();
        };
        new Thread(gameRunner).start();
    }

    /**
     * setUpGame is called once at the beginning when the game is started. Entities that are present from the start
     * should be initialized here, with their corresponding sprites added to the RunGameView.
     */
    private void setUpGame() {
        // TODO: Implement.
        PlayerTank playerTank =
                new PlayerTank(
                        Constants.PLAYER_TANK_ID,
                        Constants.PLAYER_TANK_INITIAL_X,
                        Constants.PLAYER_TANK_INITIAL_Y,
                        Constants.PLAYER_TANK_INITIAL_ANGLE);

        DumbAiTank aiTank1 =
                new DumbAiTank(
                        Constants.AI_TANK_1_ID,
                        Constants.AI_TANK_1_INITIAL_X,
                        Constants.AI_TANK_1_INITIAL_Y,
                        Constants.AI_TANK_1_INITIAL_ANGLE);

        SmartAiTank aiTank2 =
                new SmartAiTank(
                        Constants.AI_TANK_2_ID,
                        Constants.AI_TANK_2_INITIAL_X,
                        Constants.AI_TANK_2_INITIAL_Y,
                        Constants.AI_TANK_2_INITIAL_ANGLE);

        PowerUps powerUps1 = new PowerUps(Constants.POWERUP1_ID, Constants.POWERUP1_X,Constants.POWERUP1_Y,0);
        PowerUps powerUps2 = new PowerUps(Constants.POWERUP2_ID,Constants.POWERUP2_X,Constants.POWERUP2_Y,0);

        gameWorld.addEntity(playerTank);
        gameWorld.addEntity(aiTank1);
        gameWorld.addEntity(aiTank2);
        gameWorld.addEntity(powerUps1);
        gameWorld.addEntity(powerUps2);

        runGameView.addSprite(
                playerTank.getId(),
                runGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle());

        runGameView.addSprite(
                aiTank1.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank1.getX(),
                aiTank1.getY(),
                aiTank1.getAngle());

        runGameView.addSprite(
                aiTank2.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank2.getX(),
                aiTank2.getY(),
                aiTank2.getAngle());

        runGameView.addSprite(
                powerUps1.getId(),
                RunGameView.POWERUP_IMAGE_FILE,
                powerUps1.getX(),
                powerUps1.getY(),
                powerUps1.getAngle());

        runGameView.addSprite(
                powerUps2.getId(),
                RunGameView.POWERUP_IMAGE_FILE,
                powerUps2.getX(),
                powerUps2.getY(),
                powerUps2.getAngle());

        int i = 0;
        for (WallInformation wall : WallInformation.readWalls()) {
            Wall wallInfos = new Wall("wall"+i,wall.getX(),wall.getY(),0);
            gameWorld.addEntity(wallInfos);
            runGameView.addSprite("wall"+i,wall.getImageFile(),wall.getX(),wall.getY(),0);
            i++;
        }
    }

    public boolean entitiesOverLap(Entity e1, Entity e2) {
        return e1.getX() < e2.getXBound() &&
                e1.getXBound() > e2.getX() &&
                e1.getY() < e2.getYBound() &&
                e1.getYBound() > e2.getY();
    }

    private Sounds boom;

    public void collisionHandling(Entity e1, Entity e2, GameWorld gameWorld) {
        // A tank colliding with a tank
        if (e1 instanceof Tank && e2 instanceof Tank) {
            TankTankCollide(e1, e2);
        }

        // shell w non shell entity
        if (e1 instanceof PlayerTank && e2 instanceof Shell) {
            ShellToPlayerTankCollide(e1, e2, gameWorld);
            boom.clipSound2();
        }
        if (e1 instanceof SmartAiTank && e2 instanceof Shell) {
            ShellToSmartTankCollide(e1,e2,gameWorld);
            boom.clipSound2();
        }
        if (e1 instanceof DumbAiTank && e2 instanceof Shell) {
            ShellToDumbTankCollide(e1, e2, gameWorld);
            boom.clipSound2();
        }

        if (e1 instanceof Wall && e2 instanceof Shell) {
            ShellToWallCollide(e1, e2, gameWorld);
            boom.clipSound2();
        }

        // A shell colliding with a shell
        if (e1 instanceof Shell && e2 instanceof Shell) {
            ShellToShellCollide(e1, e2, gameWorld);
            boom.clipSound2();
        }

        // tank to wall
        if (e1 instanceof Tank && e2 instanceof Wall) {
            TankToWallCollide(e1, e2);
        }

        //playerTank to powerup
        if (e1 instanceof Tank && e2 instanceof PowerUps) {
            TankToPowerUpCollide(e1, e2);
        }
    }

    public void ShellToShellCollide(Entity e1, Entity e2, GameWorld gameWorld) {
        List<Entity> entitiesToRemove = new ArrayList<>();

        entitiesToRemove.add(e1);
        entitiesToRemove.add(e2);

        for (Entity entity: entitiesToRemove) {
            gameWorld.removeEntity(entity.getId());
            gameWorld.addToGarbage(entity);
        }

        runGameView.addAnimation(
                RunGameView.SHELL_EXPLOSION_ANIMATION,
                RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                e1.getX(),
                e1.getY());

        runGameView.addAnimation(
                RunGameView.SHELL_EXPLOSION_ANIMATION,
                RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                e2.getX(),
                e2.getY());
    }

    public void ShellToWallCollide(Entity e1, Entity e2, GameWorld gameWorld) {
        List<Entity> entitiesToRemove = new ArrayList<>();
        Wall wall = (Wall) e1;
        if(wall.getHealth() == 0){
            entitiesToRemove.add(e1);
            runGameView.addAnimation(
                    RunGameView.BIG_EXPLOSION_ANIMATION,
                    RunGameView.BIG_EXPLOSION_FRAME_DELAY,
                    e1.getX(),
                    e1.getY());
        }
        else {
            wall.decreaseHealth();
        }

        entitiesToRemove.add(e2);
        for (Entity entity: entitiesToRemove) {
            gameWorld.removeEntity(entity.getId());
            gameWorld.addToGarbage(entity);
        }
        runGameView.addAnimation(
                RunGameView.SHELL_EXPLOSION_ANIMATION,
                RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                e2.getX(),
                e2.getY());
    }

    public void ShellToPlayerTankCollide(Entity e1, Entity e2, GameWorld gameWorld) {
        List<Entity> entitiesToRemove = new ArrayList<>();
        PlayerTank tank = (PlayerTank) e1;
        if(tank.getHealth() == 0){
            entitiesToRemove.add(e1);
            runGameView.addAnimation(
                    RunGameView.BIG_EXPLOSION_ANIMATION,
                    RunGameView.BIG_EXPLOSION_FRAME_DELAY,
                    e1.getX(),
                    e1.getY());
        }
        else {
            tank.decreaseHealth();
        }

        entitiesToRemove.add(e2);
        for (Entity entity: entitiesToRemove) {
            gameWorld.removeEntity(entity.getId());
            gameWorld.addToGarbage(entity);
        }

        runGameView.addAnimation(
                RunGameView.SHELL_EXPLOSION_ANIMATION,
                RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                e2.getX(),
                e2.getY());
    }

    public void ShellToSmartTankCollide(Entity e1, Entity e2, GameWorld gameWorld) {
        List<Entity> entitiesToRemove = new ArrayList<>();
        SmartAiTank tank = (SmartAiTank) e1;
        if(tank.getHealth() == 0){
            entitiesToRemove.add(e1);
            runGameView.addAnimation(
                    RunGameView.BIG_EXPLOSION_ANIMATION,
                    RunGameView.BIG_EXPLOSION_FRAME_DELAY,
                    e1.getX(),
                    e1.getY());
        }
        else {
            tank.decreaseHealth();
        }

        entitiesToRemove.add(e2);
        for (Entity entity: entitiesToRemove) {
            gameWorld.removeEntity(entity.getId());
            gameWorld.addToGarbage(entity);
        }

        runGameView.addAnimation(
                RunGameView.SHELL_EXPLOSION_ANIMATION,
                RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                e2.getX(),
                e2.getY());
    }

    public void ShellToDumbTankCollide(Entity e1, Entity e2, GameWorld gameWorld) {
        List<Entity> entitiesToRemove = new ArrayList<>();
        DumbAiTank tank = (DumbAiTank) e1;
        if(tank.getHealth() == 0){
            entitiesToRemove.add(e1);
            runGameView.addAnimation(
                    RunGameView.BIG_EXPLOSION_ANIMATION,
                    RunGameView.BIG_EXPLOSION_FRAME_DELAY,
                    e1.getX(),
                    e1.getY());
        }
        else {
            tank.decreaseHealth();
        }

        entitiesToRemove.add(e2);
        for (Entity entity: entitiesToRemove) {
            gameWorld.removeEntity(entity.getId());
            gameWorld.addToGarbage(entity);
        }

        runGameView.addAnimation(
                RunGameView.SHELL_EXPLOSION_ANIMATION,
                RunGameView.SHELL_EXPLOSION_FRAME_DELAY,
                e2.getX(),
                e2.getY());
    }

    public void TankToPowerUpCollide(Entity e1, Entity e2) {
        List<Entity> entitiesToRemove = new ArrayList<>();
        PlayerTank.increaseHealth();

        entitiesToRemove.add(e2);
        for (Entity entity: entitiesToRemove) {
            gameWorld.removeEntity(entity.getId());
            gameWorld.addToGarbage(entity);
        }
    }

    public void TankTankCollide(Entity e1, Entity e2) {
        double num1 = e1.getXBound() - e2.getX();
        double num2 = e2.getXBound() - e1.getX();
        double num3 = e1.getYBound() - e2.getY();
        double num4 = e2.getYBound() - e1.getY();
        double [] findMin = {num1, num2, num3, num4};
        double smallest = num1;

        // Find smallest number
        for (int i = 0; i < 4; i++) {
            if (smallest > findMin[i]) {
                smallest = findMin[i];
            }
        }

        // If number 1 is smallest
        if (num1 == smallest) {
            e1.setX(e1.getX() - smallest / 2);
            e2.setX(e2.getX() + smallest / 2);
        }

        // If number 2 is smallest
        if (num2 == smallest) {
            e1.setX(e1.getX() + smallest / 2);
            e2.setX(e2.getX() - smallest / 2);
        }

        // If number 3 is smallest
        if (num3 == smallest) {
            e1.setY(e1.getY() - smallest / 2);
            e2.setY(e2.getY() + smallest / 2);
        }

        // If number 4 is smallest
        if (num4 == smallest) {
            e1.setY(e1.getY() + smallest / 2);
            e2.setY(e2.getY() - smallest / 2);
        }
    }

    public void TankToWallCollide(Entity e1, Entity e2) {
        double num1 = e1.getXBound() - e2.getX();
        double num2 = e2.getXBound() - e1.getX();
        double num3 = e1.getYBound() - e2.getY();
        double num4 = e2.getYBound() - e1.getY();
        double [] findMin = {num1, num2, num3, num4};
        double smallest = num1;

        // Find smallest number
        for (int i = 0; i < 4; i++) {
            if (smallest > findMin[i]) {
                smallest = findMin[i];
            }
        }

        // If number 1 is smallest
        if (num1 == smallest) {
            e1.setX(e1.getX() - smallest);
        }

        // If number 2 is smallest
        if (num2 == smallest) {
            e1.setX(e1.getX() + smallest);
        }

        // If number 3 is smallest
        if (num3 == smallest) {
            e1.setY(e1.getY() - smallest);
        }

        // If number 4 is smallest
        if (num4 == smallest) {
            e1.setY(e1.getY() + smallest);
        }
    }

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */
    private boolean updateGame() {
        // TODO: Implement.

        for (Entity entity: new ArrayList<>(gameWorld.getEntities())){
            entity.move(gameWorld);
            entity.checkBounds();
        }

        List<Entity> tempShells = gameWorld.getTempEntities();
        for (Entity newShellEntity : tempShells) {
            runGameView.addSprite(
                    newShellEntity.getId(),
                    RunGameView.SHELL_IMAGE_FILE,
                    newShellEntity.getX(),
                    newShellEntity.getY(),
                    newShellEntity.getAngle());
            System.out.println(newShellEntity.getId());
        }

        for (Entity entity : tempShells) {
            gameWorld.addEntity(entity);
        }

        tempShells.removeAll(tempShells);

        for (Entity entity : gameWorld.getEntities()) {
            runGameView.setSpriteLocationAndAngle(
                    entity.getId(),
                    entity.getX(),
                    entity.getY(),
                    entity.getAngle());
        }

        for (Entity entity : gameWorld.getGarbageList()) {
            gameWorld.removeEntity(entity.getId());
            runGameView.removeSprite(entity.getId());
        }
        gameWorld.getGarbageList().clear();

        List<Entity> deleteShells = new ArrayList<>();
        for (Entity entity: gameWorld.getEntities()) {
            if(entity.checkBounds()) {
                deleteShells.add(entity);
            }
        }

        for (Entity entity: deleteShells) {
            gameWorld.removeEntity(entity.getId());
            runGameView.removeSprite(entity.getId());
        }

        // Collision handling
        for (int i = 0; i < gameWorld.getEntities().size(); i++) {
            for (int j = i + 1; j < gameWorld.getEntities().size(); j++) {
                if (entitiesOverLap(gameWorld.getEntities().get(i), gameWorld.getEntities().get(j))) {
                    collisionHandling(gameWorld.getEntities().get(i), gameWorld.getEntities().get(j), gameWorld);
                }
            }
        }
        return true;
    }

    public boolean hasDied(){
        if (PlayerTank.getHealth() == 0 || SmartAiTank.getHealth() == 0 && DumbAiTank.getHealth() == 0) {
            return true;
        }
        return false;
    }

    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame() {
        // TODO: Implement.
        runGameView.reset();
        gameWorld.reset();
        PlayerTank.setHealth(5);
        SmartAiTank.setHealth(5);
        DumbAiTank.setHealth(5);
    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
