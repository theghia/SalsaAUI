package controllers.simulation.easy;

import controllers.GUIController;
import main.SalsaModel;
import views.GameView;

public class EasySimulationGUIController extends GUIController {
    /**
     * Constructor for the GUIController that will only be called by the subclass through the super keyword.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView
     */
    public EasySimulationGUIController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
    }
}
