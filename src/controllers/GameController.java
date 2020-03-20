package controllers;

import components.enums.State;
import events.ClipInformationEvent;
import listeners.ClipInformationListener;
import main.SalsaController;
import main.SalsaModel;
import views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class GameController extends SalsaController implements ClipInformationListener {
    // To have access to the buttons in the HardSimulationView Class
    private GameView gameView;

    // Random Generator to be used to get the next beat that the user will be tested on
    // and to find a starting state
    private Random randomGenerator;

    // The number of beats before and after the requested beat that the system will take in an input from the user
    private final int TIME_WINDOW = 3;

    // The next 4 beats that the system will request the user to identify
    private volatile ArrayList<Integer> nextBeats;

    // This executor will hold 6 threads needed to be executed when a new State object is traversed
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Constructor for the GameController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public GameController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName);
        this.gameView = gameView;
        //this.gameStatusFunction = new DynamicGameDifficultyBalancing();
        this.randomGenerator = new Random();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(6);

        initClickerButton();
        initStartButton();
    }

    /**
     * Abstract method to be used to calculate the error value and either store and display the error on the gauge, or
     * just display the error on the gauge
     *
     * @param currentBeat The beat that the user needs to find
     * @param barNumber   The bar number that the beat requested is on
     */
    public abstract void calculateErrorValue(int currentBeat, int barNumber);

    /**
     * Abstract method to be used to add an action listener to the Start button of the GameView
     */
    public abstract void initStartButton();

    /**
     * Abstract method to choose the state that the game will start on
     *
     * @return A State object that will be the first state that the game will begin on
     */
    public abstract State chooseStartingState();

    /**
     * Abstract method to execute the needed to progress the game. The reason this method is abstract is to allow the
     * TutorialController to also fire a method to connect to the TutorialGUIController to display the lights to
     * show the timing of the Salsa audio file
     *
     * @param clipSalsa Long value representing the length of the Salsa audio clip
     */
    public abstract void clipReady(long clipSalsa);

    /**
     * This method deals with the necessary logic when the fireClipInfoReadyEvent(...) method is called in the
     * HardSimulationMusicController. This method will fire events for every new 8-beat bar and when the Salsa audio clip
     * has finished as well
     *
     * @param e A ClipInformationEvent object that will pass information to GameProgress on the length of the clip of the
     *          Salsa audio clip.
     */
    @Override
    public void onClipInfoReadyEvent(ClipInformationEvent e) {
        // Adding a beat timeline so that the HardSimulationController can have the correct times that each beat occurs at
        long quarter = e.getClipSalsa()/4;
        getSalsaModel().setBeatTimeline(createBeatTimeline(quarter));

        // This method will be different for the TutorialController and the HardSimulationController as the Tutorial is
        // the only one the will display the lights
        clipReady(e.getClipSalsa());
    }

    /**
     * Method returns the nextBeats field
     *
     * @return An ArrayList of integers representing the next beats that system will request the user to find
     */
    public ArrayList<Integer> getNextBeats() {
        return nextBeats;
    }

    /**
     * Method returns the scheduledExecutorService field
     *
     * @return A ScheduledExecutorService object to be used to execute the thread dealing with displaying the lights
     * on the timing of the Salsa audio clip
     */
    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    /**
     * Method returns the randomGenerator to be used to generate random integers for the next beats that the system will
     * request the user to locate
     *
     * @return Random object associated to the GameController
     */
    public Random getRandomGenerator() {
        return randomGenerator;
    }

    /**
     * Returns the TIME_WINDOW variable. This can be modified to make the Game harder or easier
     *
     * @return An integer representing the time window
     */
    public int getTIME_WINDOW() {
        return TIME_WINDOW;
    }

    /**
     * Returns the GameView associated to the GameController. This will be used to add functionality to
     * the buttons on the GameView
     *
     * @return A GameView object associated to the GameController
     */
    public GameView getGameView() {
        return gameView;
    }

    /* Helper method that sets up the clicker button - calculateErrorValue() will be kept as abstract so that
    * additional functionality can be included i.e. whether we want to record the error value in the UserProfile or
    * not  */
    private void initClickerButton() {
        ActionListener click = new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                System.out.println("The time I perceive the beat to be: " + System.currentTimeMillis());
                System.out.println();

                // If a Salsa audio clip is currently playing, then proceed
                if (!getSalsaModel().isCountdownCurrentlyPlaying()) {
                    // Checking if has previously clicked in the assigned time windows
                    System.out.println("In InitButtonClicker");
                    System.out.println("Button Clicker b4 change: " + getSalsaModel().getButtonClickerTracker());
                    System.out.println("Window Tracker b4 change: " + getSalsaModel().getWindowTracker());
                    System.out.println("hasClickedOnce1 b4 change: " + getSalsaModel().hasClickedOnce1());
                    System.out.println("hasClickedOnce2 b4 change: " + getSalsaModel().hasClickedOnce2());

                    if (getSalsaModel().getButtonClickerTracker() == 1 && !getSalsaModel().hasClickedOnce1()) {
                        getSalsaModel().setHasClickedOnce1(true);
                        getSalsaModel().increaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: " + getSalsaModel().getButtonClickerTracker());
                        System.out.println("Window Tracker after change: " + getSalsaModel().getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + getSalsaModel().hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + getSalsaModel().hasClickedOnce2());

                        //Logic to determine the beat to the current time window
                        calculateErrorValue(getSalsaModel().getBeatCaching().get(0),
                                getSalsaModel().getBarCaching().get(0));
                    }

                    else if (getSalsaModel().getButtonClickerTracker() == 2 && !getSalsaModel().hasClickedOnce2()) {
                        getSalsaModel().setHasClickedOnce2(true);
                        getSalsaModel().decreaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: " + getSalsaModel().getButtonClickerTracker());
                        System.out.println("Window Tracker after change: " + getSalsaModel().getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + getSalsaModel().hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + getSalsaModel().hasClickedOnce2());

                        // Logic to determine the beat to the current time window
                        calculateErrorValue(getSalsaModel().getBeatCaching().get(1),
                                getSalsaModel().getBarCaching().get(1));
                    }
                }
            }
        };
        gameView.getBeatClicker().addActionListener(click);
    }

    /* Helper method to create a beat timeline for each beat in a group of 4 8-beat bars */
    private ArrayList<Long> createBeatTimeline(long quarter) {
        // Number of beats in a group of 4 8-beat bars
        int numBeatsPerState = 32;

        ArrayList<Long> beatTimeline = new ArrayList<>(numBeatsPerState);

        // Beat 1 of a new group of 4 8-beat bars is 0
        long beatTime = 0;

        // The time each beat is spread out with
        long anEighth = quarter/8;

        // Incrementally adding anEighth to the beatTime to get the beat timeline
        for (int i = 0; i < numBeatsPerState; i++) {
            beatTimeline.add(beatTime);
            beatTime += anEighth;
        }
        return beatTimeline;
    }
}
