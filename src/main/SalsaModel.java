package main;

import components.State;
import components.UserProfile;
import events.ClipInformationEvent;
import events.ClipInformationListener;
import events.SimulationEvent;
import events.SimulationListener;

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

    // Keeping track whether the user has previously clicked before during each 8-beat bar of music
    private boolean hasClickedOnce;

    // To keep track of the number of State objects we have transitioned
    private int numTransitionedStates;

    // A beat timeline so that we can use the error function to compare the user's input to the correct timing
    private ArrayList<Long> beatTimeline;

    // To normalise the timestamp of the user's input
    private long timeAccumulation;

    // An ArrayList of SimulationListeners associated with the SimulationGUIController class and the
    // SimulationMusicController class
    ArrayList<SimulationListener> simListeners;

    // This listener will be used with the SimulationController Class
    ClipInformationListener clipInfoListener;

    /**
     * Constructor of the SalsaModel Class
     */
    public SalsaModel() {
        // Initialise all of the States along with their respective neighbours
        userProfile = new UserProfile();

        // Initialise the ArrayList to be able to add Listeners
        simListeners = new ArrayList<SimulationListener>(2);

        // The beats will only be from 1 to 8. -1 used as they can never be that number
        this.currentBeat = -1;
        this.nextBeat = -1;

        // Default will be false as the user would not have clicked the beat button before the simulation has started
        this.hasClickedOnce = false;

        // Default will be 0
        this.timeAccumulation = 0;
    }

    public void addSimulationListener(SimulationListener simulationListener) {
        this.simListeners.add(simulationListener);
    }

    public void addClipInformationListener(ClipInformationListener clipInfoListener) {
        this.clipInfoListener = clipInfoListener;
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

    public State getCurrentState() {
        return currentState;
    }

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
        //simListeners.onNewStateEvent(e);
    }

    public void fireNewBeatEvent() {
        //simListeners.onNewBeatEvent(e)
    }

    public void fireSimulationFinishedEvent() {
        //simListeners.onSimulationFinished;
    }

    /**
     * Method gets the UserProfile associated with the user.
     *
     * @return An UserProfile object associated to the user.
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * Method sets the field currentView with a String object representing the current view
     *
     * @param currentView String object representing the current view
     */
    public void setCurrentView(String currentView) {
        this.currentView = currentView;
    }

    /**
     * Method gets the field currentView
     *
     * @return A String object representing the current view
     */
    public String getCurrentView() {
        return currentView;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    // Should never reach below 0
    public void decreaseNumTransitionedStates() {
        this.numTransitionedStates--;
    }

    public int getNumTransitionedStates() {
        return numTransitionedStates;
    }

    public void setNumTransitionedStates(int numTransitionedStates) {
        this.numTransitionedStates = numTransitionedStates;
    }

    public void resetHasClickedOnce() {
        this.hasClickedOnce = false;
    }

    public void addToTimeAccumulated(long add) {
        this.timeAccumulation += add;
    }

    public void resetModel() {
        // Default is 0
        this.timeAccumulation = 0;
    }
}
