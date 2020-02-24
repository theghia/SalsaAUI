package components;

import java.util.ArrayList;
import java.util.Random;

/**
 * DynamicGameDifficultyBalancing Class that implements the GameStatusFunction interface as this class' objective is
 * to determine what State object should be visited next while the simulation is running to keep the user engaging
 * with the system.
 *
 * @author Gareth Iguasnia
 * @date 20/02/2020
 */
public class DynamicGameDifficultyBalancing implements GameStatusFunction {

    // This is the threshold that the user's error value needs to surpass in order for the difficulty to be increased
    private double THRESHOLD = 0.65;

    // In this design, we only choose a state that has been deemed as easier/harder.
    // If there is a choice, then we randomly choose. This behaviour can be modified by other GameStatusFunctions
    private Random randomGenerator;

    /**
     * Constructor for the DynamicGameDifficultyBalancing Class
     */
    public DynamicGameDifficultyBalancing() {
        randomGenerator = new Random();
    }

    @Override
    public State getNextState(State currentState) {
        // Getting the neighbouring states
        ArrayList<State> neighbours = currentState.getNeighbours();

        // The next State that will be decided in this logic
        State nextState = null;

        // To be used if there is no average error value associated to the current state
        ArrayList<State> unexploredNeighbours = sortNeighbours(neighbours, currentState, 5);
        ArrayList<State> exploredNeighbours = sortNeighbours(neighbours, currentState, 6);

        // To be used if the user needs to be challenged
        ArrayList<State> harderExploredNeighbours = sortNeighbours(neighbours, currentState, 1);
        ArrayList<State> harderUnexploredNeighbours = sortNeighbours(neighbours, currentState, 2);

        // To be used if the user has struggled on the current state
        ArrayList<State> easierExploredNeighbours = sortNeighbours(neighbours, currentState, 3);
        ArrayList<State> easierUnexploredNeighbours = sortNeighbours(neighbours, currentState, 4);

        // If there are no error values recorded for this state - hence not explored by our definition
        if (!currentState.hasBeenExplored()) {
            // If there are unexplored State objects - they take priority
            if (!unexploredNeighbours.isEmpty())
                nextState = chooseRandomState(unexploredNeighbours);

            // If no unexplored State objects, then just randomly move onto a new State
            else {
                nextState = chooseRandomState(exploredNeighbours);
            }
        }
        // There are some error values associated to this state
        else {
            // We need to move to a harder state
            if (currentState.getCurrentAverageErrorValue() > THRESHOLD) {

                if (!harderUnexploredNeighbours.isEmpty())
                    // Unexplored State objects have priority
                    nextState = chooseRandomState(harderUnexploredNeighbours);

                // If all neighbouring State objects have been explored, then choose a harder, explored State object
                else if (!harderExploredNeighbours.isEmpty())
                    nextState = chooseRandomState(harderExploredNeighbours);

                // If all neighbouring states are not harder than the current State, then see if there are any
                // easier unexplored State objects
                else if (!easierUnexploredNeighbours.isEmpty())
                    nextState = chooseRandomState(easierUnexploredNeighbours);

                // If we reach this block of code, then it means that all neighbouring states have been visited and
                // are all easier than the current state. Pick the state with the lowest average error value?
                else if (!easierExploredNeighbours.isEmpty())
                    nextState = chooseRandomState(easierExploredNeighbours);
            }

            // We need to move to an easier/same level state
            else {

                if (!easierUnexploredNeighbours.isEmpty())
                    // Unexplored State objects have priority
                    nextState = chooseRandomState(easierUnexploredNeighbours);

                // If all easier neighbouring states have been explored, then choose an easier explored state
                else if (!easierExploredNeighbours.isEmpty())
                    nextState = chooseRandomState(easierExploredNeighbours);

                // If all neighbouring states are not easier, then try to choose a "harder", unexplored state
                else if (!harderUnexploredNeighbours.isEmpty())
                    nextState = chooseRandomState(harderUnexploredNeighbours);

                // No -> No-> N0 -> Pick the state with the highest average error value?
                else if (!harderExploredNeighbours.isEmpty())
                    nextState = chooseRandomState(harderExploredNeighbours);

            }
        }
        return nextState;
    }

    /* Helper method to randomly returns a State object from an ArrayList of State objects */
    private State chooseRandomState(ArrayList<State> possibleCandidates) {
        // Randomly choosing an index number
        int rndIndex = this.randomGenerator.nextInt(possibleCandidates.size());

        return possibleCandidates.get(rndIndex);
    }

    /*
    * Helper method sorts the neighbouring states into a list of easier/harder and/or un/explored states
    * Code:
    *   1 - Returns an ArrayList of Harder, Explored State objects
    *   2 - Returns an ArrayList of Harder, Unexplored State objects
    *   3 - Returns an ArrayList of Easier/Same level, Explored State objects
    *   4 - Returns an ArrayList of Easier, Unexplored State objects
    *   5 - Returns an ArrayList of Unexplored State objects
    *   6 - Returns an ArrayList of Explored State objects
    */
    private ArrayList<State> sortNeighbours(ArrayList<State> neighbours, State currentState, int code) {
        ArrayList<State> sorted = new ArrayList<State>();

        for (State state: neighbours) {
            // Harder, explored
            if (code == 1 && state.hasBeenExplored()) {
                // Error values closer to 0 means that the user did not perform well - so a harder state for them
                if (state.getCurrentAverageErrorValue() < currentState.getCurrentAverageErrorValue())
                    sorted.add(state);
            }
            // Harder, unexplored states
            else if (code == 2 && !state.hasBeenExplored()) {
                // What determines a harder state?

                // A faster tempo
                if (state.getBpm().getBPM() > currentState.getBpm().getBPM())
                    sorted.add(state);

                // More instruments playing
                else if (state.getInstruments().size() > currentState.getInstruments().size())
                    sorted.add(state);

                // Switching out instruments
                else if (state.getInstruments().size() == currentState.getInstruments().size()
                && state.getBpm().getBPM() == currentState.getBpm().getBPM())
                    sorted.add(state);
            }
            // Easier/Same level, explored states
            else if (code == 3 && state.hasBeenExplored()) {
                // Error values closer to 1 means that the user performed very well - so easier state for them
                if(state.getCurrentAverageErrorValue() >= currentState.getCurrentAverageErrorValue())
                    sorted.add(state);
            }
            // Easier, unexplored states
            else if (code == 4 && !state.hasBeenExplored()) {
                // What determines an easier state?

                // Less instruments playing
                if (state.getInstruments().size() < currentState.getInstruments().size())
                    sorted.add(state);

                // Slower tempo
                else if (state.getBpm().getBPM() < currentState.getBpm().getBPM())
                    sorted.add(state);
            }

            // Unexplored states
            else if (code == 5 && !state.hasBeenExplored())
                sorted.add(state);

            // Explored states
            else if (code == 6 && state.hasBeenExplored())
                sorted.add(state);
        }
        return sorted;
    }
}
