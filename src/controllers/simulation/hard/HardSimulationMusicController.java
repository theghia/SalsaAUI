package controllers.simulation.hard;

import controllers.MusicController;
import main.SalsaModel;

/**
 * HardSimulationMusicController Class extends the MusicController class. This class deals with the logic to play the Salsa
 * audio files one after the other during the simulation according to how the simulation moves around the State objects.
 *
 * @author Gareth Iguasnia
 * @date 26/02/2020
 */
public class HardSimulationMusicController extends MusicController {
    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public HardSimulationMusicController(SalsaModel salsaModel, String controllerName) {
        super(salsaModel, controllerName);
    }

    @Override
    public void countdownStarted() {
        getSalsaModel().fireCountdownStartedEvent();
    }

    @Override
    public void countdownFinished() {
        getSalsaModel().fireCountdownFinishedEvent();
        getSalsaModel().fireNewStateEvent();
    }

    @Override
    public void clipReady(long clipSalsaLength) {
        getSalsaModel().fireClipInfoReadyEvent(clipSalsaLength);
    }
}
