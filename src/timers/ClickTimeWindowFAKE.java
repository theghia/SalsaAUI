package timers;

import main.SalsaModel;

public class ClickTimeWindowFAKE implements Runnable {
    // This will only be repeated once
    int repeat = 1;

    // To update the trackers for the time window
    SalsaModel model;

    public ClickTimeWindowFAKE(SalsaModel model) {
        this.model = model;
    }

    @Override
    public synchronized void run() {
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
            System.out.println("Window Tracker b4 change: " + model.getWindowTracker());
            System.out.println("Button Clicker b4 change: " + model.getButtonClickerTracker());
            System.out.println("hasClickedOnce1 b4 change: " + model.hasClickedOnce1());
            System.out.println("hasClickedOnce2 b4 change: " + model.hasClickedOnce2());

            // Checking if there is a zero in the int array
            if (doesArrayContainZero(model.getWindowCache())) {
                if (model.getWindowCache()[0] > 0) {
                    model.setHasClickedOnce1(true);
                    model.getWindowCache()[0] = 0;
                    model.increaseButtonClickerTracker();
                    System.out.println("Button Clicker after change: " + model.getButtonClickerTracker());
                    System.out.println("Window Tracker after change: " + model.getWindowTracker());
                    System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                    System.out.println();
                } else if (model.getWindowCache()[1] > 0) {
                    model.setHasClickedOnce2(true);
                    model.getWindowCache()[1] = 0;
                    model.decreaseButtonClickerTracker();
                    System.out.println("Button Clicker after change: " + model.getButtonClickerTracker());
                    System.out.println("Window Tracker after change: " + model.getWindowTracker());
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
                    System.out.println("Button Clicker after change: " + model.getButtonClickerTracker());
                    System.out.println("Window Tracker after change: " + model.getWindowTracker());
                    System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                    System.out.println();
                } else if (model.getWindowCache()[0] > model.getWindowCache()[1]) {
                    model.setHasClickedOnce2(true);
                    model.getWindowCache()[1] = 0;
                    model.decreaseButtonClickerTracker();
                    System.out.println("Button Clicker after change: " + model.getButtonClickerTracker());
                    System.out.println("Window Tracker after change: " + model.getWindowTracker());
                    System.out.println("hasClickedOnce1 after change: " + model.hasClickedOnce1());
                    System.out.println("hasClickedOnce2 after change: " + model.hasClickedOnce2());
                    System.out.println();
                }
            }
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
