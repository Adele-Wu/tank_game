package edu.csc413.tankgame.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class StartMenuView extends JPanel {
    public static final Dimension SCREEN_DIMENSIONS = new Dimension(510, 550);
    private static final String START_MENU_IMAGE_FILE = "title.png";

    private static final Dimension BUTTON_SIZE = new Dimension(200, 100);
    private static final Font BUTTON_FONT = new Font("Consolas", Font.BOLD, 22);
    private static final Rectangle START_BUTTON_BOUNDS = new Rectangle(150, 300, 200, 50);
    private static final Rectangle EXIT_BUTTON_BOUNDS = new Rectangle(150, 400, 200, 50);

    public static final String START_BUTTON_ACTION_COMMAND = "start_ac";
    public static final String EXIT_BUTTON_ACTION_COMMAND = "exit_ac";

    private final BufferedImage menuBackground;

    public StartMenuView(String startButtonText, ActionListener startMenuListener) {
        URL imageUrl = getClass().getClassLoader().getResource(START_MENU_IMAGE_FILE);
        if (imageUrl == null) {
            throw new RuntimeException("Background image file not found: " + START_MENU_IMAGE_FILE);
        }
        try {
            menuBackground = ImageIO.read(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setBackground(Color.BLACK);
        setLayout(null);

        addButton(startButtonText, START_BUTTON_BOUNDS, START_BUTTON_ACTION_COMMAND, startMenuListener);
        addButton("Exit", EXIT_BUTTON_BOUNDS, EXIT_BUTTON_ACTION_COMMAND, startMenuListener);
    }

    private void addButton(
            String buttonText, Rectangle buttonBounds, String buttonActionCommand, ActionListener actionListener) {
        JButton button = new JButton(buttonText);
        button.setSize(BUTTON_SIZE);
        button.setFont(BUTTON_FONT);
        button.setBounds(buttonBounds);
        button.setActionCommand(buttonActionCommand);
        button.addActionListener(actionListener);
        add(button);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(menuBackground, 0, 0, null);
    }
}
