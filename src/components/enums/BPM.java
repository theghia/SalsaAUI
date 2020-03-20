package components.enums;

/**
 * BPM Enum Class that holds information on the different tempos that will be used in the application to test the
 * user. A maximum of 25 different tempo's can be used in this application.
 *
 * @author Gareth Iguasnia
 * @date 12/02/2020
 */
public enum BPM {
    SLOW(180, 'a', "Slow"),
    MEDIUM(200, 'b', "Medium"),
    FAST(220, 'c', "Fast");

    private int BPM;
    private char id;
    private String name;

    /**
     * Constructor for the BPM Enum class
     *
     * @param tempo Integer representing the beats per minute
     * @param id The ID assigned to the BPM object
     * @param name String description of the BPM object
     */
    BPM(int tempo, char id, String name) {
        this.BPM = tempo;
        this.id = id;
        this.name = name;
    }

    /**
     * Method returns the bpm
     *
     * @return An integer representing the bpm of the BPM object
     */
    public int getBPM() {
        return BPM;
    }

    /**
     * Method returns the ID associated to the BPM object
     *
     * @return A character representing the ID of the BPM object
     */
    public char getId() {
        return id;
    }

    /**
     * Method returns the description speed of the tempo in a String format
     *
     * @return A String representing the speed of the tempo
     */
    public String getName() {
        return name;
    }
}
