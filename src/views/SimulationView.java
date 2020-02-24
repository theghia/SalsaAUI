package views;

import main.SalsaView;

import javax.swing.*;
import java.awt.*;

public class SimulationView extends SalsaView {

    private JButton beatClicker;
    private JButton startButton;

    public SimulationView(String name, Dimension dimension) {
        super(name, dimension, false);
        setupButtons();
        layoutButtons();
    }

    public JButton getBeatClicker() {
        return beatClicker;
    }

    public JButton getStartButton() { return startButton; }

    /* Helper method to set up the buttons in this view */
    private void setupButtons() {
        // Button to be used by the user to click for the beat
        this.beatClicker = new JButton("+");
        beatClicker.setPreferredSize(new Dimension(50,50));
        beatClicker.setEnabled(true);
        this.add(beatClicker); beatClicker.setBackground(null);
        //beatClicker.setBorder(new RoundedBorder());

        // Button to be used by the user to start the simulation
        this.startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(75, 75));
        startButton.setEnabled(true);
        this.add(startButton); startButton.setBackground(null);
    }

    private void layoutButtons() {
        this.getPanelLayout().putConstraint(SpringLayout.WEST, startButton, 400, SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, startButton, 400, SpringLayout.NORTH, this);
    }

}
