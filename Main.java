import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by troymartin on 3/1/17.
 */
public class Main {

    private static final double CELL_START_MASS = 50;
    private static final Color CELL_START_COLOR = Color.blue;
    private static final double CELL_START_SPLIT_POINT = 100.0;
    private static final double CELL_START_SENSE_RANGE = 1000.0;

    public static void main(String[] args) {
        Cell initCell = new Cell(Math.random()*1200, Math.random()*700, CELL_START_MASS, CELL_START_SPLIT_POINT,
                CELL_START_COLOR, CELL_START_SENSE_RANGE);

        List<Cell> initialCells = new ArrayList<>();
        initialCells.add(initCell);

        AgarWorld world = new AgarWorld(initialCells, 1200, 700,1500.0);
        AgarFrame frame = new AgarFrame("AGAR SIM", world, 1440, 900);
        frame.setVisible(true);
        AgarCanvas canvas = frame.getCanvas();
        canvas.run();
    }
}
