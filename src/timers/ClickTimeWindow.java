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
        System.out.println("End window: " + model.getBeatTimeline().get(indexForPeriod));
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
                System.out.println("Start of time window: " + System.currentTimeMillis());
                // We want this to run only once
                repeat--;
                System.out.println("Window Tracker b4 change: "  + model.getWindowTracker());
                System.out.println("Button Clicker b4 change: " + model.getButtonClickerTracker());
                System.out.println("hasClickedOnce1 b4 change: " + model.hasClickedOnce1());
                System.out.println("hasClickedOnce2 b4 change: " + model.hasClickedOnce2());
                // We will be using two flags as the time windows can overlap in some cases
                if ( model.getWindowTracker() == 1 ) {
                    model.setHasClickedOnce1(false);
                    model.getWindowCache()[0] = model.getWindowCache()[1] + 1;
                    model.increaseWindowTracker();
                    System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                    System.out.println("Button Clicker after change: " + model.getButtonClickerTracker());
                    System.out.println("hasClickedOnce1 after: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after: " + model.hasClickedOnce2());
                    System.out.println();

                }
                else if ( model.getWindowTracker() == 2 ) {
                    model.setHasClickedOnce2(false);
                    model.getWindowCache()[1] = model.getWindowCache()[0] + 1;
                    model.decreaseWindowTracker();
                    System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                    System.out.println("Button Clicker after change: " + model.getButtonClickerTracker());
                    System.out.println("hasClickedOnce1 after: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after: " + model.hasClickedOnce2());
                    System.out.println();
                }
            }

            // This is in case the user does not do any input. The time window will be closed and the
            // buttonClickerTracker will be updated accordingly for the button clicker
            else {
                System.out.println("End of time window: " + System.currentTimeMillis());
                System.out.println("Window Tracker b4 change: "  + model.getWindowTracker());
                System.out.println("Button Clicker b4 change: "  + model.getButtonClickerTracker());
                System.out.println("hasClickedOnce1 b4 change: " + model.hasClickedOnce1());
                System.out.println("hasClickedOnce2 b4 change: " + model.hasClickedOnce2());
                // The if and else if logic needs changing
                /*if ( model.getButtonClickerTracker() == 1 ) {
                    model.setHasClickedOnce1(true);
                    model.increaseButtonClickerTracker();
                    System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                    System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                    System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                    System.out.println();
                }
                else if ( model.getButtonClickerTracker() == 2 ) {
                    model.setHasClickedOnce2(true);
                    model.decreaseButtonClickerTracker();
                    System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                    System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                    System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                    System.out.println();
                }*/

                // THE CHANGE

                // Checking if there is a zero in the int array
                if (doesArrayContainZero(model.getWindowCache())) {
                    if (model.getWindowCache()[0] > 0) {
                        model.setHasClickedOnce1(true);
                        model.getWindowCache()[0] = 0;
                        model.increaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                        System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                        System.out.println();
                    }
                    else if (model.getWindowCache()[1] > 0) {
                        model.setHasClickedOnce2(true);
                        model.getWindowCache()[1] = 0;
                        model.decreaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                        System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                        System.out.println();
                    }
                }

                // There is no zero in the int array
                else {
                    // The first window was opened before the second window
                    if (model.getWindowCache()[0] < model.getWindowCache()[1]) {
                        model.setHasClickedOnce1(true);
                        model.getWindowCache()[0] = 0;
                        model.increaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                        System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                        System.out.println();
                    }
                    else if (model.getWindowCache()[0] > model.getWindowCache()[1]) {
                        model.setHasClickedOnce2(true);
                        model.getWindowCache()[1] = 0;
                        model.decreaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                        System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                        System.out.println();
                    }
                }
                // Cancelling the timer thread
                timer.cancel();
            }
        }

        private boolean doesArrayContainZero(int[] array) {
            boolean containZero = false;
            for (int check: array) {
                if (check == 0)
                    containZero = true;
            }
            return containZero;
        }
    }
}
