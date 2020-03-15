package controllers;

import main.SalsaModel;
import views.GameView;

public class SimulationGUIController extends GUIController {

    /**
     * Constructor for the SimulationGUIController that interacts with the SimulationView.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView       A GameView object that will be a SimulationView
     */
    public SimulationGUIController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
    }
}
