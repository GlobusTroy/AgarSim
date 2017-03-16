import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by troymartin on 2/28/17.
 */
public class AgarCanvas extends Canvas implements ActionListener {

    private final Color COLOR_BACKGROUND = Color.black;
    private AgarWorld world;
    private boolean readyToPaint;
    Timer t;

    public AgarCanvas(AgarWorld world, int pixelSizeX, int pixelSizeY) {
        this.world = world;
        setSize(pixelSizeX, pixelSizeY);
        setBackground(COLOR_BACKGROUND);
        readyToPaint = true;
        t = new Timer(51, this);
    }

    public AgarCanvas(AgarWorld world, int pixelSize) {
        this(world, pixelSize, pixelSize);
    }

    public void actionPerformed(ActionEvent e) {
        if (readyToPaint) {
            world.tick();
            paint(getGraphics());
        }
    }

    public void run() {
        if (!t.isRunning()) {
            t.start();
        }
    }

    public void paint(Graphics g) {
        readyToPaint = false;
        BufferedImage bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)bufferImage.getGraphics();
        clear(g2);
        for (PhysicalEntity entity : world.getEntities()) {
            entity.drawMe(g2);
        }
        g2.setColor(Color.white);
        long cellCount = world.getEntities().stream().filter(entity -> entity instanceof Cell).count();
        g2.drawString("Cells Alive: "+cellCount, 20 , 20);

        g2.drawString("System Mass: "+world.getCurrentSystemMass(), 20 , 40);

        g.drawImage(bufferImage, 0, 0, null);
        readyToPaint = true;

    }


    private void clear(Graphics g) {
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

}
