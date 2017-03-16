/**
 * Created by troymartin on 3/2/17.
 */
public class NaturalLaws {

    public static final double DEFAULT_CELL_MASS_CONSUME_RATIO = 1.25;
    public static final double DEFAULT_CELL_DECAY_PER_TICK = 0.25;
    public static final double DEFAULT_CELL_SPEED_BASE = 50;
    public static final double DEFAULT_CELL_FAMINE_POINT = 3.0;
    public static final double DEFAULT_FORCE_BASE_MULTIPLIER = 1000.0;
    public static final double DEFAULT_FORCE_COLLISION = -100.0;

    private double cellMassConsumeRatio;
    private double cellDecayPerTick;
    private double cellSpeedBase;
    private double cellFaminePoint;
    private double forceBaseMultiplier;
    private double forceCollision;

    public NaturalLaws() {
        this.cellMassConsumeRatio = DEFAULT_CELL_MASS_CONSUME_RATIO;
        this.cellDecayPerTick = DEFAULT_CELL_DECAY_PER_TICK;
        this.cellSpeedBase = DEFAULT_CELL_SPEED_BASE;
        this.cellFaminePoint = DEFAULT_CELL_FAMINE_POINT;
        this.forceBaseMultiplier = DEFAULT_FORCE_BASE_MULTIPLIER;
        this.forceCollision = DEFAULT_FORCE_COLLISION;
    }

    public NaturalLaws(double cellMassConsumeRatio, double cellDecayPerTick, double cellSpeedBase, double cellFaminePoint,
                       double forceBaseMultiplier, double forceCollision) {
        this.cellMassConsumeRatio = cellMassConsumeRatio;
        this.cellDecayPerTick = cellDecayPerTick;
        this.cellSpeedBase = cellSpeedBase;
        this.cellFaminePoint = cellFaminePoint;
        this.forceBaseMultiplier = forceBaseMultiplier;
        this.forceCollision = forceCollision;
    }

    public double getCellMassConsumeRatio() {
        return cellMassConsumeRatio;
    }

    public double getCellDecayPerTick() {
        return cellDecayPerTick;
    }

    public double getCellSpeedBase() {
        return cellSpeedBase;
    }

    public double getCellFaminePoint() {
        return cellFaminePoint;
    }

    public double getForceBaseMultiplier() {
        return forceBaseMultiplier;
    }

    public double getForceCollision() {
        return forceCollision;
    }



}
