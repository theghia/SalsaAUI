package controllers;

import events.TutorialGUIListener;
import main.SalsaModel;
import views.GameView;

public class TutorialGUIController extends GUIController implements TutorialGUIListener {
    /**
     * Constructor for the TutorialGUIController that will be used in conjunction with the TutorialView.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView
     */
    public TutorialGUIController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);
    }

    @Override
    public void onTutorialStarted() {
        // Display the first few JLabels to progress through the tutorial
    }

    @Override
    public void onTutorialFinished() {
        // Hides all of the JLabels for the tutorial and fires off the onGameStart for the MusicController
    }

    // We need to add methods here to implement an interface that takes care of bringing up the correct lights
    //hold events in the GameGUIListener but the TutorialController will be calling those methods...? nah...? In tut
}
