import java.awt.*;

/**
 * Created by troymartin on 3/1/17.
 */
public interface PhysicalEntity {

    double getX();
    double getY();
    double getMass();
    double getRadius();
    void kill();
    boolean isDead();
    void drawMe(Graphics g);

}
