package components;

import java.util.ArrayList;

public class State {

    private BPM bpm;
    private Instrument[] instruments;

    // This is data held from when the simulation ran on this State.
    // Will be reset every time this State is revisited.
    private ArrayList<Float> errorValues;

    // All of the average error values calculated once the simulation moved on from this State
    private ArrayList<Float> averageErrorValues;

    // The average error value calculated from averageErrorValues
    private float currentAverageErrorValue;

    // A list of possible neighbours
    private ArrayList<State> neighbours;

    // Nodes that have not been explored have priority
    private boolean hasBeenExplored;

    // A check to ensure that error values have been added to this state
    private boolean hasBeenSimulated;

    public State (BPM bpm, Instrument[] instruments) {
        this.bpm = bpm;
        this.instruments = instruments;
        this.hasBeenExplored = false;
        this.hasBeenSimulated = false;
    }

    public void calculateCurrentAverageErrorValue() {
        if (hasBeenSimulated) {
            recalculateAverageErrorValues();

            // Calculating to total average error value
            currentAverageErrorValue = getAverage(averageErrorValues);
        }
    }

    public BPM getBpm() {
        return bpm;
    }

    public Instrument[] getInstruments() {
        return instruments;
    }

    // So in the JUP, they can delete a previous attempt but the system will include that
    // State in the next run
    public ArrayList<Float> getAverageErrorValues() {
        return averageErrorValues;
    }

    public float getCurrentAverageErrorValue() {
        return currentAverageErrorValue;
    }

    public ArrayList<State> getNeighbours() {
        return neighbours;
    }

    /* Helper method to calculate the average error value from the simulation */
    private void recalculateAverageErrorValues() {
        // Calculating the average
        float average = getAverage(errorValues);

        // Adding the averaged error value to the State's data
        this.averageErrorValues.add(average);

        // Resetting the values to be used again the next time this State is visited
        this.errorValues = null;
    }

    /* Helper method to calculate the average value in an ArrayList<Float> */
    private float getAverage(ArrayList<Float> values) {
        float average = 0;

        for (float value: values) {
            average += value;
        }

        average /= values.size();

        return average;
    }
}
