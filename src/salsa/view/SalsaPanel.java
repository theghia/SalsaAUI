package salsa.view;

import salsa.controller.SalsaAppController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * SalsaPanel object that extends JPanel for use with a MVC GUI
 * @author Gareth Iguasnia
 * @date 17/01/2020
 */
public class SalsaPanel extends JPanel {

    // Reference to the application SalsaAppController
    private SalsaAppController baseController;

    // Testing
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton newScreen;
    private SpringLayout baseLayout;

    /**
     * Creates a SalsaPanel object passing a reference to the SalsaAppController for use by the SalsaPanel object
     *
     * @param baseController In this scenario, the controller is used by the view...
     */
    public SalsaPanel(SalsaAppController baseController) {
        this.baseController = baseController;

        nameLabel = new JLabel("I am just here for fun");

        nameField = new JTextField(25);

        newScreen = new JButton("Testing");

        baseLayout = new SpringLayout();
        //baseLayout.putConstraint(SpringLayout.SOUTH, nameLabel, -19, SpringLayout.NORTH, newScreen);
        //baseLayout.putConstraint(SpringLayout.EAST, nameLabel, -266, SpringLayout.EAST, this);
        //baseLayout.putConstraint(SpringLayout.NORTH, newScreen, 495, SpringLayout.NORTH, this);
        //baseLayout.putConstraint(SpringLayout.EAST, newScreen, -337, SpringLayout.EAST, this);

        setUpPanel();
        setUpLayout();
        setUpListeners();

        // Needed so that we can specify the position and size of the JButtons
        //this.setLayout(null);
        //this.setSize(this.width, this.height);
    }

    private void setUpLayout() {
    	
    	// USE STATIC FINAL values for the border so that you can dynamically change the sizes of the boxes

    	// Placing the text box in the horizontal middle and it is of size 200
    	baseLayout.putConstraint(SpringLayout.WEST, nameField, 300, SpringLayout.WEST, this);
    	baseLayout.putConstraint(SpringLayout.EAST, nameField, 500, SpringLayout.WEST, this);
    	
    	// Placing the text box right in the verticle middle and it is of size 50
    	baseLayout.putConstraint(SpringLayout.NORTH, nameField, 375, SpringLayout.NORTH, this);
    	baseLayout.putConstraint(SpringLayout.SOUTH, nameField, 425, SpringLayout.NORTH, this);
    	
    	// Placing the label below the text box
    	baseLayout.putConstraint(SpringLayout.NORTH, nameLabel, 20, SpringLayout.SOUTH, nameField);
    	
    	// Horizontally centering the label
    	baseLayout.putConstraint(SpringLayout.WEST, nameLabel, 280, SpringLayout.WEST, this);
    	baseLayout.putConstraint(SpringLayout.EAST, nameLabel, 520, SpringLayout.WEST, this);
    	
    	// Placing the button below the label
    	baseLayout.putConstraint(SpringLayout.NORTH, newScreen, 25, SpringLayout.SOUTH, nameLabel);
    	
    	// Centering the button horizontally
    	baseLayout.putConstraint(SpringLayout.WEST, newScreen, 320 , SpringLayout.WEST, this);
    	baseLayout.putConstraint(SpringLayout.EAST, newScreen, 480, SpringLayout.WEST, this);
    }

    private void setUpPanel() {

        // Lay out manager
        this.setSize(800,800);
        this.setLayout(baseLayout);
        this.add(newScreen);
        this.add(nameField);
        this.add(nameLabel);
    }

    private void setUpListeners() {
    	
    	newScreen.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent click) {
    			System.out.println("Woah");
    			
    		}
    	});

    }
}
