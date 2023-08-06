
package game;

import java.awt.Image;

public interface VisualObject {
    
    Position getPosition();

    String getImage();

    void setPosition(Position position);

    void setImage(String img);
}
