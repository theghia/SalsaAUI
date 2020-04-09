package components;

import components.enums.BPM;
import components.enums.Instrument;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * State Class that holds the information of the recorded inputs of the user trying to find the requested beats to
 * the Salsa music being played. This will be an extension of the model.
 *
 * @author Gareth Iguasnia
 * @date 01/03/2020
 */
public class State implements Serializable {

    private BPM bpm;
    private ArrayList<Instrument> instruments;

    // This is data held from when the simulation ran on this State.
    // Will be reset every time this State is revisited.
    private ArrayList<Double> errorValues;

    // All of the average error values calculated once the simulation moved on from this State
    private ArrayList<Double> averageErrorValues;

    // The average error value calculated from averageErrorValues
    private double currentAverageErrorValue;

    // A list of possible neighbours
    private ArrayList<State> neighbours;

    // Nodes that have not been explored have priority
    // For a state to count as explored, it needs to have been visited and have an average error value associated to it
    private boolean hasBeenExplored;

    /**
     * Constructor for the State Class.
     *
     * @param bpm BPM object that represents the tempo that will be used for that State
     * @param instruments An ArrayList of Instrument objects that represent the combination of instruments that
     *                    will be used in the State
     */
    public State (BPM bpm, ArrayList<Instrument> instruments) {
        this.bpm = bpm;
        this.instruments = instruments;
        this.hasBeenExplored = false;

        // Instantiating the rest of this class' fields
        this.errorValues = new ArrayList<>();
        this.averageErrorValues = new ArrayList<>();
        this.neighbours = new ArrayList<>();

        // Value of -1 assigned since it can never be that value when calculated with an Error Function
        // This will be an indicator as to whether there is an error value associated to the State object
        this.currentAverageErrorValue = -1;
    }

    public ArrayList<Double> getErrorValues() {
        return errorValues;
    }

    /**
     * Method returns a boolean depending if this State has ever been visited before in the user's game play
     *
     * @return true if the State has been visited before, false otherwise
     */
    public boolean hasBeenExplored() {
        return hasBeenExplored;
    }

    /**
     * Method to get the BPM associated to the State object
     *
     * @return The BPM object related to the State object
     */
    public BPM getBpm() {
        return bpm;
    }

    /**
     * Method to get the combination of instruments assigned to the State object
     *
     * @return An ArrayList of Instruments that represents the combination that will be used by the State object
     */
    public ArrayList<Instrument> getInstruments() {
        return instruments;
    }

    /**
     * Method returns all of the averaged error value that the State object has recorded. These all will be factored
     * into the currentAverageErrorValue field
     *
     * @return An ArrayList of floats representing all of the averaged error values that this State object has recorded
     */
    public ArrayList<Double> getAverageErrorValues() {
        return averageErrorValues;
    }

    /**
     * Method gets the current average error value of the State
     *
     * @return A float representing the current average value of the State
     */
    public double getCurrentAverageErrorValue() {
        return currentAverageErrorValue;
    }

    /**
     * Returns the neighbours of this State
     *
     * @return An ArrayList of State objects representing the neighbours of the current State object
     */
    public ArrayList<State> getNeighbours() {
        return neighbours;
    }

    /**
     * Method removes any recorded error values so that if the State is visited again, we can record another average
     * again
     */
    public void resetErrorValue() {
        this.errorValues = new ArrayList<>();
    }

    /**
     * Method sets the value of currentAverageErrorValue
     *
     * @param currentAverageErrorValue A double representing the total average error value of the State
     */
    public void setCurrentAverageErrorValue(double currentAverageErrorValue) {
        this.currentAverageErrorValue = currentAverageErrorValue;
    }

    /**
     * Method sets the boolean value for the field hasBeenExplored
     *
     * @param hasBeenExplored A boolean variable representing whether the State has been explored
     */
    public void setHasBeenExplored(boolean hasBeenExplored) {
        this.hasBeenExplored = hasBeenExplored;
    }

    @Override
    public boolean equals(Object obj) {

        State compare = (State) obj;

        int instrumentCounter = 0;
        int total = this.getInstruments().size() + compare.getInstruments().size();

        for (Instrument instrument1: this.getInstruments()) {
            for (Instrument instrument2: compare.getInstruments()) {
                if (instrument1.getValue() == instrument2.getValue())
                    instrumentCounter++;
            }
        }

        for (Instrument instrument1: compare.getInstruments()) {
            for (Instrument instrument2: this.getInstruments()) {
                if (instrument1.getValue() == instrument2.getValue())
                    instrumentCounter++;
            }
        }

        return ((instrumentCounter == total) &&
                this.getBpm().getId() == compare.getBpm().getId());
    }
}
