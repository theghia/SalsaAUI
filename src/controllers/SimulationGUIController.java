package controllers;

import events.ClipInformationEvent;
import events.SimulationEvent;
import events.SimulationGUIListener;
import events.SimulationListener;
import main.SalsaController;
import main.SalsaModel;
import views.SimulationView;

public class SimulationGUIController extends SalsaController implements SimulationListener, SimulationGUIListener {

    private SimulationView simulationView;

    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public SimulationGUIController(SalsaModel salsaModel, String controllerName, SimulationView simulationView) {
        super(salsaModel, controllerName);
        this.simulationView = simulationView;
    }

    @Override
    public void onSimulationStartedEvent(SimulationEvent e) {
        // e is the model
        System.out.println("I am in the Simulation GUI Controller");
        // Make the labels or GUI to show the numbers visible for nextBeat and currentBeat
        // Populate the nextBeat label so that the user can prep themselves to find it
        // Pop the GUI gauge up
        // Pop pictures of the instruments that will be played -- NO don't do this?
        // IF POSSIBLE: 3-2-1 GUI that disappears just before the salsa music -- NO

    }

    @Override
    public void onNewStateEvent(SimulationEvent e) {
        // Make the instrument GUI that are not in the combo of the current state invisible
        // and all of the ones in the combo visible
        // Also update the label SLOW, MEDIUM, FAST so that the right one is showing
    }

    @Override
    public void onNewBeatEvent(SimulationEvent e) {
        // Make the current labels or png invisible and make the pictures that correspond to the model's
        // current and next beat to be visible
        System.out.println("Current beat: " + e.getCurrentBeat());
        System.out.println("Next beat: " + e.getNextBeat());
    }

    @Override
    public void onNewErrorValueEvent(SimulationEvent e) {
        // You should be able to get the error value from e as I have overloaded the constructor
        // Logic here will be to move the gauge needle accordingly with the error value
    }

    @Override
    public void onSimulationFinishedEvent(SimulationEvent e) {
        // Hide the button clicker and all of the instrument GUI and set the beat number
        // to blank and then make the start button visible and switch the panel to the start screen
    }

    @Override
    public void onCountdownStartedEvent(ClipInformationEvent e) {
        // This method will be called by the model in model.fireCountDownStartedEvent()
        // from e, we can get the clip length of the countdown audio and put up the relevant
        // GUI at the spaced intervals (In the ClickOnce Class)

        // Here we need to set the flag countdownCurrentlyPlaying to false which can be done in this timer thread
        // and this will be done once the clip has finished. Just like what you did with the SimulationController
        // NOOOO, this needs to be called after the clip has finished!
        //getSalsaModel().setCountdownCurrentlyPlaying(false);

        // Here is where we start the two timer threads. One will do the GUI countdown crap and the other
        // will do the onCountDownFinishedEvent (by firing the relevant event of course). We can start the
        // timer threads in the CountDown Class! ONLY NEED ONE TIMER THREAD
    }

    @Override
    public void onCountdownFinishedEvent() {
        // ClickOnce calls the fire method for this method
        // Here we need to:

        // 1) Set the flag countdownCurrentlyPlaying to false
        getSalsaModel().setCountdownCurrentlyPlaying(false);

        // 2) Make the button clicker clickable

        // 3) Make the GUI instruments appear -> so probs have SimulationEvent as a parameter
    }
}
