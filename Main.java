import javax.swing.*;

/**
 * Created by troymartin on 3/1/17.
 */
public class Main {

    public static void main(String[] args) {
        AgarWorld world = new AgarWorld(1, 1000.0);
        AgarFrame frame = new AgarFrame("DEMO", world, 900);
        frame.setVisible(true);
        AgarCanvas canvas = frame.getCanvas();
        canvas.run();
    }
}
