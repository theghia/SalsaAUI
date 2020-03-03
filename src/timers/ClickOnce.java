package timers;

import components.State;
import controllers.SimulationController;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ClickOnce Class to be used by the SimulationController so that the events for a new state and new beat can be fired
 * at the correct times for the simulation to continue. This is also takes care of noting when the simulation has ended
 * and firing the appropriate event for it. Lastly, it also deals with limiting user input to 1 input per 8-beat bar
 * of music during the simulation
 *
 * @author Gareth Iguasnia
 * @date 27/02/2020
 */
public class ClickOnce {
    // Timer object to carry out a task repeatedly
    private Timer timer;

    // In order for the RemindTask Class to have access to this data
    private SimulationController simCon;
    private long clipSalsa;

    // To keep track of the 8-beat bar number that we are currently on
    private int barNumber;

    // To create the next random beats that the simulation will request the user to identify in the music
    private Random randomGenerator;

    // The next 4 beats that the system will request the user to identify
    private ArrayList<Integer> nextBeats;

    /**
     * Constructor 1: This will be used at the start of the simulation to take into account the countdown clip that will
     * be played at the start of the simulation
     *
     * @param simCon A SimulationController object that will be used by this Class to access the model
     * @param clip123 The length of the countdown clip in milliseconds
     * @param clipSalsa The length of the Salsa audio clip in milliseconds
     */
    public ClickOnce(SimulationController simCon, long clip123, long clipSalsa) {
        this.timer = new Timer();
        this.simCon = simCon;
        this.clipSalsa = clipSalsa;
        this.randomGenerator = new Random();
        this.nextBeats = createNextBeats();

        // To correctly index the beats timeline
        this.barNumber = 1;
        this.simCon.getSalsaModel().setBarNumber(barNumber);


        // Adding the length of clip123 to the timeAccumulation since the start of clipSalsa should be at time 0
        simCon.getSalsaModel().addToTimeAccumulated(clip123);

        // This is when the 1 beat of the second bar will be
        long quarter = clipSalsa/4;

        // Adding a beat timeline so that the SimulationController can have the correct times that each beat occurs at
        simCon.getSalsaModel().setBeatTimeline(createBeatTimeline(quarter));

        // Slight buffer to allow the button to capture the one beat of each 8-beat bar - THIS MIGHT NEED ADJUSTING
        long buffer = quarter/1000;

        // Start the event for the SimulationGUIController to display the countdown numbers
        simCon.getSalsaModel().fireCountdownStartedEvent(clip123, clipSalsa);

        timer.schedule(new RemindTask(),
                clip123 - buffer, // Initial delay as we are first playing the countdown clip
                quarter - buffer); // This will happen just before beat 1 of every 8-beat bar
    }

    /**
     * Constructor 2: This will be used throughout all of the simulation bar at the start of the simulation
     *
     * @param simCon A SimulationController object that will be used by this Class to access the model
     * @param clipSalsa The length of the Salsa audio clip in milliseconds
     */
    public ClickOnce(SimulationController simCon, long clipSalsa) {
        this.timer = new Timer();
        this.simCon = simCon;
        this.clipSalsa = clipSalsa;
        this.randomGenerator = new Random();
        this.nextBeats = createNextBeats();

        // To correctly index the beats timeline
        this.barNumber = 1;
        this.simCon.getSalsaModel().setBarNumber(barNumber);

        // This is when the 1 beat of the second bar will be
        long quarter = clipSalsa/4;

        // Adding a beat timeline so that the SimulationController can have the correct times that each beat occurs at
        simCon.getSalsaModel().setBeatTimeline(createBeatTimeline(quarter));

        // Slight buffer to allow the button to capture the one beat of each 8-beat bar - THIS MIGHT NEED ADJUSTING
        long buffer = quarter/1000;

        timer.schedule(new RemindTask(),
                0,
                quarter - buffer);

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
            if (simCon.getSalsaModel().isCountdownCurrentlyPlaying()) {
                // So that the button clicker can now be taking the input of the user
                simCon.getSalsaModel().setCountdownCurrentlyPlaying(false);

                // The 4 time windows, in which the user can try to find the requested beat, are started up for the 1st State
                startWindows();

                // Notify listeners that the countdown has finished
                simCon.getSalsaModel().fireCountdownFinishedEvent();
            }
            if (numBars > 0) {
                // Went through one 8-beat bar of music
                numBars--;

                // Setting the next beat in the model
                simCon.getSalsaModel().setNextBeat(nextBeats.get(barNumber - 1));
                System.out.println("The index of nextBeats in ClickOnce: " + (barNumber-1));
                System.out.println("Setting the next beat: " + nextBeats.get(barNumber - 1));

                // Next bar
                simCon.getSalsaModel().setBarNumber(barNumber);
                barNumber++;

                // An event is thrown here for onNewBeatEvent
                simCon.getSalsaModel().fireNewBeatEvent();
            }
            // We need to signal the system internally that we have reached the final 8-beat bar of the State
            else {
                // If the simulation has not finished yet
                if (simCon.getSalsaModel().getNumTransitionedStates() > 0) {
                    // We travelled to one State and must decrease the counter in the model
                    simCon.getSalsaModel().decreaseNumTransitionedStates();

                    // We add the length of this Salsa audio clip to timeAccumulation
                    simCon.getSalsaModel().addToTimeAccumulated(clipSalsa);

                    // Logic to determine which new State to move onto next
                    State currentState = simCon.getSalsaModel().getCurrentState();
                    State newState = simCon.getGameStatusFunction().getNextState(currentState);

                    // If there are error values recorded in the State, then calculate the total average error value
                    if (!currentState.getErrorValues().isEmpty()) {
                        // The State has been explored since error values have been recorded
                        simCon.getSalsaModel().getCurrentState().setHasBeenExplored(true);

                        // Calculate the total average error value of the current state
                        calculateAverageErrorValue(simCon.getSalsaModel().getCurrentState());
                    }

                    System.out.println("The total average error value is: " + simCon.getSalsaModel().getCurrentState().getCurrentAverageErrorValue());

                    // Changing State depending on the input or lack of
                    simCon.getSalsaModel().setCurrentState(newState);

                    // The model fires an event to get the simulation started for the next State
                    simCon.getSalsaModel().fireNewStateEvent();
                }
                // If the simulation has finished
                else {
                    // We must end the simulation here and notify the other controllers working during the simulation
                    simCon.getSalsaModel().fireSimulationFinishedEvent();

                    // Clean up the model to set it back to its default state
                    simCon.getSalsaModel().resetModel();
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
        new ClickTimeWindow(simCon.getSalsaModel(), simCon.getSalsaModel().getNextBeat(), 1 );
        new ClickTimeWindow(simCon.getSalsaModel(), nextBeats.get(0), 2);
        new ClickTimeWindow(simCon.getSalsaModel(), nextBeats.get(1), 3);
        new ClickTimeWindow(simCon.getSalsaModel(), nextBeats.get(2), 4);
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
