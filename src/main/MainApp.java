package main;

import controllers.NavigationController;
import controllers.SimulationController;
import views.SimulationView;

public class MainApp {

    public static void main(String[] args) {
        // All of the views are started here
        MainFrame start = new MainFrame();

        SalsaModel model = new SalsaModel();

        SalsaController control1 = new NavigationController(model, "navigation", start);

        SimulationView simulationView = (SimulationView) start.getPanels().get("simulation");
        SalsaController control2 = new SimulationController(model, "simulation", simulationView);
    }
}
