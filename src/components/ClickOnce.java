package components;

import controllers.SimulationController;
import main.SalsaModel;

import java.util.Timer;
import java.util.TimerTask;

public class ClickOnce {
    Timer timer;

    public ClickOnce(SimulationController simCon, long clip123, long clipSalsa) {
        timer = new Timer();
    }

    public ClickOnce(SimulationController simCon, long clipSalsa) {
        timer = new Timer();
    }

    class RemindTask extends TimerTask {
        // The number of 8-beat bars per audio file being played
        int numBars = 4;
        @Override
        public void run() {
            if (numBars > 0) {
                //do something just before the beat 1 of the next bar
                numBars--;
            }
            // This is just the end of the clip playing - throw the event to start
            // the preparations for the next state - hold a flag in the model
            // where you can see if the simulation is still running, other wise, just close the timer
            // we need to add some logic where after a certain amount of 4 8-beat bars the simulation
            // stops could be done during
            // this should be done in the initStartButton and every time it hits the else when passing
            // the if of the simulationRunning being true which you can access through the controller
            else {
                // model.firesNewStateEvent()

            }

        }
    }
}
