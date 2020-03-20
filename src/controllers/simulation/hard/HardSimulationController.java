package controllers.simulation.hard;

import components.enums.State;
import components.functions.ef.LinearErrorFunction;
import components.functions.gsf.DynamicGameDifficultyBalancingHard;
import components.functions.ErrorFunction;
import components.functions.GameStatusFunction;
import controllers.GameController;
import events.GameEvent;
import main.SalsaModel;
import components.ingame.levels.HardProgress;
import views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * HardSimulationController Class extends the GameController class and overrides some of its methods so that "fire" methods
 * affect the HardSimulationView only.
 * This Class deals with the user's input and controls the flow of the game by making sure that the appropriate
 * events are fired at specific times so that the HardSimulationGUIController and the HardSimulationMusicController can also
 * execute their logic.
 *
 * @author Gareth Iguasnia
 * @date 02/03/2020
 */
public class HardSimulationController extends GameController {
    // As we are programming to an interface, we can switch out the ErrorFunction with ease
    private ErrorFunction errorFunction;

    // As we are programming to an interface, we can switch out the GameStatusFunction with ease
    private GameStatusFunction gameStatusFunction;

    /**
     * Constructor for the HardSimulationController.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView       A GameView object that will be the HardSimulationView
     */
    public HardSimulationController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);

        // The Game Status function for the Hard mode of the game
        this.gameStatusFunction = new DynamicGameDifficultyBalancingHard();
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

    @Override
    public void clipReady(long clipSalsa) {
        //new GameProgress(this, clipSalsa);
        HardProgress hardProgress = new HardProgress(this, clipSalsa);
        hardProgress.start();
    }

    @Override
    public void calculateErrorValue(int currentBeat, int barNumber) {
        // Normalising the time stamp of the user's input
        long clickTSNormalised = System.currentTimeMillis() - getSalsaModel().getTimeAccumulation();

        // Necessary information to initialise the error function
        long requiredBeatTime = getSalsaModel().getBeatTimeline().get(currentBeat - 1 + 8*(barNumber - 1));
        System.out.println("Bar number: " + barNumber);
        System.out.println("Current beat: " + currentBeat);
        System.out.println("Required beat time: " + requiredBeatTime);

        // Initialising the error function for the Linear Error Function
        long one_beat = getSalsaModel().getBeatTimeline().get(1);
        long left_time_window = getSalsaModel().getBeatTimeline().get(
                currentBeat - 1 + 8*(barNumber - 1) - getTIME_WINDOW());
        System.out.println("Left time window: " + left_time_window);
        this.errorFunction = new LinearErrorFunction(one_beat, left_time_window);

        // Error value calculated using the error function and then added to the UserProfile
        double errorValue = errorFunction.calculateErrorValue(clickTSNormalised, requiredBeatTime);
        getSalsaModel().setErrorValue(errorValue);

        // Setting the event object with the recently recorded error value
        GameEvent simEvent = new GameEvent(getSalsaModel(), errorValue);

        // New error value event fired so that the GUI can display it
        getSalsaModel().fireNewErrorValueEvent(simEvent);
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

                // Only 15 different State objects will be visited
                getSalsaModel().setNumTransitionedStates(15);

                // Update the model
                getSalsaModel().setCurrentState(randState);
                getSalsaModel().setNextBeat(firstBeat);

                // Fire off the events
                getSalsaModel().fireSimulationStartEvent();
                getSalsaModel().fireNewBeatEvent();
            }
        };
        getGameView().getStartButton().addActionListener(start);
    }

    @Override
    public State chooseStartingState() {
        // The random starting State that has been chosen
        State randState;
        // To loop through all of the State objects in the User Profile
        Map<String, State> states = getSalsaModel().getUserProfile().getStates();

        // An ArrayList of State objects that have not been explored
        ArrayList<State> unexploredStates = new ArrayList<State>();

        // An ArrayList of State objects that have previously been explored
        ArrayList<State> exploredStates = new ArrayList<State>();

        for (Map.Entry<String, State> entry: states.entrySet()) {
            if (!entry.getValue().hasBeenExplored())
                unexploredStates.add(entry.getValue());
            else
                exploredStates.add(entry.getValue());
        }

        // If there are some States that have not been explored, then randomly choose one of those State objects
        if (!unexploredStates.isEmpty()) {
            int rndIndex = this.getRandomGenerator().nextInt(unexploredStates.size());
            randState = unexploredStates.get(rndIndex);
        }
        // Otherwise, randomly choose a starting State from all possible State objects
        else {
            int rndIndex = this.getRandomGenerator().nextInt(exploredStates.size());
            randState = exploredStates.get(rndIndex);
        }
        return randState;
    }
}
