package components.ingame.levels;

import components.enums.State;
import components.ingame.GameProgress;
import controllers.GameController;
import controllers.simulation.hard.HardSimulationController;

import java.util.ArrayList;
import java.util.Random;

public class HardProgress extends GameProgress {

    private HardSimulationController hardSimulationController;

    /**
     * Constructor: This will be used throughout all of the simulation bar at the start of the simulation
     *
     * @param gameController A GameController object that will be used by this Class to access the model
     * @param clipSalsa      The length of the Salsa audio clip in milliseconds
     */
    public HardProgress(GameController gameController, long clipSalsa) {
        super(gameController, clipSalsa);
        this.hardSimulationController = (HardSimulationController) gameController;
    }

    @Override
    public ArrayList<Integer> createNextBeats(Random randomGenerator) {
        // ArrayList for the next 4 beats in the simulation
        ArrayList<Integer> nextBeats = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            // 2nd and 3rd bar of the 4 8-beat bar Salsa audio clip
            if (i == 0 || i == 1) {
                int nextBeat = randomGenerator.nextInt(8) + 1;
                nextBeats.add(nextBeat);
            }

            // 4th bar of the 4 8-beat bar Salsa audio clip
            else if (i == 2) {
                int nextBeat = randomGenerator.nextInt(5) + 1;
                nextBeats.add(nextBeat);
            }

            // 1st bar of the next 4 8-beat bar Salsa audio clip
            else {
                int nextBeat = randomGenerator.nextInt(5) + 4;
                nextBeats.add(nextBeat);
            }
        }
        System.out.println(nextBeats);
        return nextBeats;
    }

    @Override
    public void stateTransitionBehaviour(State currentState) {
        State newState = hardSimulationController.getGameStatusFunction().getNextState(currentState); // NEED TO ADAPT THE DGDB FUNCTION

        // Changing State depending on the input or lack of
        hardSimulationController.getSalsaModel().setCurrentState(newState);

        // The model fires an event to get the simulation started for the next State
        hardSimulationController.getSalsaModel().fireNewStateEvent(); // This should be in an ABSTRACT METHOD so that
        // you can add the pause before the next state for the beginners
    }
}
