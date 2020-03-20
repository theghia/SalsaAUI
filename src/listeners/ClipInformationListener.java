package listeners;

import events.ClipInformationEvent;

import java.util.EventListener;

/**
 * ClipInformationListener Interface that will be implemented by the class that needs to be notified once information
 * of the created Clip object, from the desired WAV file, has been extracted. This should be used to then to monitor
 * when the next clip should be created and when a new bar of 8 beats of music have transpired
 */
public interface ClipInformationListener extends EventListener {

    /**
     * This method deals with the event when a new State has been reached and a new Clip has been prepared to be played
     * next in the simulation and will only consider the size of the Salsa WAV file to be played next.
     *
     * @param e A ClipInformationEvent object that will pass on information on the total Clip size to the listener
     */
    void onClipInfoReadyEvent(ClipInformationEvent e);
}
