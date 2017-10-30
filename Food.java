import java.awt.*;

/**
 * Created by troymartin on 3/1/17.
 */
public class Food implements PhysicalEntity {

    private double x;
    private double y;
    private double mass;

    public Food(double x, double y, double mass) {
        this.x = x;
        this.y = y;
        this.mass = mass;
    }

    public boolean isDead() {
        return (mass <= 0);
    }

    public boolean isDetectable() {
        return true;
    }

    public void kill() {
        mass = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return mass/2;
    }

    public void drawMe(Graphics g) {
        double r = getRadius();
        g.setColor(Color.green);
        g.drawRect((int) (x - r), (int) (y - r), (int) (2*r), (int) (2*r));
    }
}
