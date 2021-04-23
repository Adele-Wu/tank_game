package edu.csc413.tankgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The KeyboardReader is a singleton class that provides information about the state of the keyboard. You can ask the
 * instance if various keys are currently being pressed by calling upPressed(), downPressed(), etc.
 */
public class KeyboardReader implements KeyListener {
    private static final KeyboardReader instance = new KeyboardReader();

    public static KeyboardReader instance() {
        return instance;
    }

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean spacePressed;
    private boolean escapePressed;

    private KeyboardReader() {
        // Empty constructor. We define this private constructor to enforce the Singleton pattern.
    }

    public boolean upPressed() {
        return upPressed;
    }

    public boolean downPressed() {
        return downPressed;
    }

    public boolean leftPressed() {
        return leftPressed;
    }

    public boolean rightPressed() {
        return rightPressed;
    }

    public boolean spacePressed() {
        return spacePressed;
    }

    public boolean escapePressed() {
        return escapePressed;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        // Ignored.
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        handleKeyEvent(keyEvent.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        handleKeyEvent(keyEvent.getKeyCode(), false);
    }

    private void handleKeyEvent(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> upPressed = pressed;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> downPressed = pressed;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> leftPressed = pressed;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> rightPressed = pressed;
            case KeyEvent.VK_SPACE -> spacePressed = pressed;
            case KeyEvent.VK_ESCAPE -> escapePressed = pressed;
        }
    }
}
