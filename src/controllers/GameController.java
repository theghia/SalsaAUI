package controllers;

import components.*;
import events.ClipInformationEvent;
import events.ClipInformationListener;
import main.SalsaController;
import main.SalsaModel;
import timers.ClickTimeWindowFAKE;
import views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class GameController extends SalsaController implements ClipInformationListener {
    // To have access to the buttons in the SimulationView Class
    private GameView gameView;

    // As we are programming to an interface, we can switch out the GameStatusFunction with ease
    private GameStatusFunction gameStatusFunction;

    // Random Generator to be used to get the next beat that the user will be tested on
    // and to find a starting state
    private Random randomGenerator;

    // The number of beats before and after the requested beat that the system will take in an input from the user
    private final int TIME_WINDOW = 3;

    // The next 4 beats that the system will request the user to identify
    private ArrayList<Integer> nextBeats;

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
        this.gameStatusFunction = new DynamicGameDifficultyBalancing();
        this.randomGenerator = new Random();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(6);

        initClickerButton();
        initStartButton();
    }

    /**
     * Abstract method to be used to calculate the error value and either store and display the error on the gauge, or
     * just display the error on the gauge
     */
    public abstract void calculateErrorValue();

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

    @Override
    public abstract void onClipInfoReadyEvent(ClipInformationEvent e);// {
        /*System.out.println("NEW CLIP INFO EVENT");
        this.scheduledExecutorService.shutdownNow();
        this.nextBeats = createNextBeats();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(6);

        // Create the beat timeline
        long quarter = e.getClipSalsa()/4;
        getSalsaModel().setBeatTimeline(createBeatTimeline(quarter));

        // Might be needed to take into account human reaction
        long buffer = quarter/1000; // Or do like an addition to the GUI or the button clicker..

        // To normalise the user input
        getSalsaModel().setTimeAccumulation(System.currentTimeMillis());

        // Setting up the time windows
        startupTimeWindows();

        // Starting up the thread to keep progressing through the game
        GameProgressFake gameProgress = new GameProgressFake(this);
        this.scheduledExecutorService.scheduleAtFixedRate(gameProgress, 0, quarter, TimeUnit.MILLISECONDS);*/

    //}

    public ArrayList<Integer> getNextBeats() {
        return nextBeats;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    /**
     * Method returns the GameStatusFunction that the MVC application will be using to determine the next state that
     * the game will move on to next
     *
     * @return A GameStatusFunction
     */
    public GameStatusFunction getGameStatusFunction() {
        return gameStatusFunction;
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
                System.out.println("The current beat I am trying to find: " + getSalsaModel().getCurrentBeat());
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
                        calculateErrorValue();

                        System.out.println("I have been printed");System.out.println();
                    }

                    else if (getSalsaModel().getButtonClickerTracker() == 2 && !getSalsaModel().hasClickedOnce2()) {
                        getSalsaModel().setHasClickedOnce2(true);
                        getSalsaModel().decreaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: " + getSalsaModel().getButtonClickerTracker());
                        System.out.println("Window Tracker after change: " + getSalsaModel().getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + getSalsaModel().hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + getSalsaModel().hasClickedOnce2());
                        calculateErrorValue();
                        System.out.println("I have been printed");System.out.println();
                    }
                }
            }
        };
        gameView.getBeatClicker().addActionListener(click);
    }

    /* Helper method to calculate the index of the time that the start of the window will be at */
    private long initialDelay(int requestedBeat, int barNumber) {
        int indexForInitialDelay = requestedBeat - TIME_WINDOW - 1 + (8*(barNumber - 1));
        System.out.println("Index for start window: " + indexForInitialDelay);
        System.out.println("Start window: " + getSalsaModel().getBeatTimeline().get(indexForInitialDelay));
        return getSalsaModel().getBeatTimeline().get(indexForInitialDelay);
    }

    /* Helper method to calculate the index of the time that the end of the window will be at */
    private long period(int requestedBeat, int barNumber, long initialDelay) {
        int indexForPeriod = requestedBeat + TIME_WINDOW - 1 + (8*(barNumber - 1));
        System.out.println("Index for the end window: " + indexForPeriod);
        System.out.println("End window: " + getSalsaModel().getBeatTimeline().get(indexForPeriod));
        System.out.println();
        return getSalsaModel().getBeatTimeline().get(indexForPeriod) - initialDelay;
    }

    private void startupTimeWindows() {
        // 1st time window
        System.out.println("Bar number: " + 1);
        long initDelay1 = initialDelay(getSalsaModel().getNextBeat(), 1);
        ClickTimeWindowFAKE t1 = new ClickTimeWindowFAKE(getSalsaModel());

        // Time windows are the same size
        long period = period(getSalsaModel().getNextBeat(), 1, initDelay1);

        this.scheduledExecutorService.scheduleAtFixedRate(t1, initDelay1, period, TimeUnit.MILLISECONDS);

        // 2nd, 3rd and 4th time window
        for (int i = 1; i < 4; i++) {
            System.out.println("Bar number: " + (i + 1));
            long initDelay = initialDelay(nextBeats.get(i-1), i + 1);
            ClickTimeWindowFAKE tw = new ClickTimeWindowFAKE(getSalsaModel());
            long p = period(nextBeats.get(i - 1), i + 1, initDelay);
            this.scheduledExecutorService.scheduleAtFixedRate(tw, initDelay, p, TimeUnit.MILLISECONDS);
        }
    }

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
        System.out.println(beatTimeline);
        //System.out.println("The beat timeline has been created: " + System.currentTimeMillis());

        return beatTimeline;
    }

    /* The next beats that the simulation will request the user to identify in the music */
    private ArrayList<Integer> createNextBeats() {
        // ArrayList for the next 4 beats in the simulation
        ArrayList<Integer> nextBeats = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            // 2nd and 3rd bar of the 4 8-beat bar Salsa audio clip
            if (i == 0 || i == 1) {
                int nextBeat = randomGenerator.nextInt(8) + 1;
                nextBeats.add(nextBeat);
            }

            // 4th bar of the 4 8-beat bar Salsa audio clip
            else if (i == 2) {
                int nextBeat = randomGenerator.nextInt(5) + 1;
                nextBeats.add(nextBeat);
            }

            // 1st bar of the next 4 8-beat bar Salsa audio clip
            else {
                int nextBeat = randomGenerator.nextInt(5) + 4;
                nextBeats.add(nextBeat);
            }
        }
        System.out.println(nextBeats);
        return nextBeats;
    }
}
