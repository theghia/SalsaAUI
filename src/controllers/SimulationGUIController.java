package controllers;

import components.Instrument;
import components.MoveGaugeNeedle;
import events.ClipInformationEvent;
import events.SimulationEvent;
import events.SimulationGUIListener;
import events.SimulationListener;
import main.SalsaController;
import main.SalsaModel;
import views.SimulationView;

import javax.swing.*;
import java.awt.*;

public class SimulationGUIController extends SalsaController implements SimulationListener, SimulationGUIListener {

    private SimulationView simulationView;
    private MoveGaugeNeedle moveGaugeNeedle;

    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public SimulationGUIController(SalsaModel salsaModel, String controllerName, SimulationView simulationView) {
        super(salsaModel, controllerName);
        this.simulationView = simulationView;

        this.moveGaugeNeedle = new MoveGaugeNeedle(simulationView.getRotateImage());
        moveGaugeNeedle.setStartingPosition();
    }

    /**
     * Method makes the "Beat-clicker" visible and makes the home and start button invisible.
     *
     * @param e A SimulationEvent object that is not needed with this method
     */
    @Override
    public void onSimulationStartedEvent(SimulationEvent e) {

        // Making the "Beat-Clicker" visible so that user knows where they should click
        simulationView.getBeatClicker().setVisible(true);

        // Makes the Start button disappear so that it can be clicked on once
        simulationView.getStartButton().setVisible(false);

        // The home button will disappear
        simulationView.getNavigationButtons().get("main").setVisible(false);

        moveGaugeNeedle.moveNeedle(0.75);
    }

    /**
     * Method changes the JLabel to display the current tempo of the music to the user and to show the current
     * instruments that are playing
     *
     * @param e A SimulationEvent object that is used to display the State information in the form of several GUI
     */
    @Override
    public void onNewStateEvent(SimulationEvent e) {
        // Make all of the instrument GUI invisible
        makeInstrumentGUIInvisible();

        // Loop through the instruments and choose the index from the ArrayList to make visible
        for (Instrument instrument: e.getCurrentState().getInstruments()) {
            simulationView.getInstrumentsGUI().get(instrument.getValue()).setVisible(true);
        }

        // Get the name of the tempo
        JPanel tempos = simulationView.getTempos();
        CardLayout cardLayout = (CardLayout) simulationView.getTempos().getLayout();
        cardLayout.show(simulationView.getTempos(), e.getCurrentState().getBpm().getName());
    }

    /**
     * Method changes the GUI to represent the current beat that the system is currently requesting the user to
     * find and the next beat that the system will request the user to find.
     *
     * @param e A SimulationEvent object that will contain information on what is the current beat and the next beat
     *         that the user will be tested on
     */
    @Override
    public void onNewBeatEvent(SimulationEvent e) {
        //System.out.println("Current beat: " + e.getCurrentBeat());
        //System.out.println("Next beat: " + e.getNextBeat());

        // The JPanels that have the digital numbers as a png
        JPanel currentBeat = simulationView.getCurrentBeat();
        JPanel nextBeat = simulationView.getNextBeat();

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
     * @param e A SimulationEvent object that will be used to pass the information of the new Error Value to the
     *          moveGaugeNeedle object.
     */
    @Override
    public void onNewErrorValueEvent(SimulationEvent e) {
        // My assumption is that the error value should be between [0, 1]
        assert (e.getErrorValue() > 1 || e.getErrorValue() < 0);

        // ADDING
        double toRound = e.getErrorValue() * 100;
        double rounded = Math.round(toRound);


        // Rounding the error value to two decimal places
        //double rounded2dp = Math.round(e.getErrorValue() * 100)/100;
        double rounded2dp = rounded/100;

        System.out.println("Rounded is: " + rounded2dp);
        // Move the needle according to the rounded error value
        moveGaugeNeedle.moveNeedle(rounded2dp);
    }

    /**
     * Method called once a simulation run has ended. This sets the model fields used to note the user's performance
     * back to their default state and displays the same GUI on the SimulationView that was there before the Start
     * button was clicked
     *
     * @param e A SimulationEvent object that will be used to reset the model (except the UserProfile) back to default
     */
    @Override
    public void onSimulationFinishedEvent(SimulationEvent e) {
        // Displaying the Diagonal Dashed Digital numbers
        onNewBeatEvent(e);

        // Making the instruments, tempo and "Beat-Clicker" invisible
        makeInstrumentGUIInvisible();
        simulationView.getTempos().setVisible(false);
        simulationView.getBeatClicker().setVisible(false);

        // Make the Start button visible again
        simulationView.getStartButton().setVisible(true);

        // Make the home button visible again
        simulationView.getNavigationButtons().get(simulationView.getMAIN()).setVisible(true);
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
        simulationView.getBeatClicker().setEnabled(true);

        // 3) Make the GUI instruments appear according to the starting State and set the correct JLabel to show
        // for the Tempo according to the starting State
        SimulationEvent e = new SimulationEvent(getSalsaModel());
        onNewStateEvent(e);

        // 4) Make the tempo JLabel appear
        simulationView.getTempos().setVisible(true);
    }

    /* Helper method to be used by onNewBeatEvent() to switch out between the JLabels holding the
    digital number png files */
    private void switchDigitalNumber(String name, JPanel digitalNumbers) {
        CardLayout cardLayout = (CardLayout) digitalNumbers.getLayout();
        cardLayout.show(digitalNumbers, name);
    }

    /* Helper method that makes all of the instrument GUI invisible on the SimulationView JPanel */
    private void makeInstrumentGUIInvisible() {
        for (JLabel instrument: simulationView.getInstrumentsGUI())
            instrument.setVisible(false);
    }
}
