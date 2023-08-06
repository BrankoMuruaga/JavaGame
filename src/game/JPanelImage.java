package game;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelImage extends JPanel {

    private int x, y;
    private final String path;
    private JPanel panel;
    private Image img;
    private String id;

    public JPanelImage(JPanel panel, String path) {
        this.panel = panel;
        this.path = path;
        this.x = panel.getWidth();
        this.y = panel.getHeight();
        this.setSize(x, y);
        this.setOpaque(false);
        this.loadImage();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
    private void loadImage() {
        img = new ImageIcon(getClass().getResource(path)).getImage();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, x, y, this.panel);
        }
    }
    
    public void removeImage() {
        img = null;
        repaint();
    }
    
    //@Override
    //public void paint(Graphics g) { //ImageIcon image = new ImageIcon(img.getImage().getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT));
    //    g.drawImage(img, 0, 0, x, y, this.panel);
    //    super.paint(g);
    //}
}
