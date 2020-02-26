package controllers;

import components.*;
import events.ClipInformationEvent;
import events.ClipInformationListener;
import main.SalsaController;
import main.SalsaModel;
import views.JustifiedUserProfileView;
import views.SimulationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class SimulationController extends SalsaController implements ClipInformationListener {

    private SimulationView simulationView;

    // As we are programming to an interface, we can switch out the ErrorFunction and GameStatusFunction with ease
    private ErrorFunction errorFunction;
    private GameStatusFunction gameStatusFunction;

    // Random Generator to be used to get the next beat that the user will be tested on
    // and to find a starting state
    private Random randomGenerator;

    private long simulationStart;
    private long oneMinute;
    //A boolean to check whether you are good to use the error function...or in the Model?

    public SimulationController(SalsaModel salsaModel, String controllerName, SimulationView simulationView) {
        super(salsaModel, controllerName);
        this.simulationView = simulationView;
        this.errorFunction = new GaussianErrorFunction();
        this.gameStatusFunction = new DynamicGameDifficultyBalancing();
        this.randomGenerator = new Random();

        initButtonClicker();
        initStartButton();

        // test -- Not really needed tbh. The issue is with the function
        // Actually - this is good but should be in a separate function and not constructor
        this.simulationStart = System.currentTimeMillis();
        this.oneMinute = 10*1000;

        // Play the 321 clip here -- Initiate the PlayFile[] here then?
    }

    // This controller should observe model so that any updates on things like "a new clip should be played" will
    // mean that this controller will calculate the average value and then calculate the overall average value
    // and then use the current average value along with the state movement to determine which state to move on to
    // next. Once that has been chosen, then we can then create another PlayFile object from the finite
    // PlayFile[] and join it to the queue...?

    @Override
    public void onInitClipInfoReadyEvent(ClipInformationEvent e) {
        // Connected to the method fireClipInfoReadyEvent (model) that is called in the SimulationMusicController - YES

        // So we have


        // Start the thread that sets the flag in the model "hasClickedOnce" to false at each time calculated in
        // the ClipInformationEvent - Divide the Salsa Bar into 4 and determine when a new 8-beat bar should finish and
        // start so that the thread can at that time set the flag to false to allow the user click again to avoid
        // multiple inputs
        // This information should be in the controller so that we can also determine what time the requested beat
        // will occur

        // This method takes into account the 123 clip (the difference between the bottom one)
        // so a thread would turn hasClickedOnce in false once the 123 clip has finished
    }

    @Override
    public void onClipInfoReadyEvent(ClipInformationEvent e) {
        // Connected to the method fireClipInfoReadyEvent (model) that is called in the SimulationMusicController
        // Start the thread that sets the flag in the model "hasClickedOnce" to false at each time calculated in
        // the ClipInformationEvent - Divide the Salsa Bar into 4 and determine when a new 8-beat bar should finish and
        // start so that the thread can at that time set the flag to false to allow the user click again to avoid
        // multiple inputs
        // This information should be in the controller so that we can also determine what time the requested beat
        // will occur
    }

    private void initButtonClicker() {
        ActionListener click = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                //System.out.println(timestamp.getTime());
                //System.out.println(test);
                //System.out.println(gef.calculateErrorValue(timestamp.getTime(), test));
                long clickTSNormalised = System.currentTimeMillis() - simulationStart;
                System.out.println(clickTSNormalised);
                double errorValue = errorFunction.calculateErrorValue(clickTSNormalised, oneMinute);
                System.out.println(errorValue);
                // Here a check needs to happen if the next clip is going to play
                // Another check also needs to happen to ensure the user clicks once every 8 beats
                // No, this should only worry about getting the error value and adding it to the error
                // values of the State and that is it
            }
        };
        simulationView.getBeatClicker().addActionListener(click);
    }

    private void initStartButton() {
        ActionListener start = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Choose a starting State
                State randState = chooseStartingState();

                // Choose a random int
                int firstBeat = randomGenerator.nextInt(8) + 1;

                // Update the model
                getSalsaModel().setCurrentState(randState);
                getSalsaModel().setNextBeat(firstBeat);

                // Fire off the event
                getSalsaModel().fireSimulationStartEvent();
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
