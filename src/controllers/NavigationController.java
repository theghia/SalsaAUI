package controllers;

import components.UserProfile;
import main.MainFrame;
import main.SalsaController;
import main.SalsaModel;
import views.GameLevelView;
import views.games.EasySimulationView;
import views.games.HardSimulationView;
import views.JustifiedUserProfileView;
import views.MenuView;
import views.TutorialView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
        initLevelNavigationButtonActionListeners();
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

    /* Helper method to initialise the navigation buttons in the Simulation Views */
    private void initSimulationNavigationButtonActionListeners() {
        HardSimulationView hardSimulationView =
                (HardSimulationView) this.mainFrame.getPanels().get(mainFrame.getHARD());
        goToView(hardSimulationView.getNavigationButtons().get(mainFrame.getMAIN()), mainFrame.getMAIN());

        EasySimulationView easySimulationView =
                (EasySimulationView) this.mainFrame.getPanels().get(mainFrame.getEASY());
        goToView(easySimulationView.getNavigationButtons().get(mainFrame.getMAIN()), mainFrame.getMAIN());
    }

    /* Helper method to initialise the navigation buttons in the MenuView */
    private void initMenuNavigationButtonActionListeners() {
        MenuView main = (MenuView) this.mainFrame.getPanels().get(mainFrame.getMAIN()); // Getting the view main

        // Initialises the ActionListener for the "simulation" button
        beforePlayingTheGame(main.getNavigationButtons().get(mainFrame.getSIMULATION()), mainFrame.getLEVELS());

        // Initialises the ActionListener for the "tutorial" button
        beforePlayingTheGame(main.getNavigationButtons().get(mainFrame.getTUTORIAL()), mainFrame.getTUTORIAL());

        // Initialises the ActionListener for the "justified_user_profile" button
        buttonUnderConstruction(main.getNavigationButtons().get(mainFrame.getJUP()), mainFrame.getJUP());
        //goToView(main.getNavigationButtons().get(mainFrame.getJUP()), mainFrame.getJUP());

        // Caching the result of the current view
        this.getSalsaModel().setCurrentView(mainFrame.getMAIN());
    }

    private void initLevelNavigationButtonActionListeners() {
        GameLevelView gameLevelView = (GameLevelView) this.mainFrame.getPanels().get(mainFrame.getLEVELS());

        // Initialises the ActionListener for the "easy" button
        //buttonUnderConstruction(gameLevelView.getNavigationButtons().get("easy"), mainFrame.getLEVELS());
        goToView(gameLevelView.getNavigationButtons().get("easy"), mainFrame.getEASY());

        // Initialises the ActionListener for the "medium" button
        buttonUnderConstruction(gameLevelView.getNavigationButtons().get("medium"), mainFrame.getLEVELS());

        // Initialises the ActionListener for the "hard" button
        goToView(gameLevelView.getNavigationButtons().get(mainFrame.getHARD()), mainFrame.getHARD());

        // Initialise the ActionListener for the "home" button
        goToView(gameLevelView.getNavigationButtons().get(mainFrame.getMAIN()), mainFrame.getMAIN());
    }

    /* Helper method to add an ActionListener to the "Home" buttons */
    private void goToView(JButton navigationButton, String panel) {
        ActionListener goToView = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) mainFrame.getCards().getLayout();
                // Displaying the home page
                cardLayout.show(mainFrame.getCards(), panel);
                // Caching the result of the current view
                getSalsaModel().setCurrentView(panel);
            }
        };
        navigationButton.addActionListener(goToView);
    }

    private void buttonUnderConstruction(JButton navigationButton, String panel) {
        ActionListener underConstruction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel pane = mainFrame.getPanels().get(panel);
                JOptionPane.showMessageDialog(pane, "This is under construction");
            }
        };
        navigationButton.addActionListener(underConstruction);
    }

    private void beforePlayingTheGame(JButton navigationButton, String panel) {
        ActionListener beforePlaying = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // The user must register a name before playing
                if (getSalsaModel().getNameOfUser() == null) {
                    JPanel pane = mainFrame.getPanels().get(panel);
                    String username = JOptionPane.showInputDialog(pane,
                            "Please input your username:",
                            "Username needed",
                            JOptionPane.WARNING_MESSAGE);
                    getSalsaModel().setNameOfUser(username);
                    if (!(getSalsaModel().getNameOfUser() == null))
                        JOptionPane.showMessageDialog(pane,
                                "Username successfully added!",
                                "Alert",
                                JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    // Load the information
                    loadGameProgress();

                    CardLayout cardLayout = (CardLayout) mainFrame.getCards().getLayout();
                    // Displaying the game difficulty page
                    cardLayout.show(mainFrame.getCards(), panel);
                    // Caching the result of the current view
                    getSalsaModel().setCurrentView(panel);
                }
            }
        };
        navigationButton.addActionListener(beforePlaying);
    }

    public void loadGameProgress() {

        String filename = "";

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            // Getting the path of the .ser file in the same directory as the JAR file
            String jarPathFile = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            final File jarFile = new File(jarPathFile);

            // To be able to load the data for when the JAR file is being executed
            if (jarFile.isFile()) {
                filename = jarPathFile.substring(0, jarPathFile.length() - 12) +
                        getSalsaModel().getNameOfUser() + ".ser";
                // This stream can load data from within the JAR file
                //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
            }

            // Loading the data when running the application in an IDE - This is only for testing purposes
            else {
                filename = getSalsaModel().getDATA() +
                        getSalsaModel().getNameOfUser() + ".ser";
            }
            fileInputStream = new FileInputStream(filename);
            objectInputStream = new ObjectInputStream(fileInputStream);

            UserProfile userProfile = (UserProfile) objectInputStream.readObject();
            getSalsaModel().setUserProfile(userProfile);
            System.out.println("File has been loaded");
        }
        catch (IOException e) {
            System.out.println("There is no file to load");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
