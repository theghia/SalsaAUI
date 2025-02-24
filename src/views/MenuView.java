package views;

import main.SalsaView;
import javax.swing.*;
import java.awt.*;

/**
 * MenuView class that extends the SalsaView class. This class represents the main menu screen of this MVC
 * application
 *
 * @author Gareth Iguasnia
 * @date 30/01/2020
 */
public class MenuView extends SalsaView {

    private final String SIMULATION = "simulation";
    private final String TUTORIAL = "tutorial";
    private final String JUSTIFIED_USER_MODEL = "justified_user_profile";

    private JButton simulation;
    private JButton tutorial;
    private JButton justified_user_model;

    // Dimensions for buttons and spacing
    int BUTTON_HEIGHT = 100;
    int BUTTON_WIDTH = 400;
    int SPACES_BETWEEN_BUTTONS = 50;

    /**
     * Constructor for the MenuView Class. This object will hold details on the view of the main menu
     * of the MVC application. This will be the first panel displayed when the MVC application is run.
     *
     * @param name A String object representing the name of the view
     * @param dimension A Dimension object representing the size of this JPanel
     */
    public MenuView(String name, Dimension dimension) {
        super(name, dimension, true);
        setupButtons();
        layoutButtons();
    }

    /* Helper method to start up the buttons on the screen */
    private void setupButtons() {
        // Button to move to the simulation view
        this.simulation = new JButton("Simulation");
        this.simulation.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.simulation.setEnabled(true);
        this.getNavigationButtons().put(SIMULATION, simulation);
        this.add(simulation); simulation.setBackground(null);

        // Button to move to the tutorial view
        this.tutorial = new JButton("Tutorial");
        this.tutorial.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.tutorial.setEnabled(true);
        this.getNavigationButtons().put(TUTORIAL, tutorial);
        this.add(tutorial); tutorial.setBackground(null);

        // Button to move to the justified_user_model view
        this.justified_user_model = new JButton("Track Record");
        this.justified_user_model.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.justified_user_model.setEnabled(true);
        this.getNavigationButtons().put(JUSTIFIED_USER_MODEL, justified_user_model);
        this.add(justified_user_model); justified_user_model.setBackground(null);
    }

    /* Helper method to layout the buttons */
    private void layoutButtons() {
        // Getting dimensions ready
        int panelHeight = (int) getDimension().getHeight();
        int panelWidth = (int) getDimension().getWidth();
        int numberOfButtons = getNavigationButtons().size();

        int totalHeightButtons = (SPACES_BETWEEN_BUTTONS + BUTTON_HEIGHT) * (numberOfButtons - 1) + BUTTON_HEIGHT;

        // The following lays out the JButtons in a Menu type layout
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, simulation,
                (panelHeight - totalHeightButtons)/2, SpringLayout.NORTH, this);
        this.getPanelLayout().putConstraint(SpringLayout.WEST, simulation,
                (panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );

        this.getPanelLayout().putConstraint(SpringLayout.WEST, tutorial,
                (panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, tutorial,
                SPACES_BETWEEN_BUTTONS, SpringLayout.SOUTH, simulation);

        this.getPanelLayout().putConstraint(SpringLayout.WEST, justified_user_model,
                (panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, justified_user_model,
                SPACES_BETWEEN_BUTTONS, SpringLayout.SOUTH, tutorial);
    }

}
