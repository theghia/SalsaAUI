package components.ingame;

import main.SalsaModel;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * TimeWindow Class to be used by the GameProgress Class so that there can be a time window around the current beat
 * in which the user can input their guess of the correct beat. Once an input has been recorded, no more inputs will
 * be regarded until the next time window.
 *
 * @author Gareth Iguasnia
 * @date 29/02/2020
 */
public class TimeWindow {
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
    public TimeWindow(SalsaModel model, int beatSelected, int barNumber) {
        this.model = model;
        this.timer = new Timer();

        // -1 -> Since index starts at 0
        // + (8*(stateNumber - 1)) -> since each State comes with 4 8-beat bars of Salsa music
        int indexForInitialDelay = beatSelected - TIMEWINDOW - 1 + (8*(barNumber - 1));
        int indexForPeriod = beatSelected + TIMEWINDOW - 1 + (8*(barNumber - 1));

        // Allowing the time window to be shortened
        if (indexForInitialDelay < 0)
            indexForInitialDelay = 0;
        if (indexForPeriod > 31)
            indexForPeriod = 31;

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
                    // To open Time Window 1
                    model.setHasClickedOnce1(false);
                    int updateWindow = model.getWindowCaching().get(1) + 1;
                    model.getWindowCaching().set(0, updateWindow);

                    // To get the beat that pertains to the current open window/s
                    model.getBeatCaching().set(0, model.getTestingBeats().get(model.getBeatCacheTracker()));
                    model.increaseBeatCacheTracker();

                    // To get the bar that pertains to the current open window/s
                    if (model.getBarCaching().get(0) == 0)
                        model.getBarCaching().set(0, 1);
                    else if (model.getBarCaching().get(0) == 1)
                        model.getBarCaching().set(0, 3);

                    // The next window to be opened is Time Window 2
                    model.increaseWindowTracker();

                    System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                    System.out.println("Button Clicker after change: " + model.getButtonClickerTracker());
                    System.out.println("hasClickedOnce1 after: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after: " + model.hasClickedOnce2());
                    System.out.println();

                }
                else if ( model.getWindowTracker() == 2 ) {
                    // To open Time Window 2
                    model.setHasClickedOnce2(false);
                    int updateWindow = model.getWindowCaching().get(0) + 1;
                    model.getWindowCaching().set(1, updateWindow);

                    // To get the beat that pertains to the current open window/s
                    model.getBeatCaching().set(1, model.getTestingBeats().get(model.getBeatCacheTracker()));
                    model.increaseBeatCacheTracker();

                    // To get the bar that pertains to the current open window/s
                    if (model.getBarCaching().get(1) == 0)
                        model.getBarCaching().set(1, 2);
                    else if (model.getBarCaching().get(1) == 2)
                        model.getBarCaching().set(1, 4);

                    // The next window to be opened is Time Window 1
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

                // Checking if there is a zero in the int array
                if (doesArrayContainZero(model.getWindowCaching())) {
                    if (model.getWindowCaching().get(0) > 0) {
                        // Closing Time Window 1
                        model.setHasClickedOnce1(true);

                        // Resetting the cache
                        model.getWindowCaching().set(0, 0);
                        model.getBeatCaching().set(0, 0);
                        if (model.getBarCaching().get(0) == 3)
                            model.getBarCaching().set(0, 0);

                        // Allowing the system to take an input from Time Window 2
                        model.increaseButtonClickerTracker();

                        System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                        System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                        System.out.println();
                    }
                    else if (model.getWindowCaching().get(1) > 0) {
                        // Closing Time Window 2
                        model.setHasClickedOnce2(true);

                        // Resetting the cache
                        model.getWindowCaching().set(1, 0);
                        model.getBeatCaching().set(1, 0);
                        if (model.getBarCaching().get(1) == 4)
                            model.getBarCaching().set(1, 0);

                        // Allowing the system to take an input from Time Window 1
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
                    if (model.getWindowCaching().get(0) < model.getWindowCaching().get(1)) {
                        // Closing Time Window 1
                        model.setHasClickedOnce1(true);

                        // Resetting the cache
                        model.getWindowCaching().set(0, 0);
                        model.getBeatCaching().set(0, 0);
                        if (model.getBarCaching().get(0) == 3)
                            model.getBarCaching().set(0, 0);

                        // Allowing the system to take an input from Time Window 2
                        model.increaseButtonClickerTracker();

                        System.out.println("Button Clicker after change: "  + model.getButtonClickerTracker());
                        System.out.println("Window Tracker after change: "  + model.getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                        System.out.println();
                    }
                    else if (model.getWindowCaching().get(0) > model.getWindowCaching().get(1)) {
                        // Closing Time Window 2
                        model.setHasClickedOnce2(true);

                        // Resetting the cache
                        model.getWindowCaching().set(1, 0);
                        model.getBeatCaching().set(1, 0);
                        if (model.getBarCaching().get(1) == 4)
                            model.getBarCaching().set(1, 0);

                        // Allowing the system to take an input from Time Window 1
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

        private boolean doesArrayContainZero(AtomicIntegerArray arr) {
            boolean containZero = false;
            for (int i = 0; i < 2; i++) {
                if (arr.get(i) == 0)
                    containZero = true;
            }
            return containZero;
        }
    }
}
