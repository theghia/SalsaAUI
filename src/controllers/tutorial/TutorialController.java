package controllers.tutorial;

import components.functions.ErrorFunction;
import components.functions.ef.LinearErrorFunction;
import components.enums.State;
import controllers.GameController;
import events.GameEvent;
import main.SalsaModel;
import components.ingame.levels.HardProgress;
import views.GameView;
import views.TutorialView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TutorialController class extends the GameController class and overrides some of the methods in the GameController
 * class so that the TutorialView is manipulated with the "fire" methods
 *
 * This Class deals with the user's input and controls the flow of the game by making sure that the appropriate
 * events are fired at specific times so that the TutorialGUIController and the TutorialMusicController can also
 * execute their logic.
 *
 * @author Gareth Iguasnia
 * @date 12/03/2020
 */
public class TutorialController extends GameController {
    // Error Function to be used with the calculateErrorValue() method
    private ErrorFunction errorFunction;

    private int tutorialTracker;

    private TutorialView tutorialView;

    /**
     * Constructor for the TutorialController.
     * This class will be interacting with the TutorialMusicController and the TutorialGUIController
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView       A GameView object representing the view associated to this controller
     */
    public TutorialController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
        this.tutorialView = (TutorialView) this.getGameView();

        // The default value for the tutorialTracker
        this.tutorialTracker = 0;

        // Start up the extra buttons in the TutorialView
        initStartButton();
        initBackButton();
        initNextButton();
        initNoButton();
        initPracticeButton();
        initSkipButton();
    }

    /**
     * Method calculates the error value of the user's input and fires an event so that the gauge's needle moves
     * accordingly. The error value is not recorded in the UserProfile
     */
    @Override
    public void calculateErrorValue(int currentBeat, int barNumber) {
        // Normalising the time stamp of the user's input
        long clickTSNormalised = System.currentTimeMillis() - getSalsaModel().getTimeAccumulation();

        // Necessary information to initialise the error function
        long requiredBeatTime = getSalsaModel().getBeatTimeline().get(currentBeat - 1 + 8*(barNumber - 1));

        // Initialising the error function for the Linear Error Function
        long one_beat = getSalsaModel().getBeatTimeline().get(1);
        long left_time_window = getSalsaModel().getBeatTimeline().get(
                currentBeat - 1 + 8*(barNumber - 1) - getTIME_WINDOW());
        this.errorFunction = new LinearErrorFunction(one_beat, left_time_window);

        // Error value calculated using the error function and then added to the UserProfile
        double errorValue = errorFunction.calculateErrorValue(clickTSNormalised, requiredBeatTime );

        // Setting the event object with the recently recorded error value
        GameEvent gameEvent = new GameEvent(getSalsaModel(), errorValue);

        // New error value event fired so that the GUI can display it
        getSalsaModel().fireTutorialNewErrorValueEvent(gameEvent);
    }

    /**
     * Start button begins the tutorial to teach the user how to use the system and then lets the user play the game
     * without recording the error values
     */
    @Override
    public void initStartButton() {
        ActionListener start = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Choose a starting State
                State randState = chooseStartingState();

                // Choose a random int between [4, 8]
                int firstBeat = getRandomGenerator().nextInt(5) + 4;

                // Only 8 different State objects will be visited
                getSalsaModel().setNumTransitionedStates(8);

                // Update the model
                getSalsaModel().setCurrentState(randState);
                getSalsaModel().setNextBeat(firstBeat);

                // Fire off the events
                getSalsaModel().fireTutorialNewBeatEvent();
                getSalsaModel().fireTutorialStartEvent();
            }
        };
        getGameView().getStartButton().addActionListener(start);
    }

    /**
     * The starting state must have the slowest tempo to help train the user
     *
     * @return State object that the Tutorial will begin with
     */
    @Override
    public State chooseStartingState() {
        // Starting at a node that is slow?
        return null;
    }

    /**
     * TutorialController will not only start the GameProgress but also the Lights class so that the timing of the
     * Salsa music is displayed in sync with the lights
     *
     * @param clipSalsa Long value representing the length of the Salsa audio clip
     */
    @Override
    public void clipReady(long clipSalsa) {
        // Fires a method that will make the appropriate light turn on to display the timing of the Salsa audio clip
        getSalsaModel().fireLightsOnEvent();

        // Creates a Timer thread that will execute logic found in the GameProgress class at every new 8-beat bar
        // for 4 bars. Then a new state is chosen, and the process is repeated
        //new GameProgress(this, clipSalsa);
        HardProgress hardProgress = new HardProgress(this, clipSalsa);
        hardProgress.start();
    }

    private void initNextButton() {
        ActionListener nextClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tutorialTracker == 0) {
                    tutorialTracker++;
                    getSalsaModel().fireTutorialKeepInstrumentsAndTempo();
                }

                else if (tutorialTracker == 1) {
                    tutorialTracker++;
                    getSalsaModel().fireTutorialKeepCurrentBeat();
                }

                else if (tutorialTracker == 2) {
                    tutorialTracker++;
                    getSalsaModel().fireTutorialKeepNextBeat();
                }

                else if (tutorialTracker == 3) {
                    tutorialTracker++;
                    getSalsaModel().fireTutorialKeepClicker();
                }

                else if (tutorialTracker == 4) {
                    tutorialTracker++;
                    getSalsaModel().fireTutorialKeepGauge();
                }

                else if (tutorialTracker == 5) {
                    tutorialTracker++;
                    getSalsaModel().fireTutorialFinalEvent();
                }
            }
        };
        tutorialView.getNext().addActionListener(nextClick);
    }

    private void initBackButton() {
        ActionListener backClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tutorialTracker == 1) {
                    tutorialTracker--;
                    getSalsaModel().fireTutorialStartEvent();
                }

                else if (tutorialTracker == 2) {
                    tutorialTracker--;
                    getSalsaModel().fireTutorialKeepInstrumentsAndTempo();
                }

                else if (tutorialTracker == 3) {
                    tutorialTracker--;
                    getSalsaModel().fireTutorialKeepCurrentBeat();
                }

                else if (tutorialTracker == 4) {
                    tutorialTracker--;
                    getSalsaModel().fireTutorialKeepNextBeat();
                }

                else if (tutorialTracker == 5) {
                    tutorialTracker--;
                    getSalsaModel().fireTutorialKeepClicker();
                }

                else if (tutorialTracker == 6) {
                    tutorialTracker--;
                    getSalsaModel().fireTutorialKeepGauge();
                }
            }
        };
        tutorialView.getBack().addActionListener(backClick);
    }

    private void initSkipButton() {
        ActionListener skipClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tutorialTracker = 6;
                getSalsaModel().fireTutorialFinalEvent();
            }
        };
        tutorialView.getSkip().addActionListener(skipClick);
    }

    private void initPracticeButton() {
        ActionListener practiceClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tutorialTracker = 0;
                getSalsaModel().fireTutorialFinishedEvent();
            }
        };
        tutorialView.getPractice().addActionListener(practiceClick);
    }

    private void initNoButton() {
        ActionListener noClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tutorialTracker = 0;
                getSalsaModel().resetModel();
                getSalsaModel().fireTutorialNewBeatEvent();
                getSalsaModel().fireTutorialStartScreen();
            }
        };
        tutorialView.getNo().addActionListener(noClick);
    }
}
