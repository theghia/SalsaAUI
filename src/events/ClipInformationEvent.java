package events;

import java.util.EventObject;

/**
 * ClipInformationEvent Class that extends the EventObject Class. This event will be used to notify the
 * SimulationController of when a sound clip has been created.
 *
 * @author Gareth Iguasnia
 * @date 24/02/2020
 */
public class ClipInformationEvent extends EventObject {
    // The length of the clip of the Salsa audio file in milliseconds
    private long clipSalsa;

    // The length of the clip of the 123 audio file in milliseconds
    private long clip123;

    /**
     * Constructor 1: Creates the ClipInformationEvent that will be used by the SimulationController Class. This
     * event will only be used at the start of the simulation.
     *
     * @param source Object that is castable to a model
     * @param clip123 Long value that represents the length of the "123" audio clip in milliseconds
     * @param clipSalsa Long value that represents the length of the salsa audio clip in milliseconds
     */
    public ClipInformationEvent(Object source, long clip123, long clipSalsa) {
        super(source);
        this.clip123 = clip123;
        this.clipSalsa = clipSalsa;
    }

    /**
     * Constructor 2: Creates the ClipInformationEvent that will be used by the SimulationController Class. This event
     * will not be used at the start of the simulation but thereafter until the simulation ends.
     *
     * @param source Object that is castable to a model
     * @param clipSalsa Long value that represents the length of the salsa audio clip in milliseconds
     */
    public ClipInformationEvent(Object source, long clipSalsa) {
        super(source);
        this.clipSalsa = clipSalsa;
    }

    /**
     * Method returns the long value clipSalsa
     *
     * @return A long value representing the length of the salsa audio clip in milliseconds
     */
    public long getClipSalsa() {
        return clipSalsa;
    }

    /**
     * Method returns the long value clip123
     *
     * @return A long value representing the length of the "123" audio clip in milliseconds
     */
    public long getClip123() {
        return clip123;
    }
}
