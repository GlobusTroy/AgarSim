import java.awt.*;
import java.util.ArrayList;

/**
 * Created by troymartin on 3/1/17.
 */
public class AgarWorld {

    private final double FORCE_BASE = 1000.0;
    private final double DEATH_WEIGHT = 100.0;
    private final double PUSH_FLAT_WEIGHT = 100.0;
    private final double MASS_WEIGHT = 10.0;

    private final double CELL_MASS_CONSUME_RATIO = 1.25;
    private final double DECAY_PER_TICK = 0.25;

    private final double NEW_FOOD_MASS_MIN = 0.0;
    private final double NEW_FOOD_MASS_RANGE = 10.0;
    private final double NEW_FOOD_X_MIN = 100.0;
    private final double NEW_FOOD_X_RANGE = 700.0;
    private final double NEW_FOOD_Y_MIN = 100.0;
    private final double NEW_FOOD_Y_RANGE = 700.0;

    private double totalSystemMass;
    private double currentSystemMass;
    private ArrayList<PhysicalEntity> entities;

    public AgarWorld(int cellCount, double systemMass) {

        entities = new ArrayList<>();
        for (int i = 0; i < cellCount; i++) {
            entities.add(new Cell(Math.random() * 800, Math.random() * 800, 50.0, 100.0, Color.blue, 500.0));
            currentSystemMass += 50;
            entities.add(new Food(200 + Math.random() * 500, 200 + Math.random() * 500, 10));
            currentSystemMass += 10;
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
            else return (eater.getMass() >= CELL_MASS_CONSUME_RATIO * meal.getMass());
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
        double homeX = (2 * NEW_FOOD_X_MIN + NEW_FOOD_X_RANGE) / 2;
        double homeY = (2 * NEW_FOOD_Y_MIN + NEW_FOOD_Y_RANGE) / 2;
        double ang = getAngleBetween(cell.getX(), cell.getY(), homeX, homeY);
        return new Force(FORCE_BASE, ang);
    }

    private Force getForceBetween(PhysicalEntity entityForceUpon, PhysicalEntity entityForceFrom) {
        double distance = getDistanceBetween(entityForceUpon, entityForceFrom);
        if (entityForceUpon instanceof Cell) {
            Cell cell = (Cell) entityForceUpon;
            if (distance <= (cell.getSenseRange())) {
                double mag = FORCE_BASE / (distance * distance);
                double ang = getAngleBetween(cell, entityForceFrom);
                if (canEat(entityForceFrom, cell)) {
                    return new Force(mag * cell.getIncentiveDeath(), ang);
                } else if (canEat(entityForceUpon, entityForceFrom)) {
                    return new Force(mag * cell.getIncentiveMass() * entityForceFrom.getMass(), ang);
                } else if (distance <= entityForceUpon.getRadius()) {
                    return new Force(-mag * PUSH_FLAT_WEIGHT, ang);
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
        double x = NEW_FOOD_X_MIN + (Math.random() * NEW_FOOD_X_RANGE);
        double y = NEW_FOOD_Y_MIN + (Math.random() * NEW_FOOD_Y_RANGE);
        double mass = NEW_FOOD_MASS_MIN + (Math.random() * NEW_FOOD_MASS_RANGE) ;
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
                cell.removeMass(DECAY_PER_TICK);
                currentSystemMass -= DECAY_PER_TICK;
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
