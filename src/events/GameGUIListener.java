package events;

import java.util.EventListener;

/**
 * GameGUIListener interface will implemented by classes that deal with the GUI movement for when the game to test the
 * user's is in play.
 *
 * @author Gareth Iguasnia
 * @date 13/03/2020
 */
public interface GameGUIListener extends EventListener {
    /**
     * This method deals with the event of a new 8-beat bar starting.
     *
     * @param e A GameEvent object that will contain information on what the next beat the user will be tested on
     */
    void onNewBeatEvent(GameEvent e);

    /**
     * This method deals with the event of when an Error Value is recorded from the user's input
     *
     * @param e A GameEvent object that will be used to pass the information of the new Error Value to the
     *          listener.
     */
    void onNewErrorValueEvent(GameEvent e);

    /**
     * This method deals with the event of when the Simulation has finished. Cleans up the GUI ready for the next
     * time the simulation is started again.
     *
     * @param e A GameEvent object that will be used to reset the model (except the UserProfile) back to default
     */
    void onGameFinishedEvent(GameEvent e);

    /**
     * This method deals with the event of when the countdown audio clip has started to play. This method will display
     * the numbers on the screen as the audio countdowns from 5 to 0
     */
    void onCountdownStartedEvent();

    /**
     * This method deals with the event of when the countdown audio clip has finished playing
     */
    void onCountdownFinishedEvent();

    /**
     * This method deals with the event when the lights need to be turned on to display the timing of a Salsa audio
     * clip. The model should have been updated with a beat timeline of the new Salsa audio clip that is going to be
     * played.
     */
    void onLightsTurnOn();
}
