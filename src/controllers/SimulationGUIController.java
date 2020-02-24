package controllers;

import events.SimulationEvent;
import events.SimulationListener;
import main.SalsaController;
import main.SalsaModel;
import views.SimulationView;

public class SimulationGUIController extends SalsaController implements SimulationListener {

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
        // Pop the GUI gauge up
        // Pop pictures of the instruments that will be played
        // IF POSSIBLE: 3-2-1 GUI that disappears just before the salsa music

    }

    @Override
    public void onNewBeatEvent(SimulationEvent e) {
        // Make the current labels or png invisible and make the pictures that correspond to the model's
        // current and next beat to be visible
    }

    @Override
    public void onNewStateEvent(SimulationEvent e) {
        // Make the instrument GUI that are not in the combo of the current state invisible
        // and all of the ones in the combo visible
        // Also update the label SLOW, MEDIUM, FAST so that the right one is showing
    }

    @Override
    public void onNewErrorValue(SimulationEvent e) {
        // You should be able to get the error value from e as I have overloaded the constructor
        // Logic here will be to move the gauge needle accordingly with the error value

    }
}
