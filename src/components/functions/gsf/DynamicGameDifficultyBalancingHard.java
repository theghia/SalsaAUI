package components.functions.gsf;

import components.enums.State;

import java.util.ArrayList;

public class DynamicGameDifficultyBalancingHard extends DynamicGameDifficultyBalancing {

    @Override
    public void sortHardAndExplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // Error values closer to 0 means that the user did not perform well - so a harder state for them
        if (potentialNeighbour.getCurrentAverageErrorValue() < currentState.getCurrentAverageErrorValue())
            sorted.add(potentialNeighbour);
    }

    @Override
    public void sortHardAndUnexplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // What determines a harder state?

        // A faster tempo
        if (potentialNeighbour.getBpm().getBPM() > currentState.getBpm().getBPM())
            sorted.add(potentialNeighbour);

        // More instruments playing
        else if (potentialNeighbour.getInstruments().size() > currentState.getInstruments().size())
            sorted.add(potentialNeighbour);

        // Switching out instruments
        else if (potentialNeighbour.getInstruments().size() == currentState.getInstruments().size()
        && potentialNeighbour.getBpm().getBPM() == currentState.getBpm().getBPM())
            sorted.add(potentialNeighbour);
    }

    @Override
    public void sortEasyAndExplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // Error values closer to 1 means that the user performed very well - so easier state for them
        if(potentialNeighbour.getCurrentAverageErrorValue() >= currentState.getCurrentAverageErrorValue())
            sorted.add(potentialNeighbour);
    }

    @Override
    public void sortEasyAndUnexplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // What determines an easier state?

        // Less instruments playing
        if (potentialNeighbour.getInstruments().size() < currentState.getInstruments().size())
            sorted.add(potentialNeighbour);

        // Slower tempo
        else if (potentialNeighbour.getBpm().getBPM() < currentState.getBpm().getBPM())
            sorted.add(potentialNeighbour);
    }
}
