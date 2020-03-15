package timers;

import components.State;
import controllers.GameController;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
/**
 * GameProgress Class to be used by the GameController so that the events for a new state and new beat can be fired
 * at the correct times for the simulation to continue. This is also takes care of noting when the simulation has ended
 * and firing the appropriate event for it. Lastly, it also deals with limiting user input to 1 input per 8-beat bar
 * of music during the simulation
 *
 * @author Gareth Iguasnia
 * @date 27/02/2020
 */
public class GameProgress {
    // Timer object to carry out a task repeatedly
    private Timer timer;

    // In order for the RemindTask Class to have access to this data
    private GameController gameController;
    private long clipSalsa;

    // To keep track of the 8-beat bar number that we are currently on
    private int barNumber;

    // To create the next random beats that the simulation will request the user to identify in the music
    private Random randomGenerator;

    // The next 4 beats that the system will request the user to identify
    private ArrayList<Integer> nextBeats;

    // The first beat that the user will be tested on
    int currentBeat;

    /**
     * Constructor: This will be used throughout all of the simulation bar at the start of the simulation
     *
     * @param gameController A GameController object that will be used by this Class to access the model
     * @param clipSalsa The length of the Salsa audio clip in milliseconds
     */
    public GameProgress(GameController gameController, long clipSalsa) {
        this.timer = new Timer();
        this.gameController = gameController;
        this.clipSalsa = clipSalsa;
        this.randomGenerator = new Random();
        this.nextBeats = createNextBeats();

        // To correctly index the beats timeline
        this.barNumber = 1;
        this.gameController.getSalsaModel().setBarNumber(barNumber);

        // This is when the 1 beat of the second bar will be
        long quarter = clipSalsa/4;

        // Adding a beat timeline so that the SimulationController can have the correct times that each beat occurs at
        //System.out.println("The beat timeline is being created: " + System.currentTimeMillis());
        gameController.getSalsaModel().setBeatTimeline(createBeatTimeline(quarter));

        // Slight buffer to allow the button to capture the one beat of each 8-beat bar - THIS MIGHT NEED ADJUSTING
        long buffer = quarter/1000;

        // Record what the first beat in the group of 4 8-beat bars that the user will be tested on
        this.currentBeat = gameController.getSalsaModel().getNextBeat();

        timer.schedule(new RemindTask(),
                0,
                quarter);// + buffer);

        // This timestamp will be the new 0 for the next 4 8-beat bars
        gameController.getSalsaModel().setTimeAccumulation(System.currentTimeMillis());

        // The 4 time windows in which the user can click once are set up for all states except the 1st
        startWindows();
    }

    /**
     * RemindTask Innerclass that extends TimerTask. This will be the logic that will be executed after every 8-beat
     * bar of salsa music and will be called by the timer schedule
     *
     * @author Gareth Iguasnia
     * @date 27/02/2020
     */
    class RemindTask extends TimerTask {
        // The number of 8-beat bars per audio file being played
        int numBars = 4;

        /**
         * Run method that covers the start of the Salsa/Countdown audio clip to the end of the Salsa audio clip
         */
        @Override
        public void run() {
            if (numBars > 0) {
                // Went through one 8-beat bar of music
                numBars--;

                // Setting the next beat in the model
                gameController.getSalsaModel().setNextBeat(nextBeats.get(barNumber - 1));
                //System.out.println("The index of nextBeats in GameProgress: " + (barNumber-1));
                //System.out.println("Setting the next beat: " + nextBeats.get(barNumber - 1));

                // Next bar
                gameController.getSalsaModel().setBarNumber(barNumber);
                barNumber++;

                // An event is thrown here for onNewBeatEvent
                gameController.getSalsaModel().fireNewBeatEvent();
            }
            // We need to signal the system internally that we have reached the final 8-beat bar of the State
            else {
                // If the simulation has not finished yet
                if (gameController.getSalsaModel().getNumTransitionedStates() > 0) {
                    // We travelled to one State and must decrease the counter in the model
                    gameController.getSalsaModel().decreaseNumTransitionedStates();

                    // If there are error values recorded in the State, then calculate the total average error value
                    if (!gameController.getSalsaModel().getCurrentState().getErrorValues().isEmpty()) {
                        // The State has been explored since error values have been recorded
                        gameController.getSalsaModel().getCurrentState().setHasBeenExplored(true);

                        // Calculate the total average error value of the current state
                        calculateAverageErrorValue(gameController.getSalsaModel().getCurrentState());
                    }

                    System.out.println("The total average error value is: " +
                            gameController.getSalsaModel().getCurrentState().getCurrentAverageErrorValue());

                    // Logic to determine which new State to move onto next
                    State currentState = gameController.getSalsaModel().getCurrentState();
                    State newState = gameController.getGameStatusFunction().getNextState(currentState);

                    // Changing State depending on the input or lack of
                    gameController.getSalsaModel().setCurrentState(newState);

                    // The model fires an event to get the simulation started for the next State
                    gameController.getSalsaModel().fireNewStateEvent();
                }
                // If the simulation has finished
                else {
                    // Clean up the model to set it back to its default state
                    gameController.getSalsaModel().resetModel();

                    // We must end the simulation here and notify the other controllers working during the simulation
                    gameController.getSalsaModel().fireSimulationFinishedEvent();
                }
                // Terminate the timer thread
                timer.cancel();
            }
        }
    }

    /* Helper method to create a beat timeline for each beat in a group of 4 8-beat bars */
    private ArrayList<Long> createBeatTimeline(long quarter) {
        // Number of beats in a group of 4 8-beat bars
        int numBeatsPerState = 32;

        ArrayList<Long> beatTimeline = new ArrayList<>(numBeatsPerState);

        // Beat 1 of a new group of 4 8-beat bars is 0
        long beatTime = 0;

        // The time each beat is spread out with
        long anEighth = quarter/8;

        // Incrementally adding anEighth to the beatTime to get the beat timeline
        for (int i = 0; i < numBeatsPerState; i++) {
            beatTimeline.add(beatTime);
            beatTime += anEighth;
        }
        System.out.println(beatTimeline);
        //System.out.println("The beat timeline has been created: " + System.currentTimeMillis());

        return beatTimeline;
    }

    /* The next beats that the simulation will request the user to identify in the music */
    private ArrayList<Integer> createNextBeats() {
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

    /* Helper method to initiate the time windows */
    private void startWindows() {
        System.out.println("The time windows are being setup: " + System.currentTimeMillis());
        new TimeWindow(gameController.getSalsaModel(), currentBeat, 1 );
        new TimeWindow(gameController.getSalsaModel(), nextBeats.get(0), 2);
        new TimeWindow(gameController.getSalsaModel(), nextBeats.get(1), 3);
        new TimeWindow(gameController.getSalsaModel(), nextBeats.get(2), 4);
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
