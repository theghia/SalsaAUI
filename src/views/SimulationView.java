package views;

import main.SalsaView;

import javax.swing.*;
import java.awt.*;

public class SimulationView extends SalsaView {

    private JButton beatClicker;

    public SimulationView(String name, Dimension dimension) {
        super(name, dimension, false);
        setupButtons();
    }

    public JButton getBeatClicker() {
        return beatClicker;
    }

    /* Helper method to set up the buttons in this view */
    private void setupButtons() {
        // Button to be used by the user to click for the beat
        this.beatClicker = new JButton("+");
        beatClicker.setPreferredSize(new Dimension(50,50));
        beatClicker.setEnabled(true);
        this.add(beatClicker); beatClicker.setBackground(null);
        //beatClicker.setBorder(new RoundedBorder());
    }

}
