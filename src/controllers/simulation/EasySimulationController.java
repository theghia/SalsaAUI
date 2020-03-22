package controllers.simulation;

import components.enums.BPM;
import components.State;
import controllers.GameController;
import main.SalsaModel;
import views.GameView;

import java.util.ArrayList;
import java.util.Map;

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
        // The random starting State that has been chosen
        State randState;
        // To loop through all of the State objects in the User Profile
        Map<String, State> states = getSalsaModel().getUserProfile().getStates();

        // An ArrayList of State objects that have not been explored
        ArrayList<State> unexploredStates = new ArrayList<State>();

        // An ArrayList of State objects that have previously been explored
        ArrayList<State> exploredStates = new ArrayList<State>();

        for (Map.Entry<String, State> entry: states.entrySet()) {
            if (!entry.getValue().hasBeenExplored() && entry.getValue().getBpm().equals(BPM.SLOW)
            && (entry.getValue().getInstruments().size() <= 2))
                unexploredStates.add(entry.getValue());
            else if (entry.getValue().hasBeenExplored() && entry.getValue().getBpm().equals(BPM.SLOW)
            && (entry.getValue().getInstruments().size() <= 2))
                exploredStates.add(entry.getValue());
        }

        // If there are some States that have not been explored, then randomly choose one of those State objects
        if (!unexploredStates.isEmpty()) {
            int rndIndex = this.getRandomGenerator().nextInt(unexploredStates.size());
            randState = unexploredStates.get(rndIndex);
        }
        // Otherwise, randomly choose a starting State from all possible State objects
        else {
            int rndIndex = this.getRandomGenerator().nextInt(exploredStates.size());
            randState = exploredStates.get(rndIndex);
        }
        return randState;
    }

    @Override
    public void clipReady(long clipSalsa) {

    }
}
