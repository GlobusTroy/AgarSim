import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private int numChildren;
    private double childrenMassRatio;
    private double senseRange;
    private Color color;

    private double motiveWeightDeath;
    private double motiveWeightMass;

    private NaturalLaws naturalLaws;

    private int invincibleFrames;

    public Cell(double x, double y, double mass, double splitPoint, int numChildren, double childrenMassRatio,
                Color color, double senseRange) {
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.splitPoint = splitPoint;
        this.numChildren = numChildren;
        this.childrenMassRatio = childrenMassRatio;
        this.color = color;
        this.senseRange = senseRange;
        this.invincibleFrames = 0;

        this.motiveWeightDeath = -100.0; //Temp
        this.motiveWeightMass = 10.0;    //Temp

        vx = 0;
        vy = 0;
        this.naturalLaws = null;
    }

    public Cell(double x, double y, double mass, double splitPoint, int numChildren, double childrenMassRatio, Color color, double senseRange, NaturalLaws naturalLaws) {
        this(x, y, mass, splitPoint, numChildren, childrenMassRatio, color, senseRange);
        this.naturalLaws = naturalLaws;
    }

    public Cell (Cell other) {
        this.x = other.x;
        this.y = other.y;
        this.mass = other.mass;
        this.splitPoint = other.splitPoint;
        this.numChildren = other.numChildren;
        this.childrenMassRatio = other.childrenMassRatio;
        this.color = other.color;
        this.senseRange = other.senseRange;
        this.invincibleFrames = 0;

        this.motiveWeightDeath = other.motiveWeightDeath;
        this.motiveWeightMass = other.motiveWeightMass;
        this.naturalLaws = other.naturalLaws;
    }

    public void setNaturalLaws(NaturalLaws naturalLaws) {
        this.naturalLaws = naturalLaws;
    }

    public NaturalLaws getNaturalLaws() {
        return this.naturalLaws;
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

    public boolean isDetectable() {
        return !isInvincible();
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
        return naturalLaws.getCellSpeedBase() / Math.sqrt(mass);
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
        return mass <= naturalLaws.getCellFaminePoint();
    }

    public void kill() {
        mass = 0;
    }

    public double getRadius() {
        return mass/2;
    }

    public List<Cell> spawnOffspring() {
        List<Cell> offspring = new ArrayList<>();

        double childMass = childrenMassRatio * mass;

        for(int i = 0; i < numChildren; i++) {
            if (mass >= childMass) {
                mass -= childMass;
                Cell child = new Cell(this);
                child.mass = childMass;
                double angle = Math.random() * 2 * Math.PI;
                child.vx = getMaxSpeed()*(Math.cos(angle));
                child.vy = getMaxSpeed()*(Math.sin(angle));
                child.enforceSpeedLimit();

                child.color = this.color;

                /*//Temp
                if (Math.random() > 0.9) {
                    float[] hsb = getRandomColorAsFloatArray();
                    child.color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                } else {
                    child.color = this.color;
                }*/

                child.invincibleFrames = 10;
                offspring.add(child);
            }
        }

        return offspring;
    }

    private float[] getRandomColorAsFloatArray() {
        return Color.RGBtoHSB((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255), null);
    }

    public void drawMe(Graphics g) {
        double r = getRadius();
        g.setColor(getColor());
        g.drawOval((int) (x - r), (int) (y - r), (int) (2*r), (int) (2*r));
    }

}
