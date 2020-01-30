package views;

import main.SalsaView;

import javax.swing.*;
import java.awt.*;

public class MenuView extends SalsaView {

    private String SIMULATION = "simulation";
    private String TUTORIAL = "tutorial";
    private String JUSTIFIED_USER_MODEL = "justified_user_profile";

    // Dimensions for buttons and spacing
    int BUTTON_HEIGHT = 100;
    int BUTTON_WIDTH = 400;
    int SPACES_BETWEEN_BUTTONS = 50;

    public MenuView(String name, Dimension dimension) {
        super(name, dimension, true);
        setupButtons();
    }

    // Helper method to start up the buttons on the screen
    private void setupButtons() {

        // Button to move to the simulation view
        JButton simulation = new JButton("Simulation");
        //btnWhereTo.setIcon(new ImageIcon(ASSETS + "salsa_background.jpg"));
        //btnWhereTo.setBorder(null);
        //btnWhereTo.setForeground(new Color(0,0,0,0));
        //btnWhereTo.setDisabledIcon(new ImageIcon(ASSETS + "salsa_background.jpg"));
        simulation.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        simulation.setEnabled(true);
        this.getNavigationButtons().put(SIMULATION, simulation);
        this.add(simulation); simulation.setBackground(null);

        // Button to move to the tutorial view
        JButton tutorial = new JButton("Tutorial");
        tutorial.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        tutorial.setEnabled(true);
        this.getNavigationButtons().put(TUTORIAL, tutorial);
        this.add(tutorial); tutorial.setBackground(null);

        // Button to move to the justified_user_model view
        JButton justified_user_model = new JButton("Track Record");
        justified_user_model.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        justified_user_model.setEnabled(true);
        this.getNavigationButtons().put(JUSTIFIED_USER_MODEL, justified_user_model);
        this.add(justified_user_model); justified_user_model.setBackground(null);

        // Getting dimensions ready
        int panelHeight = (int) getDimension().getHeight();
        int panelWidth = (int) getDimension().getWidth();
        int numberOfButtons = getNavigationButtons().size();
        int totalHeightButtons = (SPACES_BETWEEN_BUTTONS + BUTTON_HEIGHT) * (numberOfButtons - 1) + BUTTON_HEIGHT;

        // The following lays out the JButtons in a Menu type layout
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, simulation, (panelHeight - totalHeightButtons)/2, SpringLayout.NORTH, this);

        this.getPanelLayout().putConstraint(SpringLayout.WEST, simulation,(panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );
        this.getPanelLayout().putConstraint(SpringLayout.WEST, tutorial,(panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );
        this.getPanelLayout().putConstraint(SpringLayout.WEST, justified_user_model,(panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );

        this.getPanelLayout().putConstraint(SpringLayout.NORTH, tutorial, SPACES_BETWEEN_BUTTONS, SpringLayout.SOUTH, simulation);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, justified_user_model, SPACES_BETWEEN_BUTTONS, SpringLayout.SOUTH, tutorial);
    }

}
