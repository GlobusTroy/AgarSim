import java.awt.*;

/**
 * Created by troymartin on 3/1/17.
 */
public class Cell implements PhysicalEntity{

    private double x;
    private double y;
    private double vx;
    private double vy;
    private double mass;

    private double splitPoint;
    private double senseRange;
    private Color color;

    private double motiveWeightDeath;
    private double motiveWeightMass;

    private int invincibleFrames;

    public Cell(double x, double y, double mass, double splitPoint, Color color, double senseRange) {
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.splitPoint = splitPoint;
        this.color = color;
        this.senseRange = senseRange;
        this.invincibleFrames = 0;

        this.motiveWeightDeath = -100.0;
        this.motiveWeightMass = 10.0;


        vx = 0;
        vy = 0;
    }

    public Cell (Cell other) {
        this.x = other.x;
        this.y = other.y;
        this.mass = other.mass;
        this.splitPoint = other.splitPoint;
        this.color = other.color;
        this.senseRange = other.senseRange;
        this.invincibleFrames = 0;

        this.motiveWeightDeath = other.motiveWeightDeath;
        this.motiveWeightMass = other.motiveWeightMass;
    }

    public double getIncentiveMass() {
        return motiveWeightMass;
    }

    public double getIncentiveDeath() {
        return motiveWeightDeath;
    }

    public void move() {
        x+= vx;
        y+= vy;
    }

    public boolean isInvincible() {
        return invincibleFrames > 0;
    }

    public void buffTick() {
        if (isInvincible()) {
            invincibleFrames -= 1;
        }
    }

    public void impressForces(Force[] forces) {
        for (Force force : forces) {
            double angle = force.getAngle();
            double mag = force.getMagnitude();
            vx += (Math.cos(angle) * mag)/mass;
            vy += (Math.sin(angle) * mag)/mass;
        }
        enforceSpeedLimit();
    }

    private void enforceSpeedLimit() {
        double speed = Math.hypot(vx,vy);
        if (speed > getMaxSpeed()) {
            double fixRatio = getMaxSpeed() / speed;
            vx *= fixRatio;
            vy *= fixRatio;
        }
    }

    public double getMaxSpeed() {
        return NaturalLaws.CELL_SPEED_BASE / Math.sqrt(mass);
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

    private double getSplitPoint() {
        return splitPoint;
    }

    public Color getColor() {

        if (!isInvincible()) {
            return color;
        } else {
            return Color.white;
        }
    }

    public double getSenseRange() {
        return senseRange;
    }

    public void addMass(double mass) {
        this.mass += mass;
    }

    public void removeMass(double mass) {
        this.mass -= mass;
    }

    public boolean isReadyToDivide() {
        if (isInvincible()) return false;
        return mass >= splitPoint;
    }

    public boolean isDead() {
        return mass <= NaturalLaws.CELL_FAMINE_POINT;
    }

    public void kill() {
        mass = 0;
    }

    public double getRadius() {
        return mass/2;
    }

    public Cell divideSelf() {
        mass /= 2;
        Cell newCell = new Cell(this);
        double angle = Math.random() * 2 * Math.PI;
        newCell.vx = getMaxSpeed()*(Math.cos(angle));
        newCell.vy = getMaxSpeed()*(Math.sin(angle));
        newCell.enforceSpeedLimit();

        if (Math.random() > 0.9) {
            float[] hsb = Color.RGBtoHSB((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255), null);
            newCell.color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        } else {
            newCell.color = this.color;
        }

        newCell.invincibleFrames = 10;

        return newCell;
    }

    public void drawMe(Graphics g) {
        double r = getRadius();
        g.setColor(getColor());
        g.drawOval((int) (x - r), (int) (y - r), (int) (2*r), (int) (2*r));
    }

}
