package components.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * RotateImage Class that extends the JPanel Class. This JPanel will be used to hold the the needle png file for the
 * gauge png and has been set up to be used by the GaugeNeedle Class to rotate the needle.
 *
 * @author Gareth Iguasnia
 * @date 06/03/2020
 */
public class RotateImage extends JPanel {
    private BufferedImage master;
    private BufferedImage rotated;

    /**
     * Constructor for RotateImage where the file path being used points to the image that will have the ability to be
     * rotated by the MoveGaugeNeedle Class.
     *
     * @param filePath String representing the file path that the user can access the image to be rotated
     */
    public RotateImage(String filePath) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resourceNeedle = classLoader.getResource(filePath);
            //ImageIcon imgGauge = new ImageIcon(resourceGauge);
            master = ImageIO.read(resourceNeedle);
            //master = ImageIO.read(new File(filePath));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        // So that the needle can be placed on top of the Gauge
        this.setOpaque(false);
    }

    @Override
    public Dimension getPreferredSize() {
        return master == null
                ? new Dimension(200,200)
                : new Dimension(master.getWidth(), master.getHeight());
    }

    /**
     * Method takes into the account the rotated Graphics and adjusts the new x and y measurements of the rotated
     * image
     *
     * @param g Graphics object that has been updated.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rotated != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - rotated.getWidth()) / 2;
            int y = (getHeight() - rotated.getHeight()) / 2;
            g2d.drawImage(rotated, x , y, this);
            g2d.dispose();
        }
    }

    /**
     * Method returns the BufferedImage master
     *
     * @return A BufferedImage
     */
    public BufferedImage getMaster() {
        return master;
    }

    /**
     * Method sets a new rotated BufferedImage to the field rotated and then repaints the JPanel so that the rotation
     * can be seen on the screen.
     *
     * @param rotated A BufferedImage that has been rotated
     */
    public void setRotated(BufferedImage rotated) {
        this.rotated = rotated;
        repaint();
    }
}
