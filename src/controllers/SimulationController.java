package controllers;

import components.*;
import events.ClipInformationEvent;
import events.GameEvent;
import main.SalsaModel;
import timers.GameProgress;
import views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * SimulationController Class extends the GameController class and overrides some of its methods so that "fire" methods
 * affect the SimulationView only.
 * This Class deals with the user's input and controls the flow of the game by making sure that the appropriate
 * events are fired at specific times so that the SimulationGUIController and the SimulationMusicController can also
 * execute their logic.
 *
 * @author Gareth Iguasnia
 * @date 02/03/2020
 */
public class SimulationController extends GameController {
    // As we are programming to an interface, we can switch out the ErrorFunction with ease
    private ErrorFunction errorFunction;

    /**
     * Constructor for the GameController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView       A GameView object that will be the SimulationView
     */
    public SimulationController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
    }

    /**
     * This method deals with the necessary logic when the fireClipInfoReadyEvent(...) method is called in the
     * SimulationMusicController. This method will fire events for every new 8-beat bar and when the Salsa audio clip
     * has finished as well
     *
     * @param e A ClipInformationEvent object that will pass information to GameProgress on the length of the clip of the
     *          Salsa audio clip.
     */
    @Override
    public void onClipInfoReadyEvent(ClipInformationEvent e) {
        // Creates a Timer thread that will execute logic found in the GameProgress class at every new 8-beat bar and when
        // the Salsa audio clip have finished
        new GameProgress(this, e.getClipSalsa());
    }

    /* Helper method to calculate the error value when the user is trying to find the requested beat */
    @Override
    public void calculateErrorValue() {
        // Normalising the time stamp of the user's input
        long clickTSNormalised = System.currentTimeMillis() - getSalsaModel().getTimeAccumulation();

        // getTimeAccumulated? Do we need to add the system
        //System.out.println("Not normalised: " + (clickTSNormalised + getSalsaModel().getTimeAccumulation()));
        //System.out.println("Normalised: " + clickTSNormalised);

        // Necessary information to initialise the error function
        int barNumber = getSalsaModel().getBarNumber();
        int currentBeat = getSalsaModel().getCurrentBeat();
        long requiredBeatTime = getSalsaModel().getBeatTimeline().get(currentBeat - 1 + 8*(barNumber - 1));
        System.out.println("Bar number: " + barNumber);
        System.out.println("Current beat: " + currentBeat);
        System.out.println("Required beat time: " + requiredBeatTime);
        // Initialising the error function for the Linear Error Function
        long one_beat = getSalsaModel().getBeatTimeline().get(1);
        long left_time_window = getSalsaModel().getBeatTimeline().get(
                currentBeat - 1 + 8*(barNumber - 1) - getTIME_WINDOW());
        //System.out.println("One beat movement: " + one_beat);
        System.out.println("Left time window: " + left_time_window);
        this.errorFunction = new LinearErrorFunction(one_beat, left_time_window);

        // Error value calculated using the error function and then added to the UserProfile
        double errorValue = errorFunction.calculateErrorValue(clickTSNormalised, requiredBeatTime );
        //System.out.println("Error value is: " + errorValue);
        //System.out.println();
        getSalsaModel().setErrorValue(errorValue);

        // Setting the event object with the recently recorded error value
        GameEvent simEvent = new GameEvent(getSalsaModel(), errorValue);

        // New error value event fired so that the GUI can display it
        getSalsaModel().fireNewErrorValueEvent(simEvent);
    }

    /* Helper method to set up an ActionListener for the Start button on the SimulationView */
    @Override
    public void initStartButton() {
        ActionListener start = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Choose a starting State
                System.out.println("Here");
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

    /* Helper method that gets a random starting State in order for the Simulation to begin */
    @Override
    public State chooseStartingState() {
        // The random starting State that has been chosen
        State randState;
        // To loop through all of the State objects in the User Profile
        Map<String, State> states = getSalsaModel().getUserProfile().getStates();
        System.out.println("Or here");

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
