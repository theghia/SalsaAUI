package controllers;

import main.SalsaController;
import main.SalsaModel;
import views.SimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class SimulationController extends SalsaController {

    private SimulationView simulationView;

    public SimulationController(SalsaModel salsaModel, String controllerName, SimulationView simulationView) {
        super(salsaModel, controllerName);
        this.simulationView = simulationView;
        initButtonClicker();
    }

    private void initButtonClicker() {
        ActionListener click = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                System.out.println(timestamp);
            }
        };
        simulationView.getBeatClicker().addActionListener(click);
    }

}
