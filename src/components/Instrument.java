package components;

/**
 * Instrument Enum Class that holds information on each of the instruments such as its name in a String format
 * and given a unique ID in order to create different combinations of instruments. Only a maximum number of
 * 10 instruments can be used in this application.
 *
 * @author Gareth Iguasnia
 * @date 12/02/2020
 */
public enum Instrument {
    PIANO(0, "piano"),
    TIMBALES(1, "timbales"),
    BASS(2, "bass"),
    CLAVE(3, "clave");

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

    /**
     * Method returns the unique ID associated to the Instrument object
     *
     * @return An integer representing the ID of the associated Instrument object
     */
    public int getValue() {
        return value;
    }

    /**
     * Method returns the name of the instrument
     *
     * @return A String object representing the name of the instrument associated to the Instrument object
     */
    public String getName() {
        return name;
    }
}
