package controllers;

import components.Instrument;
import components.MoveGaugeNeedle;
import events.ClipInformationEvent;
import events.GameEvent;
import events.GameGUIListener;
import events.GameProgressionListener;
import main.SalsaController;
import main.SalsaModel;
import views.GameView;

import javax.swing.*;
import java.awt.*;

/**
 * GUIController abstract class extends the SalsaController and implements the GameProgressionListener and
 * GameGUIListener interface. This class will be derived by controllers that will interact with the GUI while the game
 * is occurring to test the user to locate the correct the timing.
 *
 * @author Gareth Iguasnia
 * @date 12/03/2020
 */
public abstract class GUIController extends SalsaController implements GameProgressionListener, GameGUIListener {

    private GameView gameView;
    private MoveGaugeNeedle moveGaugeNeedle;

    /**
     * Constructor for the GUIController that will only be called by the subclass through the super keyword.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public GUIController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName);
        this.gameView = gameView;

        // Setting the gauge needle to the starting position
        this.moveGaugeNeedle = new MoveGaugeNeedle(gameView.getRotateImage());
        this.moveGaugeNeedle.setStartingPosition();
    }

    /**
     * Method changes the GUI to represent the current beat that the system is currently requesting the user to
     * find and the next beat that the system will request the user to find.
     *
     * @param e A GameEvent object that will contain information on what is the current beat and the next beat
     *         that the user will be tested on
     */
    @Override
    public void onNewBeatEvent(GameEvent e) {
        // The JPanels that have the digital numbers as a png
        JPanel currentBeat = gameView.getCurrentBeat();
        JPanel nextBeat = gameView.getNextBeat();

        // NOTE: The default of a beat is -1, which pertains to the "no_beat" png file

        // Changing the GUI for the current beat
        if (e.getCurrentBeat() < 1)
            switchDigitalNumber("no_beat", currentBeat);
        else
            switchDigitalNumber(Integer.toString(e.getCurrentBeat()), currentBeat);

        // Changing the GUI for the next beat
        if (e.getNextBeat() < 1)
            switchDigitalNumber("no_beat", nextBeat);
        else
            switchDigitalNumber(Integer.toString(e.getNextBeat()), nextBeat);
    }

    /**
     * Method changes the angle of the needle of the Gauge png to match the error value calculated from the user's
     * input. The better value the user scored, the closer the needle will be on the green end of the Gauge. The worse
     * the score that the user achieved, the closer the needle will move towards the end of the red zone.
     *
     * @param e A GameEvent object that will be used to pass the information of the new Error Value to the
     *          moveGaugeNeedle object.
     */
    @Override
    public void onNewErrorValueEvent(GameEvent e) {
        // My assumption is that the error value should be between [0, 1]
        assert (e.getErrorValue() > 1 || e.getErrorValue() < 0);

        // Rounding the error value to two decimal places
        double toRound = e.getErrorValue() * 100;
        double rounded = Math.round(toRound);
        double rounded2dp = rounded/100;

        // Move the needle according to the rounded error value
        moveGaugeNeedle.moveNeedle(rounded2dp);
    }

    /**
     * Method called once a game run has ended. This sets the model fields used to note the user's performance
     * back to their default state and displays the same GUI on the GameView that was there before the Start
     * button was clicked
     *
     * @param e A GameEvent object that will be used to reset the model (except the UserProfile) back to default
     */
    @Override
    public void onGameFinishedEvent(GameEvent e) {
        // Displaying the Diagonal Dashed Digital numbers
        onNewBeatEvent(e);

        // Making the instruments, tempo and "Beat-Clicker" invisible
        makeInstrumentGUIInvisible();
        gameView.getTempos().setVisible(false);
        gameView.getBeatClicker().setVisible(false);

        // Make the Start button visible again
        gameView.getStartButton().setVisible(true);

        // Make the home button visible again
        gameView.getNavigationButtons().get(gameView.getMAIN()).setVisible(true);
    }

    @Override
    public void onCountdownStartedEvent(ClipInformationEvent e) {
        // This method will be called by the model in model.fireCountDownStartedEvent()
        // from e, we can get the clip length of the countdown audio and put up the relevant
        // GUI at the spaced intervals (In the ClickOnce Class)

        // Here we need to set the flag countdownCurrentlyPlaying to false which can be done in this timer thread
        // and this will be done once the clip has finished. Just like what you did with the SimulationController
        // NOOOO, this needs to be called after the clip has finished!
        //getSalsaModel().setCountdownCurrentlyPlaying(false);

        // Here is where we start the two timer threads. One will do the GUI countdown crap and the other
        // will do the onCountDownFinishedEvent (by firing the relevant event of course). We can start the
        // timer threads in the CountDown Class! ONLY NEED ONE TIMER THREAD4

        // You do need. The fire method is in the CLickOnce constructor number 1. A

        // IF POSSIBLE: 3-2-1 GUI that disappears just before the salsa music
    }

    /**
     * Method is called once per Simulation run for this MVC application through the CLickOnce class where:
     * 1) We set the flag countdownCurrentlyPlaying to false as it has finished
     * 2) We make the "Beat Clicker" look clikable
     * 3) Make the relevant instruments appear on screen
     * 4) Make the relevant tempo label appear on screen.
     */
    @Override
    public void onCountdownFinishedEvent() {
        // 1) Set the flag countdownCurrentlyPlaying to false
        getSalsaModel().setCountdownCurrentlyPlaying(false);

        // 2) Make the button clicker look "clickable"
        gameView.getBeatClicker().setEnabled(true);

        // 3) Make the GUI instruments appear according to the starting State and set the correct JLabel to show
        // for the Tempo according to the starting State
        GameEvent e = new GameEvent(getSalsaModel());
        onNewStateEvent(e);

        // 4) Make the tempo JLabel appear
        gameView.getTempos().setVisible(true);
    }

    /**
     * Method makes the "Beat-clicker" visible and makes the home and start button invisible.
     *
     * @param e A GameEvent object that is not needed with this method
     */
    @Override
    public void onGameStartedEvent(GameEvent e) {
        // Making the "Beat-Clicker" visible so that user knows where they should click
        gameView.getBeatClicker().setVisible(true);

        // Makes the Start button disappear so that it can be clicked on once
        gameView.getStartButton().setVisible(false);

        // The home button will disappear
        gameView.getNavigationButtons().get("main").setVisible(false);
    }

    /**
     * Method changes the JLabel to display the current tempo of the music to the user and to show the current
     * instruments that are playing
     *
     * @param e A GameEvent object that is used to display the State information in the form of several GUI
     */
    @Override
    public void onNewStateEvent(GameEvent e) {
        // Make all of the instrument GUI invisible
        makeInstrumentGUIInvisible();

        // Loop through the instruments and choose the index from the ArrayList to make visible
        for (Instrument instrument: e.getCurrentState().getInstruments()) {
            gameView.getInstrumentsGUI().get(instrument.getValue()).setVisible(true);
        }

        // Get the name of the tempo
        JPanel tempos = gameView.getTempos();
        CardLayout cardLayout = (CardLayout) gameView.getTempos().getLayout();
        cardLayout.show(gameView.getTempos(), e.getCurrentState().getBpm().getName());
    }

    /**
     * Method returns the GameView JPanel that is working in conjunction with the GUIController setup
     *
     * @return A GameView object that the GUIController is connected to
     */
    public GameView getGameView() {
        return gameView;
    }

    /* Helper method to be used by onNewBeatEvent() to switch out between the JLabels holding the
       digital number png files */
    private void switchDigitalNumber(String name, JPanel digitalNumbers) {
        CardLayout cardLayout = (CardLayout) digitalNumbers.getLayout();
        cardLayout.show(digitalNumbers, name);
    }

    /* Helper method that makes all of the instrument GUI invisible on the SimulationView JPanel */
    private void makeInstrumentGUIInvisible() {
        for (JLabel instrument: gameView.getInstrumentsGUI())
            instrument.setVisible(false);
    }
}
