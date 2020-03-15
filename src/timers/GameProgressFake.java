package timers;

import components.GameStatusFunction;
import components.State;
import controllers.GameController;
import main.SalsaModel;

import java.util.ArrayList;

public class GameProgressFake implements Runnable {
    // The number of 8-beat bars per audio file being played
    int barNumber;

    // To get access to the model, game status function and nextBeats
    GameController gameController;

    // Access to the model to update the State and requested beats
    SalsaModel model;

    // Access to the GameStatusFunction
    GameStatusFunction gameStatusFunction;

    // Access to the next beats of the system
    private ArrayList<Integer> nextBeats;

    public GameProgressFake(GameController gameController) {
        this.gameController = gameController;
        this.model = gameController.getSalsaModel();
        this.gameStatusFunction = gameController.getGameStatusFunction();
        this.nextBeats = gameController.getNextBeats();
        this.barNumber = 1;
    }

    @Override
    public void run() {
        System.out.println("Bar number is: " + barNumber);
        if (barNumber < 5) {
            // Setting the next beat in the model
            model.setNextBeat(nextBeats.get(barNumber - 1));
            //System.out.println("The index of nextBeats in GameProgress: " + (barNumber-1));
            //System.out.println("Setting the next beat: " + nextBeats.get(barNumber - 1));

            // Next bar
            model.setBarNumber(barNumber);

            // Went through one 8-beat bar of music
            barNumber++;

            // An event is thrown here for onNewBeatEvent
            model.fireNewBeatEvent();
        }
        // We need to signal the system internally that we have reached the final 8-beat bar of the State
        else {
            // If the simulation has not finished yet
            if (model.getNumTransitionedStates() > 0) {
                // We travelled to one State and must decrease the counter in the model
                model.decreaseNumTransitionedStates();

                // If there are error values recorded in the State, then calculate the total average error value
                if (!model.getCurrentState().getErrorValues().isEmpty()) {
                    // The State has been explored since error values have been recorded
                    model.getCurrentState().setHasBeenExplored(true);

                    // Calculate the total average error value of the current state
                    calculateAverageErrorValue(model.getCurrentState());
                }

                System.out.println("The total average error value is: " +
                        model.getCurrentState().getCurrentAverageErrorValue());

                // Logic to determine which new State to move onto next
                State currentState = model.getCurrentState();
                State newState = gameStatusFunction.getNextState(currentState);

                // Changing State depending on the input or lack of
                model.setCurrentState(newState);

                // The model fires an event to get the simulation started for the next State
                model.fireNewStateEvent();
                System.out.println("FIRING NEW STATE?");
            }
            // If the simulation has finished
            else {
                // Clean up the model to set it back to its default state
                model.resetModel();

                // Shutdown the executor
                gameController.getScheduledExecutorService().shutdown();

                // We must end the simulation here and notify the other controllers working during the simulation
                model.fireSimulationFinishedEvent();
            }

        }
    }

    /* Helper method to calculate the average value in an ArrayList<Float> */
    private double getAverage(ArrayList<Double> values) {
        double average = 0;

        for (double value: values) {
            average += value;
        }
        average /= values.size();

        return average;
    }

    /* Helper method to calculate the average error value from the simulation */
    private void calculateAverageErrorValue(State currentState) {
        // Calculating the average
        double average = getAverage(currentState.getErrorValues());

        // Adding the averaged error value to the State's data
        currentState.getAverageErrorValues().add(average);

        // Resetting the values to be used again the next time this State is visited
        currentState.resetErrorValue();

        // Calculating the total average error value
        double totalAverage = getAverage(currentState.getAverageErrorValues());
        currentState.setCurrentAverageErrorValue(totalAverage);
    }
}
