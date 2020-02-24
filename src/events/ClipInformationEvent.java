package events;

import java.util.EventObject;

public class ClipInformationEvent extends EventObject {

    // Should hold information on the clip information
    // Here (or in the SimulationController), logic will split up the time that it will take for the clip to play
    // into 4 equal parts to determine the length of each 8 beat bar of music.
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ClipInformationEvent(Object source) {
        super(source);
    }
}
