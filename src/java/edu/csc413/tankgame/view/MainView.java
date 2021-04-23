package edu.csc413.tankgame.view;

import edu.csc413.tankgame.KeyboardReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainView {
    public enum Screen {
        START_GAME_SCREEN("start"),
        RUN_GAME_SCREEN("game"),
        END_MENU_SCREEN("end");

        private final String screenName;

        Screen(String screenName) {
            this.screenName = screenName;
        }

        public String getScreenName() {
            return screenName;
        }
    }

    private final JFrame mainJFrame;
    private final JPanel mainPanel;
    private final CardLayout mainPanelLayout;
    private final RunGameView runGameView;

    public MainView(ActionListener startMenuListener) {
        mainJFrame = new JFrame();
        mainJFrame.setVisible(false);
        mainJFrame.setResizable(false);
        mainJFrame.setTitle("Tank Wars");
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainJFrame.addKeyListener(KeyboardReader.instance());

        mainPanel = new JPanel();
        mainPanelLayout = new CardLayout();
        mainPanel.setLayout(mainPanelLayout);

        StartMenuView startMenuView = new StartMenuView("Start Game", startMenuListener);
        mainPanel.add(startMenuView, Screen.START_GAME_SCREEN.getScreenName());

        StartMenuView endGameView = new StartMenuView("Restart Game", startMenuListener);
        mainPanel.add(endGameView, Screen.END_MENU_SCREEN.getScreenName());

        runGameView = new RunGameView();
        mainPanel.add(runGameView, Screen.RUN_GAME_SCREEN.getScreenName());

        mainJFrame.add(mainPanel);
    }

    public RunGameView getRunGameView() {
        return runGameView;
    }

    public void setScreen(Screen screen) {
        mainJFrame.setVisible(false);

        Dimension screenSize = switch (screen) {
            case START_GAME_SCREEN, END_MENU_SCREEN -> StartMenuView.SCREEN_DIMENSIONS;
            case RUN_GAME_SCREEN -> RunGameView.SCREEN_DIMENSIONS;
        };
        mainJFrame.setSize(screenSize);
        mainPanelLayout.show(mainPanel, screen.getScreenName());

        mainJFrame.setVisible(true);
    }

    public void closeGame() {
        mainJFrame.dispatchEvent(new WindowEvent(mainJFrame, WindowEvent.WINDOW_CLOSING));
    }
}
