package controllers.simulation;

import components.enums.State;
import controllers.GameController;
import main.SalsaModel;
import views.GameView;

public class EasySimulationController extends GameController {
    /**
     * Constructor for the GameController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView
     */
    public EasySimulationController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
    }

    // Write this up in the game controller but override it in the Tutorial Controller
    @Override
    public void calculateErrorValue(int currentBeat, int barNumber) {

    }

    @Override
    public void initStartButton() {

    }

    @Override
    public State chooseStartingState() {
        return null; // Starts with single instrument at slow tempo. Only explores slow tempos and one or 2 instruments at a time
    }

    @Override
    public void clipReady(long clipSalsa) {

    }
}
