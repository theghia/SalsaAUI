package views;

import main.SalsaView;

import javax.swing.*;

public class MenuView extends SalsaView {

    private String SIMULATION = "simulation";
    private String TUTORIAL = "tutorial";
    private String JUSTIFIED_USER_MODEL = "justified_user_profile";

    public MenuView(String name, int width, int height) {
        super(name, width, height, true);
        setupButtons();

    }
    // Helper method to start up the buttons on the screen
    private void setupButtons() {
        // Button to move to the simulation view
        JButton simulation = new JButton("Simulation");
        simulation.setBounds  (100, 100,  100, 100);

        //btnWhereTo.setIcon(new ImageIcon(ASSETS + "salsa_background.jpg"));
        //btnWhereTo.setBorder(null);
        //btnWhereTo.setForeground(new Color(0,0,0,0));
        //btnWhereTo.setDisabledIcon(new ImageIcon(ASSETS + "salsa_background.jpg"));
        simulation.setEnabled(true);
        // just dumping here
        //this.setSize(800,800);
        this.getNavigationButtons().put(SIMULATION, simulation);
        //This is referring to the JPanel itself
        this.add(simulation); simulation.setBackground(null);

        // Button to move to the tutorial view
        JButton tutorial = new JButton("Tutorial");
        tutorial.setBounds(200, 300, 100, 100);
        tutorial.setEnabled(true);
        this.getNavigationButtons().put(TUTORIAL, tutorial);
        this.add(tutorial); tutorial.setBackground(null);

        // Button to move to the justified_user_model view
        JButton justified_user_model = new JButton("Track Record");
        justified_user_model.setBounds(200, 500, 200, 100);
        justified_user_model.setEnabled(true);
        this.getNavigationButtons().put(JUSTIFIED_USER_MODEL, justified_user_model);
        this.add(justified_user_model); justified_user_model.setBackground(null);


    }

}
