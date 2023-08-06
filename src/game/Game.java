package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.border.Border;

interface VisualObject {

    Position getPosition();

    String getImage();

    void setPosition(Position position);

    void setImage(Image img);
}

public class Game extends JFrame {

    private static Game instance;

    public int width;
    public int height;
    public int rows;
    public int cols;
    private int cellSize;
    private JPanel tablero;
    private JPanel[] paneles;
    private String pathBackground;
    private final Hover hover = new Hover();
    private final Timer timer = new Timer();
    private final TickEvent tick = new TickEvent();
    private final List<TickEvent> ticksEvent;
    private final List<VisualObject> objectsInGame;
    private final List<JPanelImage> imagesInGame;
    private final Position origin = new Position(0, 0);
    private final Position center;
    private static final Keyboard keyboard = new Keyboard();

    public static Game getInstance() {
        return instance;
    }

    public Game(int width, int height, int cellSize) {
        this(width, height, cellSize, null);
    }

    public Game(int width, int height, int cellSize, String pathBackground) {
        instance = this;
        ticksEvent = new ArrayList<>();
        objectsInGame = new ArrayList<>();
        imagesInGame = new ArrayList<>();
        this.pathBackground = pathBackground;
        tablero = new GameBackground(this.pathBackground);
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.getContentPane().add(this.tablero);

        rows = this.height / this.cellSize;
        cols = this.width / this.cellSize;
        this.center = new Position(this.cols / 2, this.rows / 2);
        tablero.setLayout(new GridLayout(rows, cols));
        paneles = new JPanel[rows * cols];

        this.addPanels();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.start();
    }

    private void start() {
        tick.addTickEvent(1, () -> {
            this.repaint();
        });
    }

    private void addPanels() {
        for (int i = 0; i < rows * cols; i++) {
            int row = i / cols;
            int col = i % cols;

            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(this.cellSize, this.cellSize));
            panel.setName(col + "," + row);
            panel.setLayout(null);
            panel.setOpaque(false);
            this.paneles[i] = panel;
            tablero.add(panel);
        }
    }

    public Position origin() {
        return this.origin;
    }

    public Position center() {
        return this.center;
    }

    public List<VisualObject> allVisuals() {
        return objectsInGame;
    }

    public VisualObject getObjectIn(Position position) {
        for (VisualObject object : objectsInGame) {
            if (object.getPosition() == position) {
                return object;
            }
        }
        return null;
    }

    private JPanel getElementIn(Position position) {
        int index = position.getY() * (this.width / this.cellSize) + position.getX();
        Component[] components = this.tablero.getComponents();
        if (index >= 0 && index < components.length && components[index] instanceof JPanel jPanel) {
            return jPanel;
        } else {
            return null;
        }
    }

    public void addVisualIn(VisualObject objeto, Position position) {
        objectsInGame.add(objeto);
        JPanelImage miImagen = new JPanelImage(this.getElementIn(position), objeto.getImage());
        miImagen.setId(objeto.toString());
        imagesInGame.add(miImagen);
        this.getElementIn(position).add(miImagen).repaint();
    }

    public void addVisualCharacter(VisualObject objeto) {
        this.addVisual(objeto);
        this.addControls(objeto);
    }

    private void addControls(VisualObject objeto) {
        keyboard.down().onPressDo(() -> {
            this.moverAbajo(objeto);
        });
        keyboard.up().onPressDo(() -> {
            this.moverArriba(objeto);
        });
        keyboard.left().onPressDo(() -> {
            this.moverIzquierda(objeto);
        });
        keyboard.right().onPressDo(() -> {
            this.moverDerecha(objeto);
        });
    }

    private void moverAbajo(VisualObject objeto) {
        this.removeVisual(objeto);
        objeto.setPosition(objeto.getPosition().down(1));
        this.addVisual(objeto);
    }

    private void moverIzquierda(VisualObject objeto) {
        this.removeVisual(objeto);
        objeto.setPosition(objeto.getPosition().left(1));
        this.addVisual(objeto);
    }

    private void moverDerecha(VisualObject objeto) {
        this.removeVisual(objeto);
        objeto.setPosition(objeto.getPosition().right(1));
        this.addVisual(objeto);
    }

    private void moverArriba(VisualObject objeto) {
        this.removeVisual(objeto);
        objeto.setPosition(objeto.getPosition().up(1));
        this.addVisual(objeto);
    }

    public void addVisual(VisualObject objeto) {
        this.addVisualIn(objeto, objeto.getPosition());
    }

    public void removeAllVisualsIn(Position position) {
        this.getElementIn(position).removeAll();
    }

    public void removeVisual(VisualObject objeto) {
        for (JPanelImage images : imagesInGame) {
            if (images.getId().equals(objeto.toString())) {
                images.removeImage();
            }
        }
    }

    public void removeAllVisuals() {
        for (JPanel panel : paneles) {
            panel.removeAll();
        }
    }

    public Position at(int x, int y) {
        return new Position(x, y);
    }

    public void addBorderInHover() {
        for (JPanel panel : paneles) {
            this.hover.borde(panel);
        }
    }

    public void addBorderInHover(Color color) {
        for (JPanel panel : paneles) {
            this.hover.borde(panel, color);
        }
    }

    public void addBorderIn(Position position, Color color) {
        this.getElementIn(position).setBorder(BorderFactory.createLineBorder(color, 2));
    }

    public void removeBorderIn(Position position) {
        this.getElementIn(position).setBorder(null);
    }

    public void addBorderIn(Position position) {
        this.getElementIn(position).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
    }

    public void addBackgroundColorIn(Position position, Color color) {
        this.getElementIn(position).setOpaque(true);
        this.getElementIn(position).setBackground(color);
    }

    public void whenClicked(Runnable functionToExecute) {
        for (JPanel panel : paneles) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        functionToExecute.run();
                    }
                }
            });
        }
    }

    public void schedule(int miliseconds, Runnable functionToExecute) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                functionToExecute.run();
            }
        }, miliseconds);
    }

    public void onTick(int miliseconds, String name, Runnable functionToExecute) {
        tick.setId(name);
        ticksEvent.add(tick);
        tick.addTickEvent(miliseconds, functionToExecute);
    }

    public void removeTickEvent(String name) {
        for (TickEvent ticks : ticksEvent) {
            if (ticks.getId().equals(name)) {
                ticks.removeTickEvent();
                ticksEvent.remove(ticks);
                break;
            }
        }
    }

    public void whenCollideDo(VisualObject object, Runnable functionToExecute) {
        this.onTick(200, "whenColiderDo", () -> {
            if (Game.this.getObjectIn(object.getPosition()) != null) {
                functionToExecute.run();
            }
        });
    }

    public VisualObject uniqueCollider(VisualObject object) {
        if (this.getObjectIn(object.getPosition()) != null) {
            return this.getObjectIn(object.getPosition());
        }
        return null;
    }

}

class GameBackground extends JPanel {

    private Image imagen;
    private String path;

    public GameBackground(String path) {
        this.path = path;
    }

    @Override
    public void paint(Graphics g) {
        if (this.path == null) {
            this.path = "/assets/background.png";
        }
        imagen = new ImageIcon(getClass().getResource(path)).getImage();
        g.drawImage(imagen, 0, 0, this.getWidth(), this.getHeight(), this);
        this.setOpaque(false);
        super.paint(g);
    }
}

class TickEvent {

    private final Timer timer = new Timer();
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addTickEvent(int miliseconds, Runnable functionToExecute) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                functionToExecute.run();
            }
        }, 0, miliseconds);
    }

    public void removeTickEvent() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
            }
        }, 0);
    }
}

class Hover {

    private Border bordeAnterior = null;
    private Color colorAnterior = null;
    private boolean isOpaque;

    public void borde(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                getStatus(panel);
                panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setStatus(panel);

            }
        });
    }

    public void borde(JPanel panel, Color color) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                getStatus(panel);
                panel.setBorder(BorderFactory.createLineBorder(color, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setStatus(panel);
            }
        });
    }

    private void getStatus(JPanel panel) {
        bordeAnterior = panel.getBorder();
        colorAnterior = panel.getBackground();
        isOpaque = panel.isOpaque();
    }

    private void setStatus(JPanel panel) {
        panel.setBorder(bordeAnterior);
        panel.setBackground(colorAnterior);
        panel.setOpaque(isOpaque);
    }
}
