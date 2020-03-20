package components.gui;

import views.GameView;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Countdown class that will be used to display the numbers that are in the countdown clip being played.
 *
 * @author Gareth Iguasnia
 * @date 13/03/2020
 */
public class Countdown {

    private ScheduledExecutorService scheduledExecutorService;
    private GameView gameView;

    /**
     * Constructor should have reference to the view to display the numbers accordingly
     *
     * @param gameView GameView object that is the view that the GUIController will work on
     */
    public Countdown(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Method starts the process of the numbers being shown when called out in the countdown clip. The timings have
     * been manually calculated using Audacity
     */
    public void countdownGUIStart() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        //1400 -> Get the length of the clip
        scheduledExecutorService.scheduleAtFixedRate(new CountdownNumbers(), 1400,
                1200, TimeUnit.MILLISECONDS );
    }

    /**
     * CountdownNumbers innerclass to be used as a thread by the Executor in the Countdown class. This class will
     * display the number according to the value numberToDisplay
     *
     * @author Gareth Iguasnia
     * @date 13/03/2020
     */
    private class CountdownNumbers implements Runnable {

        // Number 5 is displayed first once the JPanel numberCountdown is set to visible
        private int numberToDisplay = 4;

        @Override
        public void run() {
            System.out.println("One run about to happen");
            if( numberToDisplay > -1) {
                CardLayout cardLayout = (CardLayout) gameView.getNumberCountdown().getLayout();
                // Displaying the correct number
                cardLayout.show(gameView.getNumberCountdown(), Integer.toString(numberToDisplay));
                // The next number will be one less than the current number
                numberToDisplay--;
            }
            else {
                // Set the numberCountdown invisible -> This will also be done by the onCountdownFinishedEvent
                gameView.getNumberCountdown().setVisible(false);

                // Set the number 5 at the front
                CardLayout cardLayout = (CardLayout) gameView.getNumberCountdown().getLayout();
                cardLayout.show(gameView.getNumberCountdown(), "5");

                // Stop the executor
                scheduledExecutorService.shutdown();
            }

        }
    }
}
