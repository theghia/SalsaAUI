package salsa.view;

import salsa.controller.SalsaAppController;

import javax.swing.*;
import java.awt.*;

/**
 * SalsaFrame object that extends JFrame for use with a MVC GUI
 * @author Gareth Iguasnia
 * @date 17/01/2020
 */
public class SalsaFrame extends JFrame {

    // Reference to the application SalsaPanel
    private SalsaPanel basePanel;

    /**
     * Creates a SalsaFrame object passing a reference to the SalsaAppController for use by the SalsaFrame object
     *
     * @param baseController The reference to the SalsaAppController  object for MVC
     */
    public SalsaFrame(SalsaAppController baseController) {
        basePanel = new SalsaPanel(baseController);
        setUpFrame();
    }

    /**
     * Sets the content pane, size, and makes the frame visible.
     */
    private void setUpFrame() {
        this.setContentPane(basePanel);
        this.setSize(800, 800);

        // Centering the JFrame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2 - this.getSize().height/2);

        // Adding the background to the application
        //JLabel jl = new JLabel();
        //jl.setIcon(new ImageIcon("C://dissertation//SalsaAUI//src//assets//graphics//salsa_background.jpeg"));
        //this.add(jl);
        //this.add(new JLabel(new ImageIcon("C://dissertation//SalsaAUI//src//assets//graphics//salsa_background.jpeg")));

        this.setVisible(true);
    }

}
