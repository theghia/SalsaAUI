package main;

import javax.swing.*;
import java.util.ArrayList;
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

    // The size of every view
    private final int width;
    private final int height;

    // Each view should be able to navigate through the MVC application
    private Map<String, JButton> navigationButtons = new HashMap<String, JButton>();

    // Every view except the MenuView needs to have a home button
    private boolean isMenuView;
    private String MAIN = "main";

    /**
     * Constructor for abstract class SalsaView.
     * This will only be used when the subclass calls the super() method.
     *
     * @param viewName String variable representing the name of the view
     */
    public SalsaView(String viewName, int width, int height, boolean isMenuView) {
        this.viewName = viewName;
        this.width = width;
        this.height = height;

        // If bool is true then setup button
        this.isMenuView = isMenuView;
        setupHomeButton();

        // Setting up the JPanel
        setupLayout();
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
     * Method returns the width of the view
     *
     * @return An integer value representing the width of the JPanel
     */
    public int getWIDTH() {
        return this.width;
    }

    /**
     * Method returns the height of the view
     *
     * @return An integer value representing the height of the JPanel
     */
    public int getHEIGHT() {
        return this.height;
    }

    /**
     * Method to return the HashMap that will hold the JButtons dealing with navigation around the MVC
     *
     * @return HashMap where the key is a String representing the name of the JButton and the value
     * is the corresponding JButton
     */
    public Map<String, JButton> getNavigationButtons() { return navigationButtons; }

    /* Helper method: Sets up the layout of the JPanel */
    private void setupLayout() {
        // Needed so that we can specify the position and size of the JButtons
        this.setLayout(null);
        // Size of every view must be the same
        this.setSize(this.width, this.height);
    }

    /* Helper method: Sets up a "Home" button if the view is not the MenuView */
    private void setupHomeButton() {
        if (!this.isMenuView) {
            JButton main = new JButton("Main");
            main.setBounds(685, 655, 100, 100);
            main.setEnabled(true);
            this.navigationButtons.put(MAIN, main);
            this.add(main); main.setBackground(null);
        }
    }
}
