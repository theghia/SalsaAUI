package components.gui;

import views.GameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Lights class that implements Runnable to display the timing of the Salsa audio clip through lights. The first light
 * represents beat 1 of the bar of music and the eighth light represents the eighth beat of bar of music.
 *
 * @author Gareth Iguasnia
 * @date 15/03/2020
 */
public class Lights implements Runnable {
    // To access the correct light
    int beat;

    // You need access to the GameView to manipulate which light shows on and which shows off
    GameView gameView;

    /**
     * Constructor for the Lights class.
     * We start with beat at 0.
     *
     * @param gameView A GameView class so that we can access the lights GUI
     */
    public Lights(GameView gameView) {
        this.beat = 0;
        this.gameView = gameView;
    }

    /**
     * Method displays the "On" light for the selected index
     */
    @Override
    public void run() {
        if (beat < 32) {
            // Using mod since there are only 8 beats per 2 bars. 32 beats in one State object
            int index = (beat % 8);
            System.out.println("One light");

            // Turn off all of the lights
            turnOffAllLights(gameView.getLights());

            // Turn on the correct selected light
            JPanel selectedLight = gameView.getLights().get(index);
            CardLayout cardLayout = (CardLayout) selectedLight.getLayout();
            cardLayout.show(selectedLight, "on_light");
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
