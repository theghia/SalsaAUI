package main;

import components.State;
import components.UserProfile;
import events.*;

import java.util.ArrayList;

public class SalsaModel {

    // Name of the user of the application
    private String nameOfUser;

    // To cache the current view
    private String currentView;

    // Holds information on the user's ability in finding timing on the different combinations of instruments and tempo
    private UserProfile userProfile;

    // To know what the current state the simulation is at when it is running
    private State currentState;

    // To keep track of what beats we require the user to find
    private int currentBeat;
    private int nextBeat;
    private int barNumber;

    // Keeping track whether the user has previously clicked before during each 8-beat bar of music
    private boolean hasClickedOnce1;
    private boolean hasClickedOnce2;
    private volatile int windowTracker;
    private volatile int buttonClickerTracker;

    // To keep track of the number of State objects we have transitioned
    private int numTransitionedStates;

    // A beat timeline so that we can use the error function to compare the user's input to the correct timing
    private ArrayList<Long> beatTimeline;

    // To normalise the timestamp of the user's input
    private long timeAccumulation;

    // Flag to determine whether the countdown audio clip is playing or a Salsa audio clip
    private boolean countdownCurrentlyPlaying;

    // An ArrayList of SimulationListeners associated with the SimulationGUIController class and the
    // SimulationMusicController class
    private ArrayList<SimulationListener> simListeners;

    // This listener will be used with the SimulationController Class
    private ClipInformationListener clipInfoListener;

    // This listener will only be used with the SimulationGUIController Class
    private SimulationGUIListener simGUIListener;

    /**
     * Constructor of the SalsaModel Class
     */
    public SalsaModel() {
        // Initialise all of the States along with their respective neighbours
        userProfile = new UserProfile();

        // Initialise the ArrayList to be able to add Listeners
        simListeners = new ArrayList<>(2);

        // The beats will only be from 1 to 8. -1 used as they can never be that number
        this.currentBeat = -1;
        this.nextBeat = -1;

        // We will only go through 4 bars per Salsa audio clip. 0 is the default.
        this.barNumber = 0;

        // Default is true as we do not want to take any user input until the simulation starts
        this.hasClickedOnce1 = true;
        this.hasClickedOnce2 = true;
        this.windowTracker = 1;
        this.buttonClickerTracker = 1;

        // Default will be 0
        this.timeAccumulation = 0;

        // Default will be true
        this.countdownCurrentlyPlaying = true;
    }

    /* Methods to add listeners to the Model */

    public void addSimulationListener(SimulationListener simulationListener) {
        this.simListeners.add(simulationListener);
    }

    public void addClipInformationListener(ClipInformationListener clipInfoListener) {
        this.clipInfoListener = clipInfoListener;
    }

    public void addSimulationGUIListener(SimulationGUIListener simGUIListener) {
        this.simGUIListener = simGUIListener;
    }


    /* FIRE EVENT METHODS */

    public void fireSimulationStartEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listeners will execute whatever logic they have implemented
        for (SimulationListener simulationListener: this.simListeners)
            simulationListener.onSimulationStartedEvent(e);
    }

    // The fire method for fireClipInfoReadyEvent(long 123clip, long salsa1) --> onInitClipInfoReadyEvent
    public void fireClipInfoReadyEvent(long clip123, long clipSalsa) {
        ClipInformationEvent e = new ClipInformationEvent(this, clip123, clipSalsa);

        // The listener will execute whatever logic it has implemented
        this.clipInfoListener.onInitClipInfoReadyEvent(e);
    }

    public void fireClipInfoReadyEvent(long clipSalsa) {
        ClipInformationEvent e = new ClipInformationEvent(this, clipSalsa);

        // The listener will execute whatever logic it has implemented
        this.clipInfoListener.onClipInfoReadyEvent(e);
    }

    public void fireNewStateEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listeners will execute whatever logic they have implemented
        for (SimulationListener simulationListener: this.simListeners)
            simulationListener.onNewStateEvent(e);
    }

    public void fireNewBeatEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onNewBeatEvent(e);
    }

    public void fireSimulationFinishedEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onSimulationFinishedEvent(e);
    }

    public void fireNewErrorValueEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onNewErrorValueEvent(e);
    }

    public void fireCountdownStartedEvent(long clip123, long clipSalsa) {
        ClipInformationEvent e = new ClipInformationEvent(this, clip123, clipSalsa);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onCountdownStartedEvent(e);

    }

    public void fireCountdownFinishedEvent() {
        this.simGUIListener.onCountdownFinishedEvent();
    }

    /* CHANGE MODEL STATE */

    // Should never reach below 0
    public void decreaseNumTransitionedStates() {
        this.numTransitionedStates--;
    }

    public void addToTimeAccumulated(long add) {
        this.timeAccumulation += add;
    }

    /**
     * Method to be called at the end of a simulation run to reset the model's variables used in order to note down the
     * necessary information on the user's performance in identifying the correct timing
     */
    public void resetModel() {
        // Default is -1
        this.currentBeat = -1;
        this.nextBeat = -1;

        // No State as simulation is not running
        this.currentState = null;

        // Bar number begins at 1
        this.barNumber = 1;

        // ClickedOnce flags default is true
        this.hasClickedOnce1 = true;
        this.hasClickedOnce2 = true;

        // These trackers are default set to 1
        this.windowTracker = 1;
        this.buttonClickerTracker = 1;

        // No beat timeline as the simulation is not running
        this.beatTimeline = null;

        // Default is 0
        this.timeAccumulation = 0;

        // Default is true, ready for when the simulation is played again
        this.countdownCurrentlyPlaying = true;
    }

    public synchronized void increaseWindowTracker() {
        if (this.windowTracker == 1)
            this.windowTracker++;
    }

    public synchronized void decreaseWindowTracker() {
        if (this.windowTracker == 2)
            this.windowTracker--;
    }

    public synchronized void increaseButtonClickerTracker() {
        this.buttonClickerTracker = 2;
    }

    public synchronized void decreaseButtonClickerTracker() {
        this.buttonClickerTracker = 1;
    }

    /* SETTERS */

    /**
     * Method sets the field currentView with a String object representing the current view
     *
     * @param currentView String object representing the current view
     */
    public void setCurrentView(String currentView) {
        this.currentView = currentView;
    }

    public void setNumTransitionedStates(int numTransitionedStates) {
        this.numTransitionedStates = numTransitionedStates;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setNextBeat(int nextBeat) {
        this.currentBeat = this.nextBeat;
        this.nextBeat = nextBeat;
    }

    public void setErrorValue(double errorValue) {
        // This will only be called if the user clicks appropriately
        String id = userProfile.createID(currentState.getBpm(), currentState.getInstruments());

        // Put the error value in...
        userProfile.getStates().get(id).getErrorValues().add(errorValue);
    }

    public void setBeatTimeline(ArrayList<Long> beatTimeline) {
        this.beatTimeline = beatTimeline;
    }

    public void setCountdownCurrentlyPlaying(boolean countdownCurrentlyPlaying) {
        this.countdownCurrentlyPlaying = countdownCurrentlyPlaying;
    }

    public synchronized void setHasClickedOnce1(boolean hasClickedOnce1) {
        this.hasClickedOnce1 = hasClickedOnce1;
    }

    public synchronized void setHasClickedOnce2(boolean hasClickedOnce2) {
        this.hasClickedOnce2 = hasClickedOnce2;
    }

    public void setBarNumber(int barNumber) {
        this.barNumber = barNumber;
    }

    /* GETTERS */

    /**
     * Method gets the UserProfile associated with the user.
     *
     * @return An UserProfile object associated to the user.
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * Method gets the field currentView
     *
     * @return A String object representing the current view
     */
    public String getCurrentView() {
        return currentView;
    }

    public long getTimeAccumulation() {
        return timeAccumulation;
    }

    public boolean isCountdownCurrentlyPlaying() {
        return countdownCurrentlyPlaying;
    }

    public int getNumTransitionedStates() {
        return numTransitionedStates;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public State getCurrentState() {
        return currentState;
    }

    public ArrayList<Long> getBeatTimeline() {
        return beatTimeline;
    }

    public boolean hasClickedOnce1() {
        return hasClickedOnce1;
    }

    public boolean hasClickedOnce2() {
        return hasClickedOnce2;
    }

    public int getWindowTracker() {
        return this.windowTracker;
    }

    public int getButtonClickerTracker() {
        return this.buttonClickerTracker;
    }

    public int getCurrentBeat() {
        return currentBeat;
    }

    public int getNextBeat() {
        return nextBeat;
    }

    public int getBarNumber() {
        return barNumber;
    }
}
