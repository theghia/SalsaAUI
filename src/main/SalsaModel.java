package main;

import components.State;
import components.UserProfile;
import events.ClipInformationListener;
import events.SimulationEvent;
import events.SimulationListener;

import java.util.ArrayList;

public class SalsaModel {

    // Name of the user of the application
    private String nameOfUser;

    // To cache the current view
    private String currentView = null;

    // Holds information on the user's ability in finding timing on the different combinations of instruments and tempo
    private UserProfile userProfile;

    // To know what the current state the simulation is at when it is running
    private State currentState;

    // To keep track of what beats we require the user to find
    private int currentBeat = -1;
    private int nextBeat = -1;

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

    public void fireSimulationStartEvent() {
        SimulationEvent e = new SimulationEvent(this);

        // The listeners will execute whatever logic they have implemented
        for (SimulationListener simulationListener: this.simListeners)
            simulationListener.onSimulationStartedEvent(e);
    }

    // The fire method for fireClipInfoReadyEvent(long 123clip, long salsa1) --> onInitClipInfoReadyEvent
    //

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
}
