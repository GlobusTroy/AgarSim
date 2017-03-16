import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by troymartin on 2/28/17.
 */
public class AgarCanvas extends Canvas implements ActionListener, KeyListener {

    private final Color COLOR_BACKGROUND = Color.black;
    private AgarWorld world;
    private boolean readyToPaint;
    Timer t;
    private int stepSkips = 0;

    public AgarCanvas(AgarWorld world, int pixelSizeX, int pixelSizeY) {
        this.world = world;
        setSize(pixelSizeX, pixelSizeY);
        setBackground(COLOR_BACKGROUND);
        readyToPaint = true;
        t = new Timer(50, this);
        setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_SPACE:
                if (t.isRunning()) {
                    t.stop();
                    repaint();
                } else {
                    t.start();
                }
                break;

            case KeyEvent.VK_DOWN:
                System.out.print("MINUS");
                if (stepSkips > 0) {
                    stepSkips--;
                } else {
                    t.setDelay(t.getDelay() + 5);
                }
                break;

            case KeyEvent.VK_UP:
                System.out.print("PLUS");
                if (t.getDelay() - 5 <= 1) {
                    t.setDelay(1);
                    stepSkips++;
                } else {
                    t.setDelay(t.getDelay() - 5);
                }
                break;
        }
    }

    public AgarCanvas(AgarWorld world, int pixelSize) {
        this(world, pixelSize, pixelSize);
    }

    public void actionPerformed(ActionEvent e) {
        if (readyToPaint) {
            for (int i = -1; i < stepSkips; i++) {
                world.tick();
            }
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

        drawLabels(g2);

        g.drawImage(bufferImage, 0, 0, null);
        readyToPaint = true;

    }

    private void drawLabels(Graphics2D g2) {
        g2.setColor(Color.white);
        long cellCount = world.getEntities().stream().filter(entity -> entity instanceof Cell).count();
        g2.drawString("Cells Alive: "+cellCount, 20 , 20);
        g2.drawString("System Mass: "+world.getCurrentSystemMass(), 20 , 40);
        g2.drawString("Game Speed: "+((1000.0/t.getDelay()) * (1+stepSkips)), 20 , 60);
        g2.drawString("Status: "+ (t.isRunning()? "Running" : "Paused"), 20 , 80);
    }


    private void clear(Graphics g) {
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

}
