package main;

import controllers.*;
import controllers.GameProgressionGUIController;
import controllers.GameProgressionMusicController;
import events.ClipInformationListener;
import events.GameGUIListener;
import events.GameProgressionListener;
import events.TutorialGUIListener;
import views.GameView;
import views.SimulationView;

public class MainApp {

    public static void main(String[] args) {
        // All of the views are started here
        MainFrame start = new MainFrame();

        SalsaModel model = new SalsaModel();

        SalsaController navigationController = new NavigationController(model, "navigation", start);

        // do a navigationController.start() type thing

        SimulationView simulationView = (SimulationView) start.getPanels().get("simulation");

        SalsaController simulationController = new SimulationController(model,
                "simulation", simulationView);

        SalsaController simulationGUIController = new GameProgressionGUIController(model,
                "simulation_gui", simulationView);
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

        GameView tutorialView = (GameView) start.getPanels().get("tutorial");
        SalsaController tutorialController = new TutorialController(model, "tutorial", tutorialView);
        SalsaController tutorialGUIController = new TutorialGUIController(model,
                "tutorial_gui", tutorialView);
        SalsaController tutorialMusicController = new TutorialMusicController(model, "tutorial_music");

        model.addTutorialGUIListener((GameProgressionListener) tutorialGUIController);
        model.addTutorialMusicListener((GameProgressionListener) tutorialMusicController);

        model.addTutorialClipInformationListener((ClipInformationListener) tutorialController);

        model.addTutorialGameGUIListener((GameGUIListener) tutorialGUIController);

        model.addTeachTutorialGUIListener((TutorialGUIListener) tutorialGUIController);
    }

}
