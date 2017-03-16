import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by troymartin on 2/28/17.
 */
public class AgarFrame extends JFrame {

    Canvas canvas;

    AgarFrame(String title, AgarWorld world, int pixelSizeX, int pixelSizeY) {
        setTitle(title);

        canvas = new AgarCanvas(world, pixelSizeX, pixelSizeY);
        add(canvas);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });
        pack();
    }

    AgarFrame(String title, AgarWorld world, int pixelSize) {
        this(title, world, pixelSize, pixelSize);
    }

    AgarCanvas getCanvas() {
        return (AgarCanvas) canvas;
    }

}
