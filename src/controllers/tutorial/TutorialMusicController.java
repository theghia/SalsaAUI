package controllers.tutorial;

import components.PlayFile;
import controllers.MusicController;
import main.SalsaModel;

/**
 * TutorialMusicController class extends MusicController and overrides the methods in MusicController so that the
 * TutorialView is manipulated with the "fire" methods.
 *
 * @author Gareth Iguasnia
 * @date 12/03/2020
 */
public class TutorialMusicController extends MusicController {
    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public TutorialMusicController(SalsaModel salsaModel, String controllerName) {
        super(salsaModel, controllerName);
    }

    @Override
    public void countdownStarted() {
        getSalsaModel().fireTutorialCountdownStartedEvent();
    }

    @Override
    public void countdownFinished() {
        getSalsaModel().fireTutorialCountdownFinishedEvent();
        getSalsaModel().fireTutorialNewStateEvent();
    }

    @Override
    public void clipReady(long clipSalsaLength) {
        getSalsaModel().fireTutorialClipInfoReadyEvent(clipSalsaLength);
    }

}
