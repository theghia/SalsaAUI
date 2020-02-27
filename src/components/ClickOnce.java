package components;

import controllers.SimulationController;

import java.util.ArrayList;
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

        // Adding the length of clip123 to the timeAccumulation since the start of clipSalsa should be at time 0
        simCon.getSalsaModel().addToTimeAccumulated(clip123);

        // Adding a beat timeline so that the SimulationController can have the correct times that each beat occurs at
        simCon.getSalsaModel().setBeatTimeline(createBeatTimeline(clipSalsa));

        // This is when the 1 beat of the second bar will be
        long quarter = clipSalsa/4;

        // Slight buffer to allow the button to capture the one beat of each 8-beat bar - THIS MIGHT NEED ADJUSTING
        long buffer = quarter/1000;

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

        // Adding a beat timeline so that the SimulationController can have the correct times that each beat occurs at
        simCon.getSalsaModel().setBeatTimeline(createBeatTimeline(clipSalsa));

        // This is when the 1 beat of the second bar will be
        long quarter = clipSalsa/4;

        // Slight buffer to allow the button to capture the one beat of each 8-beat bar - THIS MIGHT NEED ADJUSTING
        long buffer = quarter/1000;

        timer.schedule(new RemindTask(),
                0,
                quarter - buffer);
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

        @Override
        public void run() {
            if (numBars > 0) {
                // Went through one 8-beat bar of music
                numBars--;

                // Allowing the system to record the timestamp of the user's input for the next 8-beat bar of music
                simCon.getSalsaModel().resetHasClickedOnce();

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

        ArrayList<Long> beatTimeline = new ArrayList<Long>(numBeatsPerState);

        // Beat 1 of a new group of 4 8-beat bars is 0
        long beatTime = 0;

        // The time each beat is spread out with
        long anEighth = quarter/8;

        // Incrementally adding anEighth to the beatTime to get the beat timeline
        for (int i = 0; i < numBeatsPerState; i++) {
            beatTimeline.add(beatTime);
            beatTime += anEighth;
        }
        return beatTimeline;
    }
}
