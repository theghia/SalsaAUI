package controllers.simulation.easy;

import controllers.MusicController;
import main.SalsaModel;

public class EasySimulationMusicController extends MusicController {
    /**
     * Constructor for the MusicController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public EasySimulationMusicController(SalsaModel salsaModel, String controllerName) {
        super(salsaModel, controllerName);
    }

    @Override
    protected void countdownStarted() {
        getSalsaModel().fireEasyCountdownStartedEvent();
    }

    @Override
    protected void countdownFinished() {
        getSalsaModel().fireEasyCountdownFinishedEvent();
        getSalsaModel().fireEasyNewStateEvent();
    }

    @Override
    protected void clipReady(long clipSalsaLength) {
        getSalsaModel().fireEasyClipInfoReadyEvent(clipSalsaLength);
    }
}
