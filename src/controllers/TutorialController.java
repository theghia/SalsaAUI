package controllers;

import components.ErrorFunction;
import components.LinearErrorFunction;
import components.State;
import events.ClipInformationEvent;
import events.GameEvent;
import main.SalsaModel;
import views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TutorialController class extends the GameController class and overrides some of the methods in the GameController
 * class so that the TutorialView is manipulated with the "fire" methods
 *
 * @author Gareth Iguasnia
 * @date 12/03/2020
 */
public class TutorialController extends GameController {
    // Error Function to be used with the calculateErrorValue() method
    private ErrorFunction errorFunction;

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
    }

    /**
     * Method calculates the error value of the user's input and fires an event so that the gauge's needle moves
     * accordingly. The error value is not recorded in the UserProfile
     */
    @Override
    public void calculateErrorValue() {
        // Normalising the time stamp of the user's input
        long clickTSNormalised = System.currentTimeMillis() - getSalsaModel().getTimeAccumulation();

        // Necessary information to initialise the error function
        int barNumber = getSalsaModel().getBarNumber();
        int currentBeat = getSalsaModel().getCurrentBeat();
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
                //getSalsaModel().fireTutorialStartEvent(); -- onGameStartedEvent for the GUI will be called
                // as well as another onTutorialStartedEvent -> after this mini tutorial finishes
                // we then call onGameStartedEvent for the MusicController since the clip itself should call the
                //appropriate countdownStartedEvent
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

    @Override
    public void onClipInfoReadyEvent(ClipInformationEvent e) {
        // We want to modify the ClickOnce Class so that we can progress onto states that are only slow and medium
    }
}
