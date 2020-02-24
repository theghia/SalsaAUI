package events;

import java.util.EventObject;

/**
 * SimulationEvent Class that extends the EventObject Class. This event will be used in the scenarios where we are
 * notifying listeners about updates to the SalsaModel such as: New state, new next beat and new error value
 *
 * @author Gareth Iguasnia
 * @date 24/02/2020
 */
public class SimulationEvent extends EventObject {

    // To simplify the retrieval of the most recent error value added to the UserProfile object
    double errorValue;

    /**
     * Constructor 1: Creates the SimulationEvent object that will be used by the SimulationMusicController Class and
     * the SimulationGUIController Class. This event will only be used at the start of the simulation.
     *
     * @param source Object that is castable to a SalsaModel object
     */
    public SimulationEvent(Object source) {
        super(source);
    }

    /**
     * Constructor 2: Creates the SimulationEvent object that will be used by the SimulationMusicController and
     * the SimulationGUIController Class. This event will not be used at the start of the simulation but thereafter
     * until the simulation has finished.
     *
     * @param source Object that is castable to a SalsaModel object
     * @param errorValue A double that represents the error value calculated from the user's input
     */
    public SimulationEvent(Object source, double errorValue) {
        super(source);
        this.errorValue = errorValue;
    }

    /**
     * Method gets the error value that has been recorded in this object from the user's input
     *
     * @return A double that represents the error value of the user's most recent input
     */
    public double getErrorValue() {
        return errorValue;
    }

    // All objects that will be passed through here are going to be MODELSSSS
    // so that the model does not have to be involved with any logic bar:
    // SimulationEvent e = new SimulationEvent(this); BUUUT this is the modelllllll
    // this.listener...onSimulationStartEvent(e);
    // This is good because the listener object will not be able to access the model as it is a Listener and not
    // a controller. So the only access it will have to the model is via the event.

}
