package components;

import java.util.ArrayList;

public class State {

    private BPM bpm;
    private ArrayList<Instrument> instruments;

    // This is data held from when the simulation ran on this State.
    // Will be reset every time this State is revisited.
    private ArrayList<Double> errorValues;

    // All of the average error values calculated once the simulation moved on from this State
    private ArrayList<Double> averageErrorValues;

    // The average error value calculated from averageErrorValues
    private float currentAverageErrorValue;

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
        this.errorValues = new ArrayList<Double>();
        this.averageErrorValues = new ArrayList<Double>();
        this.neighbours = new ArrayList<State>();

        // Value of -1 assigned since it can never be that value when calculated with an Error Function
        // This will be an indicator as to whether there is an error value associated to the State object
        this.currentAverageErrorValue = -1;
    }

    /* Switch hasBeenSimulated for hasBeenExplored -> if there are error values in errorValues
    public void calculateCurrentAverageErrorValue() {
        if (hasBeenSimulated) {
            recalculateAverageErrorValues();

            // Calculating to total average error value
            currentAverageErrorValue = getAverage(averageErrorValues);
        }
    }*/


    /* Helper method to calculate the average error value from the simulation */
    /*private void recalculateAverageErrorValues() {
        // Calculating the average
        float average = getAverage(errorValues);

        // Adding the averaged error value to the State's data
        this.averageErrorValues.add(average);

        // Resetting the values to be used again the next time this State is visited
        this.errorValues = null;
    }*/

    /* Helper method to calculate the average value in an ArrayList<Float> */
    private float getAverage(ArrayList<Float> values) {
        float average = 0;

        for (float value: values) {
            average += value;
        }

        average /= values.size();

        return average;
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

    // So in the JUP, they can delete a previous attempt but the system will include that
    // State in the next run
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
    public float getCurrentAverageErrorValue() {
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
}
