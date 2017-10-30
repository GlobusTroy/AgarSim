import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by troymartin on 3/1/17.
 */
public class Main {

    private static final double CELL_START_MASS = 50;
    //private static final Color CELL_START_COLOR = Color.blue;
    private static final double CELL_START_SPLIT_POINT = 250.0;
    private static final int CELL_START_NUM_CHILDREN = 9;
    private static final double CELL_START_CHILD_MASS_RATIO = 0.1;
    private static final double CELL_START_SENSE_RANGE = 1000.0;

    public static void main(String[] args) {

        NaturalLaws naturalLaws = new NaturalLaws(NaturalLaws.DEFAULT_CELL_MASS_CONSUME_RATIO, NaturalLaws.DEFAULT_CELL_DECAY_PER_TICK,
        NaturalLaws.DEFAULT_CELL_SPEED_BASE, NaturalLaws.DEFAULT_CELL_FAMINE_POINT,
        NaturalLaws.DEFAULT_FORCE_BASE_MULTIPLIER * 5,  NaturalLaws.DEFAULT_FORCE_COLLISION);

        Cell initBlue = new Cell(Math.random()*1200, Math.random()*700, CELL_START_MASS, CELL_START_SPLIT_POINT,
                CELL_START_NUM_CHILDREN, CELL_START_CHILD_MASS_RATIO, Color.blue, CELL_START_SENSE_RANGE);

        Cell initRed = new Cell(Math.random()*1200, Math.random()*700, CELL_START_MASS, CELL_START_SPLIT_POINT,
                CELL_START_NUM_CHILDREN, CELL_START_CHILD_MASS_RATIO, Color.red, CELL_START_SENSE_RANGE);

        Cell initGreen = new Cell(Math.random()*1200, Math.random()*700, CELL_START_MASS, CELL_START_SPLIT_POINT,
                CELL_START_NUM_CHILDREN, CELL_START_CHILD_MASS_RATIO, Color.green, CELL_START_SENSE_RANGE);

        List<Cell> initialCells = new ArrayList<>();
        initialCells.add(initBlue);
        initialCells.add(initRed);
        initialCells.add(initGreen);

        AgarWorld world = new AgarWorld(initialCells, 1200, 700,1500.0, naturalLaws);
        AgarFrame frame = new AgarFrame("AGAR SIM", world, 1440, 900);
        frame.setVisible(true);
        AgarCanvas canvas = frame.getCanvas();
        canvas.run();
    }
}
