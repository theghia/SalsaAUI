package controllers;

import main.MainFrame;
import main.SalsaController;
import main.SalsaModel;
import views.JustifiedUserProfileView;
import views.MenuView;
import views.SimulationView;
import views.TutorialView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * NavigationController Class that extends the SalsaController Class. This controller will be in charge of the logic
 * needed to navigate around the MVC application when the different navigation buttons are clicked
 *
 * @author Gareth Iguansnia
 * @date 27/01/2020
 */
public class NavigationController extends SalsaController {

    // A reference to the MainFrame class so that this controller can navigate through the different views
    private MainFrame mainFrame;

    /**
     * Constructor for the NavigationController class. This class will hold a reference to the MainFrame
     * class, in addition to the SalsaModel in order to switch through the different views
     * @param salsaModel A SalsaModel object that holds data of the MVC application.
     * @param controllerName A String object representing the name of the controller
     * @param mainFrame A MainFrame object that the controller will use to navigate around the MVC application
     */
    public NavigationController (SalsaModel salsaModel, String controllerName, MainFrame mainFrame) {
        super(salsaModel, controllerName);
        this.mainFrame = mainFrame;

        initMenuNavigationButtonActionListeners();
        initSimulationNavigationButtonActionListeners();
        initTutorialNavigationButtonActionListeners();
        initJUPNavigationButtonActionListeners();
    }

    /* Helper method to initialise the navigation buttons in the JustifiedUserProfileView */
    private void initJUPNavigationButtonActionListeners() {
        JustifiedUserProfileView jup = (JustifiedUserProfileView) this.mainFrame.getPanels().get(mainFrame.getJUP());

        goToView(jup.getNavigationButtons().get(mainFrame.getMAIN()), mainFrame.getMAIN());
    }

    /* Helper method to initialise the navigation buttons in the TutorialView */
    private void initTutorialNavigationButtonActionListeners() {
        TutorialView tutorial = (TutorialView) this.mainFrame.getPanels().get(mainFrame.getTUTORIAL());

        goToView(tutorial.getNavigationButtons().get(mainFrame.getMAIN()), mainFrame.getMAIN());
    }

    /* Helper method to initialise the navigation buttons in the SimulationView */
    private void initSimulationNavigationButtonActionListeners() {
        SimulationView simulation = (SimulationView) this.mainFrame.getPanels().get(mainFrame.getSIMULATION());

        goToView(simulation.getNavigationButtons().get(mainFrame.getMAIN()), mainFrame.getMAIN());
    }

    /* Helper method to initialise the navigation buttons in the MenuView */
    private void initMenuNavigationButtonActionListeners() {
        MenuView main = (MenuView) this.mainFrame.getPanels().get(mainFrame.getMAIN()); // Getting the view main

        // Initialises the ActionListener for the "simulation" button
        goToView(main.getNavigationButtons().get(mainFrame.getSIMULATION()), mainFrame.getSIMULATION());

        // Initialises the ActionListener for the "tutorial" button
        goToView(main.getNavigationButtons().get(mainFrame.getTUTORIAL()), mainFrame.getTUTORIAL());

        // Initialises the ActionListener for the "justified_user_profile" button
        goToView(main.getNavigationButtons().get(mainFrame.getJUP()), mainFrame.getJUP()); // NULL POINTER HERE
    }

    /* Helper method to add an ActionListener to the "Home" buttons */
    private void goToView(JButton navigationButton, String panel) {
        ActionListener gotoMain = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) mainFrame.getCards().getLayout();
                // Displaying the home page
                cardLayout.show(mainFrame.getCards(), panel);
            }
        };
        navigationButton.addActionListener(gotoMain);
    }
}
