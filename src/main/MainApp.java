package main;

import controllers.*;
import controllers.GameProgressionGUIController;
import controllers.GameProgressionMusicController;
import events.ClipInformationListener;
import events.GameGUIListener;
import events.GameProgressionListener;
import views.SimulationView;

public class MainApp {

    public static void main(String[] args) {
        // All of the views are started here
        MainFrame start = new MainFrame();

        SalsaModel model = new SalsaModel();

        SalsaController navigationController = new NavigationController(model, "navigation", start);

        // do a navigationController.start() type thing

        SimulationView simulationView = (SimulationView) start.getPanels().get("simulation");
        SalsaController simulationController = new SimulationController(model, "simulation", simulationView);

        //GaussianErrorFunction test = new GaussianErrorFunction();
        //long time1 = System.currentTimeMillis()/1000;
        //long time2 = (System.currentTimeMillis()/1000) + 3;// + (60*1000);
        //System.out.println(System.currentTimeMillis()/1000);

        //System.out.println(test.calculateErrorValue(439999,440000));

        SalsaController simulationGUIController = new GameProgressionGUIController(model, "simulation_gui",
                simulationView);
        SalsaController simulationMusicController = new GameProgressionMusicController(model,
                "simulation_music");

        simulationController.getSalsaModel().setNameOfUser("Gareth");
        System.out.println(model.getNameOfUser());
        simulationGUIController.getSalsaModel().setNameOfUser("Not Gareth");
        System.out.println(model.getNameOfUser());

        // Casting as we only want the model to have the Listener version of the controller so that any methods
        // that the SalsaController has will not be present in the model
        model.addSimulationListener((GameProgressionListener) simulationGUIController);
        model.addSimulationListener((GameProgressionListener) simulationMusicController);
        model.addClipInformationListener((ClipInformationListener) simulationController);
        model.addSimulationGUIListener((GameGUIListener) simulationGUIController);
    }

}
