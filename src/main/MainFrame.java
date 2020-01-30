package main;

import views.JustifiedUserProfileView;
import views.MenuView;
import views.SimulationView;
import views.TutorialView;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MainFrame class that extends JFrame for use the MVC GUI
 *
 * @author Gareth Iguasnia
 * @date 22/01/2020
 */
public class MainFrame extends JFrame {

    // Used to move through views
    private Map<String, SalsaView> panels = new HashMap<String, SalsaView>(); //Use the index number? panels.keySet()

    // Card layout structure
    private JPanel cards = new JPanel(new CardLayout());

    // Frame size details
    private final int WIDTH = 800;
    private final int HEIGHT = 800;

    // File paths
    private final String BACKGROUND = "src/assets/graphics/salsa_background.jpg"; //Resize object

    // Name of the views - for the navigation controller too?
    private final String MAIN = "main";
    private final String SIMULATION = "simulation";
    private final String TUTORIAL = "tutorial";
    private final String JUP = "justified_user_profile";


    /**
     * Constructor for the MainFrame class. The views in the MVC application will
     * all be instantiated in this class and CardLayout will be adopted to allow the
     * controllers to move through the different views according to the user's navigation
     * of the application
     */
    public MainFrame() {
        //SHOULD THIS BE DONE IN A THREAD SAFETY THING
        // This could be one method - Set up frame
        setupFrame();

        // Method to instantiate the views and add to cards and panels
        setupMultipleViews();

        // Adding Frame behaviour
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        //System.out.println(getContentPane().getHeight());
        //System.out.println(panels.get(MAIN).getSize());
        //System.out.println(panels.get(MAIN).getHeight()/2);
        //System.out.println((int) getContentPane().getSize().getHeight()/2);
    }

    /**
     * Method to get the panels field which is a Map with the key value as a String value
     * representing the name of the view and the respective SalsaView object
     *
     * @return A Map object where the key is the name of the view and the value is the respective
     * SalsaView object
     */
    public Map<String, SalsaView> getPanels() {
        return this.panels;
    }

    /**
     * Method gets the cards field that is a JPanel using the card layout function
     *
     * @return The JPanel cards field
     */
    public JPanel getCards() {
        return this.cards;
    }

    /**
     * Method returns the field MAIN
     *
     * @return String object that is held by the field MAIN
     */
    public String getMAIN() { return MAIN; }

    /**
     * Method returns the field SIMULATION
     *
     * @return String object that is held by the field SIMULATION
     */
    public String getSIMULATION() { return SIMULATION; }

    /**
     * Method returns the field TUTORIAL
     *
     * @return String object that is held by the field TUTORIAL
     */
    public String getTUTORIAL() { return TUTORIAL; }

    /**
     * Method that returns the field JUP
     *
     * @return String object that is held by the field JUP
     */
    public String getJUP() { return JUP; }

    /* Helper Method to initialise the views */
    private void setupMultipleViews() {
        //SalsaView menu = new MenuView(MAIN, WIDTH, HEIGHT);
        SalsaView menu = new MenuView(MAIN, this.getContentPane().getSize());
        setupOneView(menu);

        //SalsaView simulation = new SimulationView(SIMULATION, WIDTH, HEIGHT);
        SalsaView simulation = new SimulationView(SIMULATION, this.getContentPane().getSize());
        setupOneView(simulation);

        //SalsaView tutorial = new TutorialView(TUTORIAL, WIDTH, HEIGHT);
        SalsaView tutorial = new TutorialView(TUTORIAL, this.getContentPane().getSize());
        setupOneView(tutorial);

        //SalsaView justifiedUserProfile = new JustifiedUserProfileView(JUP, WIDTH, HEIGHT);
        SalsaView justifiedUserProfile = new JustifiedUserProfileView(JUP, this.getContentPane().getSize());
        setupOneView(justifiedUserProfile);

        // Adding the cards to the frame
        this.getContentPane().add(cards);
    }

    /* Helper Method to initialise one view */
    private void setupOneView(SalsaView salsaView) {
        this.panels.put(salsaView.getViewName(), salsaView);
        this.cards.add(salsaView, salsaView.getViewName());
    }

    /* Helper Method to setup the frame for the MVC application */
    private void setupFrame() {
        this.getContentPane().setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(WIDTH,HEIGHT);

        // Centering the JFrame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2 - this.getSize().height/2);
    }

}
