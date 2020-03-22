package main;

import components.State;
import components.UserProfile;
import events.*;
import listeners.ClipInformationListener;
import listeners.GameGUIListener;
import listeners.GameProgressionListener;
import listeners.TutorialGUIListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * SalsaModel Class that represents the single Model in this MVC application
 *
 * @author Gareth Iguasnia
 * @date 03/03/2020
 */
public class SalsaModel implements Serializable {

    // Name of the user of the application
    private String nameOfUser;

    // File path of the folder that holds the game data
    private String DATA = "src/data/";

    // To cache the current view
    private String currentView;

    // Holds information on the user's ability in finding timing on the different combinations of instruments and tempo
    private UserProfile userProfile;

    // To know what the current state the simulation is at when it is running
    private volatile State currentState;

    // To keep track of what beats we require the user to find
    private volatile int currentBeat;
    private volatile int nextBeat;
    private volatile int barNumber;

    // Keeping track whether the user has previously clicked before during each 8-beat bar of music
    // and which beat pertains to which time window.
    // Variables have been set as volatile since different threads will be accessing this data
    private volatile boolean hasClickedOnce1;
    private volatile boolean hasClickedOnce2;
    private volatile int windowTracker;
    private volatile int buttonClickerTracker;
    private AtomicIntegerArray windowCaching;
    private AtomicIntegerArray beatCaching;
    private AtomicIntegerArray barCaching;
    private volatile int beatCacheTracker;

    // To keep track of the number of State objects we have transitioned
    private int numTransitionedStates;

    // A beat timeline so that we can use the error function to compare the user's input to the correct timing
    private volatile ArrayList<Long> beatTimeline;

    // The 4 beats that the user will be tested on in this State run
    private volatile ArrayList<Integer> testingBeats;

    // To normalise the timestamp of the user's input
    private volatile long timeAccumulation;

    // Flag to determine whether the countdown audio clip is playing or a Salsa audio clip
    private volatile boolean countdownCurrentlyPlaying;

    // GameProgressionListeners for both the Tutorial and Simulation controllers
    private ArrayList<GameProgressionListener> simListeners;
    private GameProgressionListener tutProgressGUIListener;
    private GameProgressionListener tutProgressMusicListener;

    // This listener will be used with the HardSimulationController Class
    private ClipInformationListener clipInfoListener;
    private ClipInformationListener tutClipInfoListener;

    // This listener will only be used with the HardSimulationGUIController Class
    private GameGUIListener simGUIListener;
    private GameGUIListener tutGUIListener;

    // This listener will only be used with the TutorialGUIController
    private TutorialGUIListener tutorialGUIListener;

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

        // Setting the cache for the bar number of that the State is in to know which time window pertains to which
        // bar number
        this.barCaching = new AtomicIntegerArray(2);
        this.barCaching.set(0, 0);
        this.barCaching.set(1, 0);

        // Setting the window cache to know which time window should be closed when they are overlapping
        this.windowCaching = new AtomicIntegerArray(2);
        this.windowCaching.set(0, 0);
        this.windowCaching.set(1, 0);

        // Setting up the beat cache to know which beat pertains to which time window
        this.beatCaching = new AtomicIntegerArray(2);
        this.beatCaching.set(0, 0);
        this.beatCaching.set(1, 0);

        this.beatCacheTracker = 0;

        // Default will be 0
        this.timeAccumulation = 0;

        // Default will be true
        this.countdownCurrentlyPlaying = true;
    }

    /* Methods to add listeners to the Model */

    /**
     * Method adds a GameProgressionListener to the simListeners.
     * This will be connected to the HardSimulationView
     *
     * @param gameProgressionListener A Class that has implemented the GameProgressionListener interface
     */
    public void addSimulationListener(GameProgressionListener gameProgressionListener) {
        this.simListeners.add(gameProgressionListener);
    }

    /**
     * Method adds a GameProgressionListener to the tutProgressionMusicListener field. This will be used to manage the
     * GUI in the TutorialView for when the game is in progress.
     * This will be connected to the TutorialView
     *
     * @param tutProgressMusicListener A class that has implemented the GameProgressionListener interface
     */
    public void addTutorialGUIListener(GameProgressionListener tutProgressMusicListener) {
        this.tutProgressGUIListener = tutProgressMusicListener;
    }

    /**
     * Method adds a GameProgressionListener to the tutProgressionGUIListener field. This will be used to manage the
     * music in the TutorialView for when the game is in progress.
     * This will be connected to the TutorialView
     *
     * @param tutProgressGUIListener
     */
    public void addTutorialMusicListener(GameProgressionListener tutProgressGUIListener) {
        this.tutProgressMusicListener = tutProgressGUIListener;
    }

    /**
     * Method adds a ClipInformationListener to the clipInfoListeners.
     * This will be connected to the HardSimulationView.
     *
     * @param clipInfoListener A Class that has implemented the ClipInformationListener interface
     */
    public void addClipInformationListener(ClipInformationListener clipInfoListener) {
        this.clipInfoListener = clipInfoListener;
    }

    /**
     * Method adds a ClipInformationListener to the tutClipInfoListener field.
     * This will be connected to the TutorialView.
     *
     * @param tutClipInfoListener
     */
    public void addTutorialClipInformationListener(ClipInformationListener tutClipInfoListener) {
        this.tutClipInfoListener = tutClipInfoListener;
    }

    /**
     * Method adds a GameGUIListener to the simGUIListener.
     * This will be connected to the HardSimulationView.
     *
     * @param simGUIListener A Class that has implemented the GameGUIListener interface
     */
    public void addSimulationGUIListener(GameGUIListener simGUIListener) {
        this.simGUIListener = simGUIListener;
    }

    /**
     * Method adds a GameGUIListener to the tutGUIListener. This will be used to manage the GUI for when the game is in
     * action in the TutorialView.
     * This will be connected to the TutorialView.
     *
     * @param tutGUIListener
     */
    public void addTutorialGameGUIListener(GameGUIListener tutGUIListener) {
        this.tutGUIListener = tutGUIListener;
    }

    /**
     * Method to add a TutorialGUIListener to tutorialGUIListener. This is to deal with GUI that comes up to teach the
     * user how to use the system.
     * This will only be connected to the TutorialView
     *
     * @param tutorialGUIListener A class that has implemented the TutorialGUIListener interface
     */
    public void addTeachTutorialGUIListener(TutorialGUIListener tutorialGUIListener) {
        this.tutorialGUIListener = tutorialGUIListener;
    }

    /* FIRE EVENT METHODS */

    /**
     * Method start the onSimulationStartedEvent(...) method for the SimulationListeners of this model. This will be
     * called at the start of each simulation run
     */
    public void fireSimulationStartEvent() {
        GameEvent e = new GameEvent(this);

        // The listeners will execute whatever logic they have implemented
        for (GameProgressionListener gameProgressionListener : this.simListeners)
            gameProgressionListener.onGameStartedEvent(e);
    }

    /**
     * Method starts the onClipInfoReadyEvent(...) method for the ClipInformationListener of this model. This will be
     * called for every new State object traversed bar the first one and allows the HardSimulationController know the
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
        GameEvent e = new GameEvent(this);

        // The listeners will execute whatever logic they have implemented
        for (GameProgressionListener gameProgressionListener : this.simListeners)
            gameProgressionListener.onNewStateEvent(e);
    }

    /**
     * Method starts the onNewBeatEvent(...) method for the GameGUIListener of this model. This will be called
     * whenever the simulation proceeds to a new 8-beat bar of music.
     */
    public void fireNewBeatEvent() {
        GameEvent e = new GameEvent(this);

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onNewBeatEvent(e);
    }

    /**
     * Method starts the onSimulationFinishedEvent(...) method for the GameGUIListener of this model. This will
     * be called whenever the simulation run has ended.
     */
    public void fireSimulationFinishedEvent() {
        GameEvent e = new GameEvent(this);

        // The two listeners will be notified. This is to save rewriting more classes
        this.simGUIListener.onGameFinishedEvent(e);
        this.tutGUIListener.onGameFinishedEvent(e);
    }

    /**
     * Method starts the onNewErrorValueEvent(...) method for the GameGUIListener of this model. This will be
     * called every time the user's input was successfully recorded during the simulation.
     */
    public void fireNewErrorValueEvent(GameEvent e) {

        // The listener will execute whatever logic that has been implemented by the SimGUIController
        this.simGUIListener.onNewErrorValueEvent(e);
    }

    /**
     * Method starts the onCountdownStartedEvent() method for the GameGUIListener of this model. This will be
     * called once the start button has been clicked to begin the countdown to the simulation run.
     */
    public void fireCountdownStartedEvent() {
        this.simGUIListener.onCountdownStartedEvent();
    }

    /**
     * Method starts the onCountdownFinishedEvent(...) method for the GameGUIListener of this model. This will be
     * called after the countdown audio clip has finished to display the relevant GUI for the simulation.
     */
    public void fireCountdownFinishedEvent() {
        this.simGUIListener.onCountdownFinishedEvent();
    }

    /**
     *  Method starts the countdown GUI that displays the number according to the audio clip counting down. This is
     *  only for the Tutorial view
     */
    public void fireTutorialCountdownStartedEvent() {

        this.tutGUIListener.onCountdownStartedEvent();
    }

    /**
     * Method cleans up the GUI used in the countdown and starts the game to test the user's ability to find the
     * correct timing. This is only for the TutorialView
     */
    public void fireTutorialCountdownFinishedEvent() {
        this.tutGUIListener.onCountdownFinishedEvent();
    }

    /**
     * Method to keep progressing through the game. This is only associated for the TutorialView.
     */
    public void fireTutorialNewStateEvent() {
        GameEvent e = new GameEvent(this);

        // Execute logic found in the TutorialGUIController and TutorialMusicController that has implemented
        this.tutProgressGUIListener.onNewStateEvent(e);
        this.tutProgressMusicListener.onNewStateEvent(e);
    }

    /**
     * Method to pass on information to listeners on the length of the salsa audio clips. This will only be associated
     * with the TutorialView
     *
     * @param clipSalsaLength Long value representing the length of the Salsa audio clip
     */
    public void fireTutorialClipInfoReadyEvent(long clipSalsaLength) {
        ClipInformationEvent e = new ClipInformationEvent(this, clipSalsaLength);

        this.tutClipInfoListener.onClipInfoReadyEvent(e);
    }

    /**
     * Method to display the error value on the gauge. This is only associated to the TutorialView.
     *
     * @param gameEvent GameEvent object that will contain the current error value that has been calculated
     */
    public void fireTutorialNewErrorValueEvent(GameEvent gameEvent) {
        this.tutGUIListener.onNewErrorValueEvent(gameEvent);
    }

    /**
     * Method to start the Tutorial for the game. This will occur before the simulation music starts playing. This is
     * only associated with the TutorialView.
     */
    public void fireTutorialStartEvent() {
        this.tutorialGUIListener.onTutorialStarted();
    }

    public void fireTutorialFinishedEvent() {
        this.tutorialGUIListener.onTutorialFinished();
    }

    public void fireTutorialGameStartEvent() {
        GameEvent e = new GameEvent(this);

        // Displaying the starting screen of the game view
        this.tutProgressGUIListener.onGameStartedEvent(e);
        this.tutProgressMusicListener.onGameStartedEvent(e);
    }

    /**
     * Method to display the digital numbers according to the current and next beats that the system is currently and
     * will test the user on
     */
    public void fireTutorialNewBeatEvent() {
        GameEvent e = new GameEvent(this);

        this.tutGUIListener.onNewBeatEvent(e);
    }

    /**
     * Method to display the timing of the Salsa audio clip through lights. The first light represents beat 1 of the
     * bar of music and the eighth light represents the eighth beat of bar of music.
     */
    public void fireLightsOnEvent() {
        this.tutGUIListener.onLightsTurnOn();
    }

    public void fireTutorialFinalEvent() {
        this.tutorialGUIListener.onFinalTutorial();
    }

    public void fireTutorialKeepInstrumentsAndTempo() {
        this.tutorialGUIListener.onKeepInstrumentsAndTempo();
    }

    public void fireTutorialKeepCurrentBeat() {
        this.tutorialGUIListener.onKeepCurrentBeat();
    }

    public void fireTutorialKeepNextBeat() {
        this.tutorialGUIListener.onKeepNextBeat();
    }

    public void fireTutorialKeepClicker() {
        this.tutorialGUIListener.onKeepClicker();
    }

    public void fireTutorialKeepGauge() {
        this.tutorialGUIListener.onKeepGauge();
    }

    public void fireTutorialStartScreen() {
        this.tutorialGUIListener.onStartScreenEvent();
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

        // Resetting the cache
        this.beatCacheTracker = 0;
        for (int i = 0; i < 2; i ++) {
            this.beatCaching.set(i, 0);
            this.windowCaching.set(i, 0);
        }
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

    /**
     * Method increases the beatCacheTracker by 1
     */
    public synchronized void increaseBeatCacheTracker() {
        if (this.beatCacheTracker < 4)
            this.beatCacheTracker++;
    }

    /**
     * Method resets the beatCacheTracker back to 0
     */
    public synchronized void resetBeatCacheTracker() {
        this.beatCacheTracker = 0;
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

    /**
     * Method sets a new timestamp that will serve as the time that the first beat in the group of 4 8-beat bars would
     * have theoretically occurred.
     *
     * @param timeAccumulation A Long object that represents the time of the first beat in the group of 4 8-beat bars
     */
    public void setTimeAccumulation(long timeAccumulation) {
        this.timeAccumulation = timeAccumulation;
    }

    /**
     * Method sets an ArrayList of integers to the field testingBeats that represents the beats that the user will be
     * tested on this State
     *
     * @param testingBeats An ArrayList of integers of size 4
     */
    public void setTestingBeats(ArrayList<Integer> testingBeats) {
        this.testingBeats = testingBeats;
    }

    public void setCurrentBeat(int currentBeat) {
        this.currentBeat = currentBeat;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
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

    /**
     * Method returns an ArrayList containing integers that represents the beats that the system will request the user
     * to locate in this State
     *
     * @return An ArrayList if integers of size 4
     */
    public ArrayList<Integer> getTestingBeats() {
        return testingBeats;
    }

    /**
     * Method returns the tracker used to get the current beat that the system is testing the user on
     *
     * @return An integer representing the index to be used for the testingBeats
     */
    public int getBeatCacheTracker() {
        return beatCacheTracker;
    }

    /**
     * Method returns the cache of the current window time lines that are open
     *
     * @return An AtomicIntegerArray where the first index represents one window, and the second index represents
     * the other window. Only 2 windows can be up at a time.
     */
    public AtomicIntegerArray getWindowCaching() {
        return windowCaching;
    }

    /**
     * Method returns the cache of the current beat according to which time windows are open
     *
     * @return An AtomicIntegerArray where the first index represents the beat that the user needs to locate in
     * Time Window 1, and the second index is the beat that the user needs to locate in Time Window 2
     */
    public AtomicIntegerArray getBeatCaching() {
        return beatCaching;
    }

    /**
     * Method return the cache of the current bar according to which time windows are open
     *
     * @return An AtomicIntegerArray where the first index represents the bar number for Time Window 1 and the second
     * index is the bar number for Time Window 2
     */
    public AtomicIntegerArray getBarCaching() {
        return barCaching;
    }

    /**
     * Method to get the file path of where the Game data is saved
     *
     * @return A String representing the file path of the "data" folder
     */
    public String getDATA() {
        return DATA;
    }
}