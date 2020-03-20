package main;

import controllers.*;
import controllers.simulation.hard.HardSimulationGUIController;
import controllers.simulation.hard.HardSimulationMusicController;
import controllers.simulation.hard.HardSimulationController;
import controllers.tutorial.TutorialController;
import controllers.tutorial.TutorialGUIController;
import controllers.tutorial.TutorialMusicController;
import listeners.ClipInformationListener;
import listeners.GameGUIListener;
import listeners.GameProgressionListener;
import listeners.TutorialGUIListener;
import views.games.HardSimulationView;
import views.TutorialView;

public class MainApp {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainApp main = new MainApp();

                // All of the views are created here
                MainFrame mainFrame = new MainFrame();

                SalsaModel model = new SalsaModel();

                // Navigation Controller
                new NavigationController(model,
                        "navigation", mainFrame);

                // Setting up the controllers to listen to the model
                main.setupHardSimulation(mainFrame, model);
                main.setupTutorial(mainFrame, model);
            }
        });
    }

    public void setupHardSimulation(MainFrame mainFrame, SalsaModel salsaModel) {
        // Isolating the simulation view
        HardSimulationView hardSimulationView = (HardSimulationView) mainFrame.getPanels().get("hard");

        // Setting up the three controllers needed for the Game Simulation
        SalsaController simulationController = new HardSimulationController(salsaModel,
                "hard_simulation", hardSimulationView);

        SalsaController simulationGUIController = new HardSimulationGUIController(salsaModel,
                "hard_simulation_gui", hardSimulationView);
        SalsaController simulationMusicController = new HardSimulationMusicController(salsaModel,
                "hard_simulation_music");


        // Casting as we only want the model to have the Listener version of the controller so that any methods
        // that the SalsaController has will not be present in the model
        salsaModel.addSimulationListener((GameProgressionListener) simulationGUIController);
        salsaModel.addSimulationListener((GameProgressionListener) simulationMusicController);
        salsaModel.addClipInformationListener((ClipInformationListener) simulationController);
        salsaModel.addSimulationGUIListener((GameGUIListener) simulationGUIController);
    }

    public void setupTutorial(MainFrame mainFrame, SalsaModel salsaModel) {
        TutorialView tutorialView = (TutorialView) mainFrame.getPanels().get("tutorial");
        SalsaController tutorialController = new TutorialController(salsaModel,
                "tutorial", tutorialView);
        SalsaController tutorialGUIController = new TutorialGUIController(salsaModel,
                "tutorial_gui", tutorialView);
        SalsaController tutorialMusicController = new TutorialMusicController(salsaModel,
                "tutorial_music");

        salsaModel.addTutorialGUIListener((GameProgressionListener) tutorialGUIController);
        salsaModel.addTutorialMusicListener((GameProgressionListener) tutorialMusicController);

        salsaModel.addTutorialClipInformationListener((ClipInformationListener) tutorialController);

        salsaModel.addTutorialGameGUIListener((GameGUIListener) tutorialGUIController);

        salsaModel.addTeachTutorialGUIListener((TutorialGUIListener) tutorialGUIController);
    }

}
