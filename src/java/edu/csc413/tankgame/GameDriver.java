package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GameDriver {
    private final MainView mainView;
    private final RunGameView runGameView;
    //    private final List<Tank> entities;
    private final GameWorld gameWorld;

    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed);
        runGameView = mainView.getRunGameView();
//        entities = new ArrayList<>();
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
        List<WallInformation> wallInfos = WallInformation.readWalls();

//        x = 100.0;
//        y = 100.0;
//        angle = Math.toRadians(0.0);
//
//        runGameView.addSprite("tank-1", "player-tank.png", x, y, angle);

        PlayerTank playerTank =
                new PlayerTank(
                        Constants.PLAYER_TANK_ID,
                        Constants.PLAYER_TANK_INITIAL_X,
                        Constants.PLAYER_TANK_INITIAL_Y,
                        Constants.PLAYER_TANK_INITIAL_ANGLE);

//        playerTank.move();
//        aiTank1.move();
//        aiTank2.move();
//        shell1.move();

        DumbAiTank aiTank1 =
                new DumbAiTank(
                        Constants.AI_TANK_1_ID,
                        Constants.AI_TANK_1_INITIAL_X,
                        Constants.AI_TANK_1_INITIAL_Y,
                        Constants.AI_TANK_1_INITIAL_ANGLE);

//        Entity shell = new Shell("shell-id", playerTank.getX(), playerTank.getY(), Math.toRadians(45.0));

        gameWorld.addEntity(playerTank);
        gameWorld.addEntity(aiTank1);
//        gameWorld.addEntity(shell);

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

//        runGameView.addSprite(
//                shell.getId(),
//                RunGameView.SHELL_IMAGE_FILE,
//                shell.getX(),
//                shell.getY(),
//                shell.getAngle());

    }

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */
    private boolean updateGame() {
        // TODO: Implement.

        // tank moving
//        x += 2.0;
//        y += 1.0;
//        angle += 1.0;
//
//        runGameView.setSpriteLocationAndAngle("tank-1", x, y, angle);

        // keyboard reading
//        KeyboardReader keyboard = KeyboardReader.instance();
//        if (keyboard.upPressed())
//        {
//            System.out.println("up is pressed");
//        }
//        if (keyboard.downPressed())
//        {
//            System.out.println("down is pressed");
//        }
//        if (keyboard.leftPressed())
//        {
//            System.out.println("left is pressed");
//        }
//        if (keyboard.rightPressed())
//        {
//            System.out.println("right is pressed");
//        }

//        for(Tank entity: gameWorld.getEntities()) {
//            entity.move(gameWorld);
//        }
//
//        for (Tank entity: gameWorld.getEntities()) {
//            runGameView.setSpriteLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
//        }

// ---------------------------------------------------------------------------------------------------------------
//        // from Dawson's lecture, doesnt fully work. make it work
//        ArrayList<Entity> originalEntities = new ArrayList<>(gameWorld.getEntities()); //creates copy of list
//        for(Entity entity: originalEntities) {  //iterates the copy
//            entity.move(gameWorld); // moves original
//        }
// --------------------------------------------------------------------------------------------------------------

        // when shells are added, dont add them to the entities list directly
        // put them in a separate temporary list instead.
        // process (addSprite) that separate temp list, and then move them all to main list

        for (Entity entity: new ArrayList<>(gameWorld.getEntities())){
            entity.move(gameWorld);
        }

        List<Entity> tempShells = gameWorld.getTempEntities();
        for (Entity newShellEntity : tempShells) {
            runGameView.addSprite(
                    newShellEntity.getId(),
                    RunGameView.SHELL_IMAGE_FILE,
                    newShellEntity.getX(),
                    newShellEntity.getY(),
                    newShellEntity.getAngle());
        }

        for (Entity entity : tempShells) {
            gameWorld.addEntity(entity);
        }

        tempShells.removeAll(tempShells);
        //tempShells.remove(tempShells);

        for (Entity entity : gameWorld.getEntities()) {
            runGameView.setSpriteLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
        }

// ---------------------------------------------------------------------------------------------------------------

        for (Entity entity : gameWorld.getGarbageList()) {
            entity.checkBounds(gameWorld);
            gameWorld.removeShell(entity);
        }

        return true;

    }

    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame() {
        // TODO: Implement.
        runGameView.reset();
    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
