package events;

import components.State;
import main.SalsaModel;

import java.util.EventObject;

/**
 * GameEvent Class that extends the EventObject Class. This event will be used in the scenarios where we are
 * notifying listeners about updates to the SalsaModel such as: New state, new next beat and new error value. This
 * event will be used with SimulationListeners and SimulationGUIListeners.
 *
 * @author Gareth Iguasnia
 * @date 24/02/2020
 */
public class GameEvent extends EventObject {

    // To simplify the retrieval of the most recent error value added to the UserProfile object
    private double errorValue;

    // To create simple getter methods for the EventObject
    private SalsaModel model;

    /**
     * Constructor 1: Creates the GameEvent object that will be used by the HardSimulationMusicController Class and
     * the HardSimulationGUIController Class. This event will only be used at the start of the simulation.
     *
     * @param source Object that is castable to a SalsaModel object
     */
    public GameEvent(Object source) {
        super(source);
        this.model = (SalsaModel) source;
    }

    /**
     * Constructor 2: Creates the GameEvent object that will be used by the HardSimulationMusicController and
     * the HardSimulationGUIController Class. This event will not be used at the start of the simulation but thereafter
     * until the simulation has finished.
     *
     * @param source Object that is castable to a SalsaModel object
     * @param errorValue A double that represents the error value calculated from the user's input
     */
    public GameEvent(Object source, double errorValue) {
        super(source);
        this.errorValue = errorValue;
        this.model = (SalsaModel) source;
    }

    /**
     * Method gets the error value that has been recorded in this object from the user's input
     *
     * @return A double that represents the error value of the user's most recent input
     */
    public double getErrorValue() {
        return errorValue;
    }

    /**
     * Method to get the current state that the simulation is currently on
     *
     * @return A State object that the simulation is currently on
     */
    public State getCurrentState() {
        return model.getCurrentState();
    }

    /**
     * Method to get the current beat that the simulation is testing the user on
     *
     * @return Returns an integer representing the current beat that the system is requesting the user to find
     */
    public int getCurrentBeat() {
        return model.getCurrentBeat();
    }

    /**
     * Method to get the next beat that the system is going to test the user with
     *
     * @return Returns an integer representing the next beat that the system will test the user to find
     */
    public int getNextBeat() {
        return model.getNextBeat();
    }
}
