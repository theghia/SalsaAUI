package controllers;

import events.SimulationEvent;
import events.SimulationListener;
import main.SalsaController;
import main.SalsaModel;

public class SimulationMusicController extends SalsaController implements SimulationListener {
    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public SimulationMusicController(SalsaModel salsaModel, String controllerName) {
        super(salsaModel, controllerName);
    }

    @Override
    public void onSimulationStartedEvent(SimulationEvent e) {
        // e is the model
        System.out.println("I am in the Simulation Music Controller");
        // Create the clip to play the "123" WAV file
        // Play the clip and join it to the queue
        // Create a new CLip with the information from the model on the current state
        // and join it to the queue
        // model.onInitClipInfoReadyEvent


    }

    @Override
    public void onNewBeatEvent(SimulationEvent e) {
        // Nothing needed here
    }

    @Override
    public void onNewStateEvent(SimulationEvent e) {

    }

    @Override
    public void onNewErrorValue(SimulationEvent e) {
        // Nothing needed here
    }
}
