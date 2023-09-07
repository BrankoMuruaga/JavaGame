package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.border.Border;

public class Game extends JFrame implements Runnable {

    private static Game instance;
    private static Thread thread;
    private static volatile boolean running = false;

    private int width;
    private int height;
    private int rows;
    private int cols;
    private int cellSize;
    private JPanel tablero;
    private JPanel[][] paneles;
    private String pathBackground;
    private final Hover hover = new Hover();
    private final Timer timer = new Timer();

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
        paneles = new JPanel[rows][cols];

        this.addPanels();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.start();
    }

    private synchronized void start() {
        running = true;

        thread = new Thread(this, "Graficos");
        thread.start();
    }

    private synchronized void detener() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
        }
    }

    private void actualizar() {
    }

    private void mostrar() {
        this.repaint();
    }

    @Override
    public void run() {
        final int NS_POR_SEGUNDO = 1000000000;
        final byte APS_OBJETIVO = 60;
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

        long referenciaActualizacion = System.nanoTime();

        double tiempoTranscurrido;
        double delta = 0;

        while (running) {
            final long inicioBucle = System.nanoTime();

            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            referenciaActualizacion = inicioBucle;

            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

            while (delta >= 1) {
                this.actualizar();
                delta--;
            }

            this.mostrar();
        }
    }

    public int height() {
        return rows;
    }

    public int width() {
        return cols;
    }

    private void addPanels() {
        for (int i = 0; i < this.paneles.length; i++) {
            for (int j = 0; j < this.paneles[i].length; j++) {
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(this.cellSize, this.cellSize));
                panel.setName(j + "," + i);
                panel.setLayout(null);
                panel.setOpaque(false);
                this.paneles[i][j] = panel;
                tablero.add(panel);
            }
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

    public Set<VisualObject> getObjectIn(Position position) {
        Set<VisualObject> objects = new HashSet<>();
        for (VisualObject object : objectsInGame) {
            if (object.getPosition() == position) {
                objects.add(object);
            }
        }
        return objects;
    }

    private JPanel getElementIn(Position position) {
        int row = position.getY();
        int col = position.getX();

        return this.paneles[row][col];

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
        for (int i = 0; i < imagesInGame.size(); i++) {
            if (imagesInGame.get(i).getId().equals(objeto.toString())) {
                imagesInGame.get(i).removeImage();
            }
        }
    }

    public void removeAllVisuals() {
        for (JPanel[] panele : paneles) {
            for (JPanel jPanel : panele) {
                jPanel.removeAll();
            }
        }
    }

    public Position at(int x, int y) {
        return new Position(x, y);
    }

    public void addBorderInHover() {
        for (JPanel[] panele : paneles) {
            for (JPanel jPanel : panele) {
                this.hover.borde(jPanel);
            }
        }
    }

    public void addBorderInHover(Color color) {
        for (JPanel[] panele : paneles) {
            for (JPanel jPanel : panele) {
                this.hover.borde(jPanel, color);
            }
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
        for (JPanel[] panele : paneles) {
            for (JPanel jPanel : panele) {
                jPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            functionToExecute.run();
                        }
                    }
                });
            }
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
        TickEvent tick = new TickEvent();
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

    public void elementInThisPosition() {
        this.whenClicked(() -> {
        });
    }

    public void whenCollideDo(VisualObject object, VisualObject objectInColition, Runnable action) {

        this.onTick(100, "whenColiderDo", () -> {
            if (object.getPosition().equals(objectInColition.getPosition())) {
                action.run();
            }
        });
    }

    public VisualObject uniqueCollider(VisualObject object) {
        for (int i = 0; i < objectsInGame.size(); i++) {
            if (objectsInGame.get(i).getPosition().equals(object.getPosition()) && !objectsInGame.get(i).equals(object)) {
                return objectsInGame.get(i);
            }
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
