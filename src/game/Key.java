
package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Key {

    private final int keyCode;

    public Key(int keyCode) {
        this.keyCode = keyCode;
    }

    public void onPressDo(Runnable functionToExecute) {

        KeyListener keyEvent = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == Key.this.keyCode) {
                    functionToExecute.run();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        Game.getInstance().addKeyListener(keyEvent);
    }

    public void onPressAndReleasedDo(Runnable functionToExecute) {
        KeyListener keyEvent = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == Key.this.keyCode) {
                    functionToExecute.run();
                }
            }
        };
        Game.getInstance().addKeyListener(keyEvent);
    }
}
