package controllers;

import events.TutorialGUIListener;
import main.SalsaModel;
import views.GameView;
import views.TutorialView;

import javax.swing.*;

/**
 * TutorialGUIController class extends GUIController and implements the TutorialGUIListener interface.
 * This class handles the logic needed to display the certain GUI on the screen depending on how the user navigates
 * around the tutorial.
 *
 * @author Gareth Iguasnia
 * @date 17/03/2020
 */
public class TutorialGUIController extends GUIController implements TutorialGUIListener {

    // To access the buttons that the tutorial view has but that the game view does not have
    private TutorialView tutorialView;

    /**
     * Constructor for the TutorialGUIController that will be used in conjunction with the TutorialView.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     * @param gameView
     */
    public TutorialGUIController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName, gameView);

        this.tutorialView = (TutorialView) gameView;
    }

    @Override
    public void onTutorialStarted() {
        onKeepLights();
        getGameView().getStartButton().setVisible(false);
    }

    @Override
    public void onTutorialFinished() {
        // This is when the practice button is clicked
        tutorialView.getPractice().setVisible(false);
        tutorialView.getNo().setVisible(false);
        tutorialView.getBack().setVisible(false);

        // We start the Game in the Tutorial
        getSalsaModel().fireTutorialGameStartEvent();
    }

    @Override
    public void onKeepLights() {
        makeGUIDisappear();
        tutorialView.getBack().setVisible(false);

        // Needed for the tutorial
        for (JPanel light: tutorialView.getLights())
            light.setVisible(true);
        tutorialView.getNext().setVisible(true);
        tutorialView.getSkip().setVisible(true);
        tutorialView.getExplanations().get(0).setVisible(true);
    }

    @Override
    public void onKeepInstrumentsAndTempo() {
        makeGUIDisappear();

        // Needed for the tutorial
        for (JLabel instrument: tutorialView.getInstrumentsGUI())
            instrument.setVisible(true);
        tutorialView.getTempos().setVisible(true);
        tutorialView.getExplanations().get(1).setVisible(true);
        tutorialView.getBack().setVisible(true);
    }

    @Override
    public void onKeepCurrentBeat() {
        makeGUIDisappear();

        // Needed for the tutorial
        tutorialView.getCurrentBeatLabel().setVisible(true);
        tutorialView.getCurrentBeat().setVisible(true);
        tutorialView.getExplanations().get(2).setVisible(true);
    }

    @Override
    public void onKeepNextBeat() {
        makeGUIDisappear();

        // Needed for the tutorial
        tutorialView.getNextBeatLabel().setVisible(true);
        tutorialView.getNextBeat().setVisible(true);
        tutorialView.getExplanations().get(3).setVisible(true);
    }

    @Override
    public void onKeepClicker() {
        makeGUIDisappear();

        // Needed for the tutorial
        tutorialView.getBeatClicker().setVisible(true);
        tutorialView.getExplanations().get(4).setVisible(true);
    }

    @Override
    public void onKeepGauge() {
        makeGUIDisappear();
        tutorialView.getPractice().setVisible(false);
        tutorialView.getNo().setVisible(false);

        // Needed for the tutorial
        tutorialView.getGaugeGUI().setVisible(true);
        tutorialView.getExplanations().get(5).setVisible(true);
        tutorialView.getNext().setVisible(true);
        tutorialView.getSkip().setVisible(true);
    }

    @Override
    public void onFinalTutorial() {
        makeGUIDisappear();
        tutorialView.getSkip().setVisible(false);
        tutorialView.getNext().setVisible(false);

        // Needed for the tutorial
        tutorialView.getPractice().setVisible(true);
        tutorialView.getNo().setVisible(true);
    }

    /* Helper method that makes all of the GUI, bar the buttons for the tutorial navigation, invisible */
    private void makeGUIDisappear() {
        // The current and next beats
        tutorialView.getNextBeat().setVisible(false);
        tutorialView.getCurrentBeat().setVisible(false);
        tutorialView.getCurrentBeatLabel().setVisible(false);
        tutorialView.getNextBeatLabel().setVisible(false);

        // The gauge
        tutorialView.getGaugeGUI().setVisible(false);
        tutorialView.getRotateNeedle().setVisible(false);

        // The beat clicker
        tutorialView.getBeatClicker().setVisible(false);

        // The lights
        for (JLabel instrument: tutorialView.getInstrumentsGUI())
            instrument.setVisible(false);

        // The tempo
        tutorialView.getTempos().setVisible(false);

        // The text labels
        for (JTextArea text: tutorialView.getExplanations())
            text.setVisible(false);

        // The instruments
        for (JLabel instrument: tutorialView.getInstrumentsGUI())
            instrument.setVisible(false);
    }

}
