package components.gui;

import views.GameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LightSwitch {
    // The time in between each beat
    private long one_beat;

    // To turn on and off the lights at a specific time
    private ScheduledExecutorService scheduledExecutorService;

    // To access the lights
    private GameView gameView;

    public LightSwitch(GameView gameView, long one_beat) {
        this.gameView = gameView;
        this.one_beat = one_beat;
    }

    public void lightSwitchStart() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        //1400 -> Get the length of the clip
        scheduledExecutorService.scheduleAtFixedRate(new Lights(), 0,
                one_beat, TimeUnit.MILLISECONDS );
    }

    private class Lights implements Runnable {

        private int beat = 0;

        @Override
        public void run() {
            if (beat < 32) {
                // Using mod since there are only 8 beats per 2 bars. 32 beats in one State object
                int index = (beat % 8);
                beat++;

                // Turn off all of the lights
                turnOffAllLights(gameView.getLights());

                // Turn on the correct selected light
                JPanel selectedLight = gameView.getLights().get(index);
                CardLayout cardLayout = (CardLayout) selectedLight.getLayout();
                cardLayout.show(selectedLight, "on_light");
            }
            else {
                scheduledExecutorService.shutdownNow();
            }
        }
    }

    /* Helper method to make all of the JPanels holding the light png files to display the grey light */
    private void turnOffAllLights(ArrayList<JPanel> lights) {
        for (JPanel light: lights) {
            CardLayout cardLayout = (CardLayout) light.getLayout();
            cardLayout.show(light, "off_light");
        }
    }
}
