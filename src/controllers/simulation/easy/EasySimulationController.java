package controllers.simulation.easy;

import components.enums.BPM;
import components.State;
import components.functions.ErrorFunction;
import components.functions.ef.LinearErrorFunction;
import components.functions.gsf.DGDBEasy;
import components.ingame.levels.EasyProgress;
import controllers.GameController;
import events.GameEvent;
import main.SalsaModel;
import views.GameView;
import views.TutorialView;
import views.games.EasySimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class EasySimulationController extends GameController {

    // Error Function to be used with the calculateErrorValue() method
    private ErrorFunction errorFunction;

    // To know which stage of the tutorial we are at
    private int tutorialTracker;

    // To add listeners to the extra buttons found in the TutorialView
    private EasySimulationView easySimulationView;

    // Has the user correctly found the requested beat
    private boolean correctAttempt;

    /**
     * Constructor for the GameController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView
     */
    public EasySimulationController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
        this.easySimulationView= (EasySimulationView) this.getGameView();

        // The Game Status function for the Hard mode of the game
        this.setGameStatusFunction(new DGDBEasy());

        // The default is false
        this.correctAttempt = false;
    }

    // Write this up in the game controller but override it in the Tutorial Controller
    @Override
    public void calculateErrorValue(int currentBeat, int barNumber) {
        // Normalising the time stamp of the user's input
        long clickTSNormalised = System.currentTimeMillis() - getSalsaModel().getTimeAccumulation();

        // Necessary information to initialise the error function
        long requiredBeatTime = getSalsaModel().getBeatTimeline().get(currentBeat - 1 + 8*(barNumber - 1));

        // Determining the index of the left time window
        int index_left_TW = currentBeat - 1 + 8*(barNumber - 1) - getTIME_WINDOW();
        if (index_left_TW < 0)
            index_left_TW = 0;

        // Initialising the error function for the Linear Error Function
        long one_beat = getSalsaModel().getBeatTimeline().get(1);
        long left_time_window = getSalsaModel().getBeatTimeline().get(index_left_TW);
        this.errorFunction = new LinearErrorFunction(one_beat, left_time_window);

        // Error value calculated using the error function and then added to the UserProfile
        double errorValue = errorFunction.calculateErrorValue(clickTSNormalised, requiredBeatTime );
        getSalsaModel().setErrorValue(errorValue);

        // Is the error value is above the threshold?
        if (errorValue > this.getGameStatusFunction().getThreshold())
            this.correctAttempt = true;

        // Setting the event object with the recently recorded error value
        GameEvent gameEvent = new GameEvent(getSalsaModel(), errorValue);

        // New error value event fired so that the GUI can display it
        getSalsaModel().fireEasyNewErrorValueEvent(gameEvent);
    }

    @Override
    public void initStartButton() {
        ActionListener start = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Choose a starting State
                State randState = chooseStartingState();

                // Choose a random int between [1, 8]
                int firstBeat = getRandomGenerator().nextInt(8) + 1;

                // Only 8 different State objects will be visited
                getSalsaModel().setNumTransitionedStates(8);

                // Update the model
                getSalsaModel().setCurrentState(randState);
                getSalsaModel().setNextBeat(firstBeat);

                // Fire off the events
                getSalsaModel().fireEasyNewBeatEvent();
                getSalsaModel().fireEasySimulationStartEvent();
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
            if (!entry.getValue().hasBeenExplored() && entry.getValue().getBpm().equals(BPM.SLOW)
            && (entry.getValue().getInstruments().size() <= 2))
                unexploredStates.add(entry.getValue());
            else if (entry.getValue().hasBeenExplored() && entry.getValue().getBpm().equals(BPM.SLOW)
            && (entry.getValue().getInstruments().size() <= 2))
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

    @Override
    public void clipReady(long clipSalsa) {
        // Fires a method that will make the appropriate light turn on to display the timing of the Salsa audio clip
        getSalsaModel().fireEasyLightsOnEvent();

        // Creates a Timer thread that will execute logic found in the GameProgress class at every new 8-beat bar
        // for 4 bars. Then a new state is chosen, and the process is repeated
        EasyProgress easyProgress = new EasyProgress(this, clipSalsa);
        easyProgress.start();
    }

    public boolean isCorrectAttempt() {
        return correctAttempt;
    }

    public void setCorrectAttempt(boolean correctAttempt) {
        this.correctAttempt = correctAttempt;
    }
}
