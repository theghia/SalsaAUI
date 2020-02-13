package main;

import components.State;

public class SalsaModel {

    // Name of the user of the application
    private String nameOfUser;

    // To cache the current view
    private String currentView = null;

    // To know what the current state the simulation is at when it is running
    private State currentState;

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
}
