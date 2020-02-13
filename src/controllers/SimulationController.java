package controllers;

import components.GaussianErrorFunction;
import main.SalsaController;
import main.SalsaModel;
import views.SimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class SimulationController extends SalsaController {

    private SimulationView simulationView;

    private GaussianErrorFunction gef = new GaussianErrorFunction();
    private long simulationStart;
    private long oneMinute;

    public SimulationController(SalsaModel salsaModel, String controllerName, SimulationView simulationView) {
        super(salsaModel, controllerName);
        this.simulationView = simulationView;

        // test -- Not really needed tbh. The issue is with the function
        // Actually - this is good but should be in a separate function and not constructor
        this.simulationStart = System.currentTimeMillis();
        this.oneMinute = 10*1000;

        initButtonClicker();
    }

    private void initButtonClicker() {
        ActionListener click = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                //System.out.println(timestamp.getTime());
                //System.out.println(test);
                //System.out.println(gef.calculateErrorValue(timestamp.getTime(), test));
                long clickTSNormalised = System.currentTimeMillis() - simulationStart;
                System.out.println(clickTSNormalised);
                System.out.println(gef.calculateErrorValue(clickTSNormalised, oneMinute));
            }
        };
        simulationView.getBeatClicker().addActionListener(click);
    }

}
