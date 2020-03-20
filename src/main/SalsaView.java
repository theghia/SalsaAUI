package main;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * SalsaView abstract class that extends JPanel to be derived by subclasses representing various views
 * in this MVC application
 *
 * @author Gareth Iguasnia
 * @date 21/01/2020
 */
public abstract class SalsaView extends JPanel{

    // To identify what view this is
    private String viewName;

    // Every view must be labelled so that the user knows where in the application they are at
    private JLabel viewLabel;

    // To be used to organise the GUI components dynamically
    private SpringLayout panelLayout;

    // The size of every view
    private Dimension dimension;

    // Each view should be able to navigate through the MVC application
    private Map<String, JButton> navigationButtons = new HashMap<>();

    // Every view except the MenuView needs to have a home button
    private boolean isMenuView;
    private String MAIN = "main";

    // File path
    private final String GRAPHICS = "src/assets/graphics/";

    // Home Button dimensions
    private final int WIDTH_HOME = 100;
    private final int HEIGHT_HOME = 100;

    // Dimensions of desired JPanel
    private final int WIDTH_PANEL = 788;
    private final int HEIGHT_PANEL = 765;

    /**
     * Constructor for the abstract class SalsaView
     * This will only be used when the subclass calls the super() method.
     *
     * @param viewName String variable representing the name of the view
     * @param dimension Dimension object representing the size needed for the JPanel to fit the JFrame
     * @param isMenuView Boolean variable indicating whether the view is the main menu screen (True for yes)
     */
    public SalsaView(String viewName, Dimension dimension, boolean isMenuView) {
        this.viewName = viewName;
        this.dimension = dimension;
        this.isMenuView = isMenuView;

        // If isMenuView is not true, include a "Home" button
        setupLayout();
        setupHomeButton();
    }

    /**
     * Method returns the name of the view
     *
     * @return A String object representing the name that the view has
     */
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Method to get the label that will be used for the view so that the user knows what screen they are currently in
     *
     * @return A JLabel object that represents what will be used to label the view
     */
    public JLabel getViewLabel() {
        return viewLabel;
    }

    /**
     * Method to instantiate the viewLabel with the desired words
     *
     * @param viewLabel A String that will be used as the label for the screen.
     */
    public void setViewLabel(String viewLabel) {
        this.viewLabel = new JLabel(viewLabel);
    }

    /**
     * Method to get the panelLayout field
     *
     * @return The SpringLayout object of that will be used for the JPanel
     */
    public SpringLayout getPanelLayout() {
        return panelLayout;
    }

    /**
     * Method to return the HashMap that will hold the JButtons dealing with navigation around the MVC
     *
     * @return HashMap where the key is a String representing the name of the JButton and the value
     * is the corresponding JButton
     */
    public Map<String, JButton> getNavigationButtons() { return navigationButtons; }

    /**
     * Method to return the final field GRAPHICS
     *
     * @return A String object representing the file path containing the images needed for the view
     */
    public String getGRAPHICS() { return GRAPHICS; }

    /**
     * Method returns the field dimension
     *
     * @return A Dimension object that represents the size of the JPanel
     */
    public Dimension getDimension() { return dimension; }

    /**
     * Method returns the field MAIN
     *
     * @return A string that represents the home button
     */
    public String getMAIN() {
        return MAIN;
    }

    /**
     * Method returns the width of the desired JPanel
     *
     * @return An integer representing the width of the desired JPanel
     */
    public int getWidthBuffer() {
        int buffer = 0;

        // The condition to get a buffer
        if (this.dimension.getWidth() > WIDTH_PANEL) {
            buffer = (int) (this.dimension.getWidth() - WIDTH_PANEL)/2;
        }
        return buffer;
    }

    /**
     * Method returns the height buffer between the current height of the JPanel and the height of the desired JPanel
     *
     * @return An integer representing the height buffer
     */
    public int getHeightBuffer() {
        int buffer = 0;

        // The condition to get a buffer
        if (this.dimension.getHeight() > HEIGHT_PANEL) {
            buffer = (int) (this.dimension.getHeight() - HEIGHT_PANEL)/2;
        }
        return buffer;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800,800);
    }

    /* Helper method: Sets up the layout of the JPanel */
    private void setupLayout() {
        this.panelLayout = new SpringLayout();
        this.setLayout(panelLayout);

        // Size of every panel must be the same
        this.setPreferredSize(dimension);
    }

    /* Helper method: Sets up a "Home" button if the view is not the MenuView */
    // SHOULD THIS BE DONE IN THE THREAD THING FOR THREAD SAFETY?
    private void setupHomeButton() {
        if (!this.isMenuView) {
            // Scaling the home png file
            ImageIcon home = new ImageIcon(GRAPHICS + "home.png");
            Image scaled = home.getImage().getScaledInstance(WIDTH_HOME - 40,HEIGHT_HOME - 40, Image.SCALE_SMOOTH);
            ImageIcon scaledHome = new ImageIcon(scaled);

            // Creating a button with the scaled home png file
            JButton main = new JButton(scaledHome);
            main.setPreferredSize(new Dimension(WIDTH_HOME,HEIGHT_HOME));

            // Setting the Home button
            panelLayout.putConstraint(SpringLayout.EAST, main, -25, SpringLayout.EAST, this);
            panelLayout.putConstraint(SpringLayout.SOUTH, main, -25, SpringLayout.SOUTH, this);

            // To make it clickable
            main.setEnabled(true);

            // Adding the button to the view and JPanel
            this.navigationButtons.put(MAIN, main);
            this.add(main); main.setBackground(null);
        }
    }
}
