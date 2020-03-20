package controllers.simulation.hard;

import controllers.GUIController;
import main.SalsaModel;
import views.GameView;

public class HardSimulationGUIController extends GUIController {

    /**
     * Constructor for the HardSimulationGUIController that interacts with the HardSimulationView.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView       A GameView object that will be a HardSimulationView
     */
    public HardSimulationGUIController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
    }
}
