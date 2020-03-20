package views;

import main.SalsaView;

import javax.swing.*;
import java.awt.*;

public class GameLevelView extends SalsaView {

    private final String EASY = "easy";
    private final String MEDIUM = "medium";
    private final String HARD = "hard";

    private JButton easy;
    private JButton medium;
    private JButton hard;

    // Dimensions for buttons and spacing
    int BUTTON_HEIGHT = 100;
    int BUTTON_WIDTH = 400;
    int SPACES_BETWEEN_BUTTONS = 50;

    /**
     * Constructor for the abstract class SalsaView
     * This will only be used when the subclass calls the super() method.
     *
     * @param viewName   String variable representing the name of the view
     * @param dimension  Dimension object representing the size needed for the JPanel to fit the JFrame
     */
    public GameLevelView(String viewName, Dimension dimension) {
        super(viewName, dimension, false);
        setupButtons();
        layoutButtons();
    }

    /* Helper method to start up the buttons on the screen */
    private void setupButtons() {
        // Button to move to the simulation view
        this.easy = new JButton("Easy");
        this.easy.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.easy.setEnabled(true);
        this.getNavigationButtons().put(EASY, easy);
        this.add(easy); easy.setBackground(null);

        // Button to move to the tutorial view
        this.medium = new JButton("Medium");
        this.medium.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.medium.setEnabled(true);
        this.getNavigationButtons().put(MEDIUM, medium);
        this.add(medium); medium.setBackground(null);

        // Button to move to the justified_user_model view
        this.hard = new JButton("Hard");
        this.hard.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.hard.setEnabled(true);
        this.getNavigationButtons().put(HARD, hard);
        this.add(hard); hard.setBackground(null);
    }

    /* Helper method to layout the buttons */
    private void layoutButtons() {
        // Getting dimensions ready
        int panelHeight = (int) getDimension().getHeight();
        int panelWidth = (int) getDimension().getWidth();
        int numberOfButtons = getNavigationButtons().size();

        int totalHeightButtons = (SPACES_BETWEEN_BUTTONS + BUTTON_HEIGHT) * (numberOfButtons - 1) + BUTTON_HEIGHT;

        // Laying out the "easy" button
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, easy,
                (panelHeight - totalHeightButtons)/2, SpringLayout.NORTH, this);

        this.getPanelLayout().putConstraint(SpringLayout.WEST, easy,
                (panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );

        // Laying out the "medium" button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, medium,
                (panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, medium,
                SPACES_BETWEEN_BUTTONS, SpringLayout.SOUTH, easy);

        // Laying out the "hard" button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, hard,
                (panelWidth - BUTTON_WIDTH)/2, SpringLayout.WEST, this );
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, hard,
                SPACES_BETWEEN_BUTTONS, SpringLayout.SOUTH, medium);
    }
}
