package listeners;

import events.GameEvent;

import java.util.EventListener;

/**
 * GameProgressionListener Interface that will need to be implemented by the class that needs to be notified every time the
 * music has gone to a new 8 beat bar, has completed 4 8-beat bars of music, has recorded an error value from the user
 * input and when the simulation has started.
 */
public interface GameProgressionListener extends EventListener {

    /**
     * This method deals with the event of the Simulation starting.
     *
     * @param e A GameEvent object that will be used to initialise the GUI and Sound accordingly
     */
    void onGameStartedEvent(GameEvent e);

    /**
     * This method deals with the event of when the simulation moves onto a new State.
     *
     * @param e A GameEvent object that will be used to take the necessary action on moving to a new State
     */
    void onNewStateEvent(GameEvent e);
}
