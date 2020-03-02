package timers;

import main.SalsaModel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ClickTimeWindow Class to be used by the ClickOnce Class so that there can be a time window around the current beat
 * in which the user can input their guess of the correct beat. Once an input has been recorded, no more inputs will
 * be regarded until the next time window.
 *
 * @author Gareth Iguasnia
 * @date 29/02/2020
 */
public class ClickTimeWindow {
    // Timer object to carry out a task repeatedly
    private Timer timer;

    // To be used to get the specific timeline of the beats
    private SalsaModel model;

    // To be used to create the time window in which we will validate a user's input as a try
    private final int TIMEWINDOW = 3;

    /**
     * Constructor for the CLickTimeWindow Class that will create the time window around the current beat. The window
     * will be TIMEWINDOW before and after the current beat.
     *
     * @param model SalsaModel object that will be used to get the trackers and beat timeline
     * @param beatSelected Integer representing the beat that the user needs to find in the music
     * @param barNumber Integer that represents the bar number in the 4 8-beat bar batch
     */
    public ClickTimeWindow(SalsaModel model, int beatSelected, int barNumber) {
        this.model = model;
        this.timer = new Timer();

        // -1 -> Since index starts at 0
        // + (8*(stateNumber - 1)) -> since each State comes with 4 8-beat bars of Salsa music
        int indexForInitialDelay = beatSelected - TIMEWINDOW - 1 + (8*(barNumber - 1));
        int indexForPeriod = beatSelected + TIMEWINDOW - 1 + (8*(barNumber - 1));

        System.out.println("Bar number: " + barNumber);
        System.out.println("Start window: " + model.getBeatTimeline().get(indexForInitialDelay));
        System.out.println("Index for start window: " + indexForInitialDelay);
        System.out.println("Index for the end window: " + indexForPeriod);
        System.out.println();
        // Initial delay is at the start of the window
        long initialDelay = model.getBeatTimeline().get(indexForInitialDelay);

        // Period is at the end of the window
        long period = model.getBeatTimeline().get(indexForPeriod) - initialDelay;

        timer.schedule(new AllowClick(),
                initialDelay,
                period);
    }

    /**
     * AllowClick Innerclass that extends TimerTask. This will be the logic that will be executed at the start of the
     * beat window and then at the end of the beat window.
     *
     * @author Gareth Iguasnia
     * @date 29/02/2020
     */
    class AllowClick extends TimerTask {
        // This will only be repeated once
        int repeat = 1;

        @Override
        public void run() {
            // This logic occurs at the start of the time window.
            if (repeat > 0) {
                // We want this to run only once
                repeat--;
                System.out.println("Window Tracker:  "  + model.getWindowTracker());

                // We will be using two flags as the time windows can overlap in some cases
                if ( model.getWindowTracker() == 1 ) {
                    model.setHasClickedOnce1(false);
                    model.increaseWindowTracker();
                }
                else if ( model.getWindowTracker() == 2 ) {
                    model.setHasClickedOnce2(false);
                    model.decreaseWindowTracker();
                }
            }

            // This is in case the user does not do any input. The time window will be closed and the
            // buttonClickerTracker will be updated accordingly for the button clicker
            else {
                if ( model.getButtonClickerTracker() == 1 ) {
                    model.setHasClickedOnce1(true);
                    model.increaseButtonClickerTracker();
                }
                else if ( model.getButtonClickerTracker() == 2 ) {
                    model.setHasClickedOnce2(true);
                    model.decreaseButtonClickerTracker();
                }
            }
        }
    }
}
