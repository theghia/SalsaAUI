package components.ingame.levels;

import components.State;
import components.ingame.GameProgress;
import controllers.GameController;
import controllers.simulation.easy.EasySimulationController;

import java.util.ArrayList;
import java.util.Random;

public class EasyProgress extends GameProgress {

    private EasySimulationController easySimulationController;
    /**
     * Constructor: This will be used throughout all of the simulation bar at the start of the simulation
     *
     * @param gameController A GameController object that will be used by this Class to access the model
     * @param clipSalsa      The length of the Salsa audio clip in milliseconds
     */
    public EasyProgress(GameController gameController, long clipSalsa) {
        super(gameController, clipSalsa);
        this.easySimulationController = (EasySimulationController) gameController;
    }

    @Override
    public ArrayList<Integer> createNextBeats(Random randomGenerator) {
        // ArrayList for the next 4 beats in the simulation
        ArrayList<Integer> nextBeats = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            int sameBeat = getGameController().getSalsaModel().getNextBeat();

            // 2nd, 3rd and 4th bar of the 4 8-beat bar Salsa audio clip
            if (!(i == 3))
                nextBeats.add(sameBeat);

                // 1st bar of the next 4 8-beat bar Salsa audio clip
            else {
                int nextBeat = randomGenerator.nextInt(8) + 1;
                nextBeats.add(nextBeat);
            }
        }
        System.out.println(nextBeats);
        return nextBeats;
    }

    @Override
    public void stateTransitionBehaviour(State currentState) {
        // Checking if we can move on to a new State
        if (this.easySimulationController.isCorrectAttempt()) {
            // Resetting the flag
            this.easySimulationController.setCorrectAttempt(false);

            // Choosing the next state
            State newState = this.easySimulationController.getGameStatusFunction().getNextState(currentState);

            // Changing State depending on the input or lack of
            this.easySimulationController.getSalsaModel().setCurrentState(newState);
        }
        // The model fires an event to get the simulation started for the next State
        easySimulationController.getSalsaModel().fireEasyNewStateEvent();
    }

    @Override
    public void newBeat() {
        getGameController().getSalsaModel().fireEasyNewBeatEvent();
    }

    @Override
    public void gameFinished() {
        getGameController().getSalsaModel().fireSimulationFinishedEvent();
    }
}
