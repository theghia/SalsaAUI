package controllers;

import components.*;
import events.ClipInformationEvent;
import events.ClipInformationListener;
import main.SalsaController;
import main.SalsaModel;
import timers.ClickOnce;
import views.SimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * SimulationController Class extends the SalsaController Class and implements the ClipInformationListener interface.
 * This Class deals with the user's input and controls the flow of the Simulation by making sure that the appropriate
 * events are fired at specific times so that the SimulationGUIController and the SimulationMusicController can also
 * execute their logic.
 *
 * @author Gareth Iguasnia
 * @date 02/03/2020
 */
public class SimulationController extends SalsaController implements ClipInformationListener {
    // To have access to the buttons in the SimulationView Class
    private SimulationView simulationView;

    // As we are programming to an interface, we can switch out the ErrorFunction and GameStatusFunction with ease
    private ErrorFunction errorFunction;
    private GameStatusFunction gameStatusFunction;

    // Random Generator to be used to get the next beat that the user will be tested on
    // and to find a starting state
    private Random randomGenerator;

    /**
     * Constructor for the SimulationController.
     *
     * @param salsaModel A SalsaModel object that contains the data of the MVC application
     * @param controllerName A String representing the name of the controller
     * @param simulationView The view that this controller will be interacting with
     */
    public SimulationController(SalsaModel salsaModel, String controllerName, SimulationView simulationView) {
        super(salsaModel, controllerName);
        this.simulationView = simulationView;
        this.errorFunction = new GaussianErrorFunction();
        this.gameStatusFunction = new DynamicGameDifficultyBalancing();
        this.randomGenerator = new Random();

        initButtonClicker();
        initStartButton();
    }

    /**
     * This method deals with the necessary logic when the fireClipInfoReadyEvent(...) method is called in the
     * SimulationMusicController. This method will fire events for every new 8-beat bar and when both the countdown
     * and Salsa audio clip have finished
     *
     * @param e A ClipInformationEvent that will pass on information to the ClickOnce object on the length of the clip
     *          of the countdown and salsa audio clip
     */
    @Override
    public void onInitClipInfoReadyEvent(ClipInformationEvent e) {
        // Creates a Timer thread that will execute logic found in the ClickOnce class at every new 8-beat bar and when
        // both the countdown and Salsa audio clip have finished
        new ClickOnce(this, e.getClip123(), e.getClipSalsa());
    }

    /**
     * This method deals with the necessary logic when the fireClipInfoReadyEvent(...) method is called in the
     * SimulationMusicController. This method will fire events for every new 8-beat bar and when the Salsa audio clip
     * has finished as well
     *
     * @param e A ClipInformationEvent object that will pass information to ClickOnce on the length of the clip of the
     *          Salsa audio clip.
     */
    @Override
    public void onClipInfoReadyEvent(ClipInformationEvent e) {
        // Creates a Timer thread that will execute logic found in the ClickOnce class at every new 8-beat bar and when
        // the Salsa audio clip have finished
        new ClickOnce(this, e.getClipSalsa());
    }

    /**
     * Method to get the GameStatusFunction that is being used in the MVC application. This will be called by the
     * ClickOnce Class in order to choose the next state that the application will move on to next.
     *
     * @return A GameStatusFunction object which will be used to determine the next State object that will be visited
     */
    public GameStatusFunction getGameStatusFunction() {
        return gameStatusFunction;
    }

    /* Helper method to add an ActionListener to the button to be used by the user to input their try */
    private void initButtonClicker() {
        ActionListener click = new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                /*System.out.println("Button Clicker Tracker: " + getSalsaModel().getButtonClickerTracker());
                System.out.println("hasClickedOnce1: " + getSalsaModel().hasClickedOnce1());
                System.out.println("hasClickedOnce2: " + getSalsaModel().hasClickedOnce2());*/

                // If a Salsa audio clip is currently playing, then proceed
                if (!getSalsaModel().isCountdownCurrentlyPlaying()) {
                    // Checking if has previously clicked in the assigned time windows
                    System.out.println("Button Clicker: " + getSalsaModel().getButtonClickerTracker());
                    System.out.println("hasClickedOnce1: " + getSalsaModel().hasClickedOnce1());
                    System.out.println("hasClickedOnce2: " + getSalsaModel().hasClickedOnce2());

                    if (getSalsaModel().getButtonClickerTracker() == 1 && !getSalsaModel().hasClickedOnce1()) {
                        getSalsaModel().setHasClickedOnce1(true);
                        getSalsaModel().increaseButtonClickerTracker();
                        calculateErrorValue();
                        System.out.println("I have been printed");System.out.println();
                    }

                    else if (getSalsaModel().getButtonClickerTracker() == 2 && !getSalsaModel().hasClickedOnce2()) {
                        getSalsaModel().setHasClickedOnce2(true);
                        getSalsaModel().decreaseButtonClickerTracker();
                        calculateErrorValue();
                        System.out.println("I have been printed");System.out.println();
                    }
                }
            }
        };
        simulationView.getBeatClicker().addActionListener(click);
    }

    /* Helper method to calculate the error value when the user is trying to find the requested beat */
    private void calculateErrorValue() {
        // Normalising the time stamp of the user's input
        long clickTSNormalised = System.currentTimeMillis() - getSalsaModel().getTimeAccumulation();

        // getTimeAccumulated? Do we need to add the system
        System.out.println(clickTSNormalised);

        // Error value calculated using the error function and then added to the UserProfile
        int barNumber = getSalsaModel().getBarNumber();
        int currentBeat = getSalsaModel().getCurrentBeat();
        long requiredBeatTime = getSalsaModel().getBeatTimeline().get(currentBeat - 1 + 8*(barNumber - 1));
        double errorValue = errorFunction.calculateErrorValue(clickTSNormalised, requiredBeatTime );
        getSalsaModel().setErrorValue(errorValue);

        // New error value event fired so that the GUI can display it
        getSalsaModel().fireNewErrorValueEvent();
    }

    /* Helper method to set up an ActionListener for the Start button on the SimulationView */
    private void initStartButton() {
        ActionListener start = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Choose a starting State
                State randState = chooseStartingState();

                // Choose a random int between [4, 8]
                int firstBeat = randomGenerator.nextInt(5) + 4;

                // Only 15 different State objects will be visited
                getSalsaModel().setNumTransitionedStates(15);

                // Update the model
                getSalsaModel().setCurrentState(randState);
                getSalsaModel().setNextBeat(firstBeat);
                getSalsaModel().addToTimeAccumulated(System.currentTimeMillis());

                // Fire off the events
                getSalsaModel().fireSimulationStartEvent();
                getSalsaModel().fireNewBeatEvent();
            }
        };
        simulationView.getStartButton().addActionListener(start);
    }

    /* Helper method that gets a random starting State in order for the Simulation to begin */
    private State chooseStartingState() {
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
            int rndIndex = this.randomGenerator.nextInt(unexploredStates.size());
            randState = unexploredStates.get(rndIndex);
        }
        // Otherwise, randomly choose a starting State from all possible State objects
        else {
            int rndIndex = this.randomGenerator.nextInt(exploredStates.size());
            randState = exploredStates.get(rndIndex);
        }
        return randState;
    }
}
