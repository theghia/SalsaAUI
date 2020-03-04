package main;

import components.State;
import components.UserProfile;
import events.*;

import java.util.ArrayList;

/**
 * SalsaModel Class that represents the single Model in this MVC application
 *
 * @author Gareth Iguasnia
 * @date 03/03/2020
 */
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
     * Constructor of the SalsaModel Class. The fields are given their default values when the object is
     * constructed
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

    /**
     * Method adds a SimulationListener to this model
     *
     * @param simulationListener A Class that has implemented the SimulationListener interface
     */
    public void addSimulationListener(SimulationListener simulationListener) {
        this.simListeners.add(simulationListener);
    }

    /**
     * Method adds a ClipInformationListener to this model
     *
     * @param clipInfoListener A Class that has implemented the ClipInformationListener interface
     */
    public void addClipInformationListener(ClipInformationListener clipInfoListener) {
        this.clipInfoListener = clipInfoListener;
    }

    /**
     * Method adds a SimulationGUIListener to this model
     *
     * @param simGUIListener A Class that has implemented the SimulationGUIListener interface
     */
    public void addSimulationGUIListener(SimulationGUIListener simGUIListener) {
        this.simGUIListener = simGUIListener;
    }

    /* FIRE EVENT METHODS */

    /**
     * Method start the onSimulationStartedEvent(...) method for the SimulationListeners of this model. This will be
     * called at the start of each simulation run
     */
    public void fireSimulationStartEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listeners will execute whatever logic they have implemented
        for (SimulationListener simulationListener: this.simListeners)
            simulationListener.onSimulationStartedEvent(e);
    }

    /**
     * Method starts the onInitClipInfoReadyEvent(...) method for the ClipInformationListener of this model. This will
     * be called at the start of each simulation run and allows the SimulationController know the timings of the music
     * to act accordingly with the countdown audio clip and the first Salsa audio clip to be played.
     *
     * @param clip123 The length of the countdown audio clip
     * @param clipSalsa The length of the Salsa audio clip
     */
    public void fireClipInfoReadyEvent(long clip123, long clipSalsa) {
        ClipInformationEvent e = new ClipInformationEvent(this, clip123, clipSalsa);

        // The listener will execute whatever logic it has implemented
        this.clipInfoListener.onInitClipInfoReadyEvent(e);
    }

    /**
     * Method starts the onClipInfoReadyEvent(...) method for the ClipInformationListener of this model. This will be
     * called for every new State object traversed bar the first one and allows the SimulationController know the
     * timings of the Salsa audio clip to act accordingly.
     *
     * @param clipSalsa The length of the Salsa audio clip
     */
    public void fireClipInfoReadyEvent(long clipSalsa) {
        ClipInformationEvent e = new ClipInformationEvent(this, clipSalsa);

        // The listener will execute whatever logic it has implemented
        this.clipInfoListener.onClipInfoReadyEvent(e);
    }

    /**
     * Method starts the onNewStateEvent(...) method for all of the SimulationListeners of this model. This will be
     * called whenever a new State object will traversed after the first one.
     */
    public void fireNewStateEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listeners will execute whatever logic they have implemented
        for (SimulationListener simulationListener: this.simListeners)
            simulationListener.onNewStateEvent(e);
    }

    /**
     * Method starts the onNewBeatEvent(...) method for the SimulationGUIListener of this model. This will be called
     * whenever the simulation proceeds to a new 8-beat bar of music.
     */
    public void fireNewBeatEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onNewBeatEvent(e);
    }

    /**
     * Method starts the onSimulationFinishedEvent(...) method for the SimulationGUIListener of this model. This will
     * be called whenever the simulation run has ended.
     */
    public void fireSimulationFinishedEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onSimulationFinishedEvent(e);
    }

    /**
     * Method starts the onNewErrorValueEvent(...) method for the SimulationGUIListener of this model. This will be
     * called every time the user's input was successfully recorded during the simulation.
     */
    public void fireNewErrorValueEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onNewErrorValueEvent(e);
    }

    /**
     * Method starts the onCountdownStartedEvent(...) method for the SimulationGUIListener of this model. This will be
     * called once the start button has been clicked to begin the countdown to the simulation run.
     *
     * @param clip123 The length of the countdown audio clip
     * @param clipSalsa The length of the Salsa audio clip
     */
    public void fireCountdownStartedEvent(long clip123, long clipSalsa) {
        ClipInformationEvent e = new ClipInformationEvent(this, clip123, clipSalsa);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onCountdownStartedEvent(e);
    }

    /**
     * Method starts the onCountdownFinishedEvent(...) method for the SimulationGUIListener of this model. This will be
     * called after the countdown audio clip has finished to display the relevant GUI for the simulation.
     */
    public void fireCountdownFinishedEvent() {
        this.simGUIListener.onCountdownFinishedEvent();
    }

    /* CHANGE MODEL STATE */

    /**
     * Method decreases the value numTransitionedStates by 1 to represent that a State object has been traversed
     */
    public void decreaseNumTransitionedStates() {
        // Should never reach below 0
        this.numTransitionedStates--;
    }

    /**
     * Method adds the time of the audio clips being played so that we can normalise the timestamp taken when the user
     * clicks the beat-clicker
     *
     * @param add A long value representing the length of an audio clip that has been played in the simulation
     */
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

    /**
     * Method increases the windowTracker by 1 iff windowTracker currently has a value of 1
     */
    public synchronized void increaseWindowTracker() {
        if (this.windowTracker == 1)
            this.windowTracker++;
    }

    /**
     * Method decreases the windowTracker by 1 iff windowTracker currently has a value of 2
     */
    public synchronized void decreaseWindowTracker() {
        if (this.windowTracker == 2)
            this.windowTracker--;
    }

    /**
     * Method sets the buttonClickerTracker to 2
     */
    public synchronized void increaseButtonClickerTracker() {
        this.buttonClickerTracker = 2;
    }

    /**
     * Method sets the buttonClickerTracker to 1
     */
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

    /**
     * Method sets the number of transitioned States that the simulation will traverse for one simulation run
     *
     * @param numTransitionedStates Integer representing the number of States to traverse
     */
    public void setNumTransitionedStates(int numTransitionedStates) {
        this.numTransitionedStates = numTransitionedStates;
    }

    /**
     * Method sets the name of the user of this MVC instance
     *
     * @param nameOfUser String representing the name of the user
     */
    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    /**
     * Method sets the current State that the application is currently on. This will only be called during a simulation
     * run and when the simulation needs to traverse a new State.
     *
     * @param currentState A State object that the simulation has currently selected to traverse
     */
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    /**
     * Method sets the the next beat that the user will be requested to find in the music. This will only be called
     * during the simulation after every one 8-beat bar of music has played.
     *
     * @param nextBeat Integer representing the next beat that the system will test the user on
     */
    public void setNextBeat(int nextBeat) {
        this.currentBeat = this.nextBeat;
        this.nextBeat = nextBeat;
    }

    /**
     * Method records the error value from the user's input and adds it to the UserProfile
     *
     * @param errorValue Double representing the error value of the user
     */
    public void setErrorValue(double errorValue) {
        // This will only be called if the user clicks appropriately
        String id = userProfile.createID(currentState.getBpm(), currentState.getInstruments());

        // Put the error value in...
        userProfile.getStates().get(id).getErrorValues().add(errorValue);
    }

    /**
     * Method sets the beat timeline for the current 4 8-beat bars of Salsa music. Each value will be a long that
     * shows when that beat in time should occur
     *
     * @param beatTimeline An ArrayList of Long values representing when in time each beat (index + 1 ) should
     *                     have occurred
     */
    public void setBeatTimeline(ArrayList<Long> beatTimeline) {
        this.beatTimeline = beatTimeline;
    }

    /**
     * Method sets the countdownCurrentlyPlaying flag with a boolean variable. This will be true once the simulation
     * has ended and false once the countdown clip has finished playing
     *
     * @param countdownCurrentlyPlaying Boolean flag representing whether the countdown audio clip has finished playing
     */
    public void setCountdownCurrentlyPlaying(boolean countdownCurrentlyPlaying) {
        this.countdownCurrentlyPlaying = countdownCurrentlyPlaying;
    }

    /**
     * Method sets the hasClickedOnce1 flag with the boolean parameter. This will be true if the user has clicked in
     * the time window or there is no time window currently open for this flag.
     *
     * @param hasClickedOnce1 Boolean variable indicating whether the system will take the user's input
     */
    public synchronized void setHasClickedOnce1(boolean hasClickedOnce1) {
        this.hasClickedOnce1 = hasClickedOnce1;
    }

    /**
     * Method sets the hasClickedOnce2 flag with the boolean parameter. This will be true if the user has clicked in
     * the time window or there is no time window currently open for this flag.
     *
     * @param hasClickedOnce2 Boolean variable indicating whether the system will take the user's input
     */
    public synchronized void setHasClickedOnce2(boolean hasClickedOnce2) {
        this.hasClickedOnce2 = hasClickedOnce2;
    }

    /**
     * Method sets a value to the field barNumber in the model. This value can only be from 1 - 4
     *
     * @param barNumber Integer representing the bar number of a Salsa Audio clip
     */
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

    /**
     *  Method returns the timeAccumulation field
     *
     * @return Long value representing the total time that the system has currently been playing music for the current
     * simulation run
     */
    public long getTimeAccumulation() {
        return timeAccumulation;
    }

    /**
     * Method returns the countdownCurrentlyPlaying field
     *
     * @return A boolean representing if the countdown audio clip is playing
     */
    public boolean isCountdownCurrentlyPlaying() {
        return countdownCurrentlyPlaying;
    }

    /**
     * Method returns the numTransitionedStates field
     *
     * @return An integer representing the number of states that can still be transitioned by the Simulation
     */
    public int getNumTransitionedStates() {
        return numTransitionedStates;
    }

    /**
     * Method returns the nameOfUser field
     *
     * @return A String representing the name of the user using this application
     */
    public String getNameOfUser() {
        return nameOfUser;
    }

    /**
     * Method returns the currentState field
     *
     * @return A State object representing the current state that the simulation is currently on
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     *  Method returns the currentState field
     *
     * @return An ArrayList of longs representing when each beat should occur in normalised time
     */
    public ArrayList<Long> getBeatTimeline() {
        return beatTimeline;
    }

    /**
     *  Method returns the hasClickedOnce1 field
     *
     * @return A boolean representing whether a user has clicked before in the allocated time or whether they missed
     * their chance to click.
     */
    public boolean hasClickedOnce1() {
        return hasClickedOnce1;
    }

    /**
     *  Method returns the hasClickedOnce2 field
     *
     * @return A boolean representing whether a user has clicked before in the allocated time or whether they missed
     * their chance to click.
     */
    public boolean hasClickedOnce2() {
        return hasClickedOnce2;
    }

    /**
     * Method returns the windowTracker field
     *
     * @return An integer representing the time window that is still active
     */
    public int getWindowTracker() {
        return this.windowTracker;
    }

    /**
     * Method returns the buttonClickerTracker field
     *
     * @return An integer representing the previous time window
     */
    public int getButtonClickerTracker() {
        return this.buttonClickerTracker;
    }

    /**
     * Method returns the currentBeat field
     *
     * @return An integer representing the current beat that the system is requesting the user to locate in the music
     */
    public int getCurrentBeat() {
        return currentBeat;
    }

    /**
     * Method returns the nextBeat field
     *
     * @return An integer representing the next beat that the system will test the user's timing on
     */
    public int getNextBeat() {
        return nextBeat;
    }

    /**
     * Method returns the barNumber field
     *
     * @return An int that presents the number of bars traversed in one group of 4 8-beat bars
     */
    public int getBarNumber() {
        return barNumber;
    }
}