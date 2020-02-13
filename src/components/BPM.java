package components;

/**
 * BPM Enum Class that holds information on the different tempos that will be used in the application to test the
 * user.
 *
 * @author Gareth Iguasnia
 * @date 12/02/2020
 */
public enum BPM {
    SLOW(180, 'a'),
    MEDIUM(200, 'b'),
    FAST(220, 'c');

    private int BPM;
    private char id;

    /**
     * Constructor for the BPM Enum class
     *
     * @param tempo Integer representing the beats per minute
     */
    BPM(int tempo, char id) {
        this.BPM = tempo;
        this.id = id;
    }

    public int getBPM() {
        return BPM;
    }

    public char getId() {
        return id;
    }
}
