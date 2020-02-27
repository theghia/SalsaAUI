package events;

import java.util.EventListener;

/**
 * SimulationListener Interface that will need to be implemented by the class that needs to be notified every time the
 * music has gone to a new 8 beat bar, has completed 4 8-beat bars of music, has recorded an error value from the user
 * input and when the simulation has started.
 */
public interface SimulationListener extends EventListener {

    /**
     * This method deals with the event of the Simulation starting.
     *
     * @param e A SimulationEvent object that will be used to initialise the GUI and Sound accordingly
     */
    void onSimulationStartedEvent(SimulationEvent e);

    /**
     * This method deals with the event of a new 8-beat bar starting.
     *
     * @param e A SimulationEvent object that will contain information on what the next beat the user will be tested on
     */
    void onNewBeatEvent(SimulationEvent e);

    /**
     * This method deals with the event of when the simulation moves onto a new State.
     *
     * @param e A SimulationEvent object that will be used to take the necessary action on moving to a new State
     */
    void onNewStateEvent(SimulationEvent e);

    /**
     * This method deals with the event of when an Error Value is recorded from the user's input
     *
     * @param e A SimulationEvent object that will be used to pass the information of the new Error Value to the
     *          listeners.
     */
    void onNewErrorValue(SimulationEvent e);

    /**
     * This method deals with the event of when the Simulation has finished. Cleans up the GUI ready for the next
     * time the simulation is started again.
     *
     * @param e A SimulationEvent object that will be used to reset the model (except the UserProfile) back to default
     */
    void onSimulationFinished(SimulationEvent e);
}
