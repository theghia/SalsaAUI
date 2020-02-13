package components;

/**
 * Instrument Enum Class that holds information on each of the instruments such as its name in a String format
 * and given a unique ID in order to create different combinations of instruments
 *
 * @author Gareth Iguasnia
 * @date 12/02/2020
 */
public enum Instrument {
    PIANO(1, "piano"),
    TIMBALES(2, "timbales"),
    BASS(3, "bass"),
    CLAVE(4, "clave");

    // Unique ID
    private int value;

    // Name in a string format - all lowercase
    private String name;

    /**
     * Constructor for the Instrument Enum Class
     *
     * @param value Integer that is the unique ID for each instrument
     * @param name The name of the object in a String format
     */
    Instrument(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
