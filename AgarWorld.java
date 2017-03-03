import java.awt.*;
import java.util.ArrayList;

/**
 * Created by troymartin on 3/1/17.
 */
public class AgarWorld {
    private double newFoodMassMin = 0.0;
    private double newFoodMassRange = 10.0;
    private double newFoodXMin;
    private double newFoodXRange;
    private double newFoodYMin;
    private double newFoodYRange;

    private double totalSystemMass;
    private double currentSystemMass;
    private ArrayList<PhysicalEntity> entities;

    private final double MINIMUM_COORD = 100;
    private final double CELL_START_MASS = 50;
    private final Color CELL_START_COLOR = Color.blue;
    private final double CELL_START_SPLIT_POINT = 125.0;
    private final double CELL_START_SENSE_RANGE = 1000.0;

    public AgarWorld(int cellCount, double systemSize, double systemMass) {

        newFoodXMin = MINIMUM_COORD;
        newFoodYMin = MINIMUM_COORD;
        newFoodXRange = systemSize;
        newFoodYRange = systemSize;

        entities = new ArrayList<>();
        for (int i = 0; i < cellCount; i++) {
            Cell newCell = new Cell(newFoodXMin + (newFoodXRange*Math.random()), newFoodYMin + (newFoodYRange*Math.random()), CELL_START_MASS,
                    CELL_START_SPLIT_POINT, CELL_START_COLOR, CELL_START_SENSE_RANGE);
            entities.add(newCell);
            currentSystemMass += CELL_START_MASS;
        }
        this.totalSystemMass = systemMass;
    }

    public ArrayList<PhysicalEntity> getEntities() {return entities;}

    public double getCurrentSystemMass() {
        return currentSystemMass;
    }

    private double getDistanceBetween(PhysicalEntity thing1, PhysicalEntity thing2) {
        double x1 = thing1.getX();
        double y1 = thing1.getY();
        double x2 = thing2.getX();
        double y2 = thing2.getY();
        return Math.hypot(x1-x2, y1-y2);
    }

    private double getAngleBetween(double x1, double y1, double x2, double y2) {
        return Math.atan2((y2 - y1), (x2 - x1));
    }

    private double getAngleBetween(PhysicalEntity from, PhysicalEntity towards) {
        double xFrom = from.getX();
        double yFrom = from.getY();
        double xTo = towards.getX();
        double yTo = towards.getY();
        return getAngleBetween(xFrom, yFrom, xTo, yTo);
    }

    private boolean canEat(PhysicalEntity eater, PhysicalEntity meal) {
        if (eater instanceof Cell) {
            if (meal instanceof Food) return true;
            else return (eater.getMass() >= NaturalLaws.CELL_MASS_CONSUME_RATIO * meal.getMass());
        }
        return false;

    }

    private Force[] calculateForces(Cell cell) {
        ArrayList<Force> forces = new ArrayList<>();
        for (PhysicalEntity other : entities) {
            if (cell != other) {
                Force newForce = getForceBetween(cell, other);
                if (newForce != null) forces.add(newForce);
            }
        }
        if (forces.isEmpty()) {
            forces.add(getHomeInstinctForce(cell));
        }

        return forces.toArray(new Force[forces.size()]);
    }

    private Force getHomeInstinctForce(Cell cell) {
        double homeX = (2 * newFoodXMin + newFoodXRange) / 2;
        double homeY = (2 * newFoodYMin + newFoodYRange) / 2;
        double ang = getAngleBetween(cell.getX(), cell.getY(), homeX, homeY);
        return new Force(NaturalLaws.FORCE_BASE_MULTIPLIER, ang);
    }

    private Force getForceBetween(PhysicalEntity entityForceUpon, PhysicalEntity entityForceFrom) {
        double distance = getDistanceBetween(entityForceUpon, entityForceFrom);
        if (entityForceUpon instanceof Cell) {
            Cell cell = (Cell) entityForceUpon;
            if (distance <= (cell.getSenseRange())) {
                double mag = NaturalLaws.FORCE_BASE_MULTIPLIER / (distance * distance);
                double angleTowards = getAngleBetween(cell, entityForceFrom);
                if (canEat(entityForceFrom, cell)) {
                    return new Force(mag * cell.getIncentiveDeath(), angleTowards);
                } else if (canEat(entityForceUpon, entityForceFrom)) {
                    return new Force(mag * cell.getIncentiveMass() * entityForceFrom.getMass(), angleTowards);
                } else if (distance <= cell.getRadius()) {
                    return new Force(mag * NaturalLaws.FORCE_COLLISION_GRAVITY, angleTowards);
                }
            }
        }
        return null;

    }

    private void eatTouchingCellsAndFood(Cell cell) {
        for (PhysicalEntity other : entities) {
            if (getDistanceBetween(cell, other) <= cell.getRadius()) {
                if (canEat(cell, other)) {
                    if (!cell.isInvincible()) {
                        cell.addMass(other.getMass());
                        other.kill();
                    }
                }
            }
        }
    }

    private Food newRandomFood() {
        double x = newFoodXMin + (Math.random() * newFoodXRange);
        double y = newFoodYMin + (Math.random() * newFoodYRange);
        double mass = newFoodMassMin + (Math.random() * newFoodMassRange) ;
        return new Food(x, y, mass);
    }

    public void tick() {
        ArrayList<PhysicalEntity> born = new ArrayList<>();
        ArrayList<PhysicalEntity> dead = new ArrayList<>();

        for (PhysicalEntity entity : entities) {
            if (entity instanceof Cell) {
                Cell cell = (Cell) entity;
                cell.move();
                cell.impressForces(calculateForces(cell));
                eatTouchingCellsAndFood(cell);
                cell.removeMass(NaturalLaws.CELL_DECAY_PER_TICK);
                currentSystemMass -= NaturalLaws.CELL_DECAY_PER_TICK;
                cell.buffTick();
                while (cell.isReadyToDivide()) {
                    born.add(cell.divideSelf());
                }
            }

            if (entity.isDead()) {
                dead.add(entity);
            }
        }

        entities.addAll(born);
        entities.removeAll(dead);

        while (currentSystemMass < totalSystemMass) {
            Food newFood = newRandomFood();
            entities.add(newFood);
            currentSystemMass += newFood.getMass();
        }


    }

}
