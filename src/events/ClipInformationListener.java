package events;

import java.util.EventListener;

/**
 * ClipInformationListener Interface that will be implemented by the class that needs to be notified once information
 * of the created Clip object, from the desired WAV file, has been extracted. This should be used to then to monitor
 * when the next clip should be created and when a new bar of 8 beats of music have transpired
 */
public interface ClipInformationListener extends EventListener {

    //  * This event will only occur at the start of the simulation as we will be taking into account the size of the
    //     * clip of the WAV file that will play "3-2-1".
    /**
     * This method deals with the event that the simulation has started and will use the size of the Salsa WAV file
     * that will be played first and the size of the clip of the WAV file that will play "3-2-1"
     *
     * @param e A ClipInformationEvent that will pass on information on the total Clip size to the listener
     */
    void onInitClipInfoReadyEvent(ClipInformationEvent e);

    //This event will continue occurring during the simulation, after the first initial event was fired, to prepare
    //     * for when the next Clip should be created, and consequently played, and for when the GUI should change throughout
    //     * the Clip playing the music
    /**
     * This method deals with the event when a new State has been reached and a new Clip has been prepared to be played
     * next in the simulation and will only consider the size of the Salsa WAV file to be played next.
     *
     * @param e A ClipInformationEvent object that will pass on information on the total Clip size to the listener
     */
    void onClipInfoReadyEvent(ClipInformationEvent e);
}
