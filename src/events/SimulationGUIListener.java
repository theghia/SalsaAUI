package events;

import java.util.EventListener;

public interface SimulationGUIListener extends EventListener {

    /**
     * This method deals with the event of a new 8-beat bar starting.
     *
     * @param e A SimulationEvent object that will contain information on what the next beat the user will be tested on
     */
    void onNewBeatEvent(SimulationEvent e);

    /**
     * This method deals with the event of when an Error Value is recorded from the user's input
     *
     * @param e A SimulationEvent object that will be used to pass the information of the new Error Value to the
     *          listener.
     */
    void onNewErrorValueEvent(SimulationEvent e);

    /**
     * This method deals with the event of when the Simulation has finished. Cleans up the GUI ready for the next
     * time the simulation is started again.
     *
     * @param e A SimulationEvent object that will be used to reset the model (except the UserProfile) back to default
     */
    void onSimulationFinishedEvent(SimulationEvent e);

    /**
     * This method deals with the event of when the countdown audio clip has started to play.
     *
     * @param e A ClipInformationEvent object that will be used to determine when the countdown audio clip will finish
     */
    void onCountdownStartedEvent(ClipInformationEvent e);

    void onCountdownFinishedEvent();
}
