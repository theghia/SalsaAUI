package components.functions.gsf;

import components.enums.BPM;
import components.State;

import java.util.ArrayList;

public class DGDBEasy extends DynamicGameDifficultyBalancing {
    @Override
    public void sortHardAndExplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // Error values closer to 0 means that the user did not perform well - so a harder state for them
        if (potentialNeighbour.getCurrentAverageErrorValue() < currentState.getCurrentAverageErrorValue()
                && potentialNeighbour.getBpm().equals(BPM.SLOW) // Only want the slow tempos
                && (potentialNeighbour.getInstruments().size() <= 2)) // Only want single or two instruments
            sorted.add(potentialNeighbour);
    }

    @Override
    public void sortHardAndUnexplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // What determines a harder state?

        // More instruments playing (There are only 2 options in this mode - 1/2)
        if (potentialNeighbour.getInstruments().size() > currentState.getInstruments().size())
            sorted.add(potentialNeighbour);

        // Switching out instruments
        else if (potentialNeighbour.getInstruments().size() == currentState.getInstruments().size()
                && potentialNeighbour.getBpm().getBPM() == currentState.getBpm().getBPM())
            sorted.add(potentialNeighbour);
    }

    @Override
    public void sortEasyAndExplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // Error values closer to 1 means that the user performed very well - so easier state for them
        if(potentialNeighbour.getCurrentAverageErrorValue() >= currentState.getCurrentAverageErrorValue()
                && potentialNeighbour.getBpm().equals(BPM.SLOW) // Only want the slow tempos
                && (potentialNeighbour.getInstruments().size() <= 2)) // Only want single or two instruments
            sorted.add(potentialNeighbour);
    }

    @Override
    public void sortEasyAndUnexplored(ArrayList<State> sorted, State potentialNeighbour, State currentState) {
        // What determines an easier state?

        // Less instruments playing
        if (potentialNeighbour.getInstruments().size() < currentState.getInstruments().size())
            sorted.add(potentialNeighbour);
    }
}
