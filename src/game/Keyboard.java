package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard {

    public Key up() {
        return new Key(KeyEvent.VK_UP);
    }
    public Key down() {
        return new Key(KeyEvent.VK_DOWN);
    }
    public Key left() {
        return new Key(KeyEvent.VK_LEFT);
    }
    public Key right() {
        return new Key(KeyEvent.VK_RIGHT);
    }

    public Key enter() {
        return new Key(KeyEvent.VK_ENTER);
    }
    
     public Key esc() {
        return new Key(KeyEvent.VK_ESCAPE);
    }

    public Key ctrl() {
        return new Key(KeyEvent.VK_CONTROL);
    }

    public Key alt() {
        return new Key(KeyEvent.VK_ALT);
    }

    public Key shift() {
        return new Key(KeyEvent.VK_SHIFT);
    }

    public Key space() {
        return new Key(KeyEvent.VK_SPACE);
    }

    public Key tab() {
        return new Key(KeyEvent.VK_TAB);
    }

    public Key delete() {
        return new Key(KeyEvent.VK_DELETE);
    }

    public Key insert() {
        return new Key(KeyEvent.VK_INSERT);
    }

    public Key home() {
        return new Key(KeyEvent.VK_HOME);
    }

    public Key end() {
        return new Key(KeyEvent.VK_END);
    }

    public Key pageUp() {
        return new Key(KeyEvent.VK_PAGE_UP);
    }

    public Key pageDown() {
        return new Key(KeyEvent.VK_PAGE_DOWN);
    }

    public Key backspace() {
        return new Key(KeyEvent.VK_BACK_SPACE);
    }

    public Key a() {
        return new Key((int)'A');
    }

    public Key b() {
        return new Key('B');
    }

    public Key c() {
        return new Key('C');
    }

    public Key d() {
        return new Key('D');
    }

    public Key e() {
        return new Key('E');
    }

    public Key f() {
        return new Key('F');
    }

    public Key g() {
        return new Key('G');
    }

    public Key h() {
        return new Key('H');
    }

    public Key i() {
        return new Key('I');
    }

    public Key j() {
        return new Key('J');
    }

    public Key k() {
        return new Key('K');
    }

    public Key l() {
        return new Key('L');
    }

    public Key m() {
        return new Key('M');
    }

    public Key n() {
        return new Key('N');
    }

    public Key o() {
        return new Key('O');
    }

    public Key p() {
        return new Key('P');
    }

    public Key q() {
        return new Key('Q');
    }

    public Key r() {
        return new Key('R');
    }

    public Key s() {
        return new Key('S');
    }

    public Key t() {
        return new Key('T');
    }

    public Key u() {
        return new Key('U');
    }

    public Key v() {
        return new Key('V');
    }

    public Key w() {
        return new Key('W');
    }

    public Key x() {
        return new Key('X');
    }

    public Key y() {
        return new Key('Y');
    }

    public Key z() {
        return new Key('Z');
    }
}

class Key {

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
