package components;

/**
 * BPM Enum Class that holds information on the different tempos that will be used in the application to test the
 * user.
 *
 * @author Gareth Iguasnia
 * @date 12/02/2020
 */
public enum BPM {
    SLOW(180),
    MEDIUM(200),
    FAST(220);

    private int BPM;

    /**
     * Constructor for the BPM Enum class
     *
     * @param tempo Integer representing the beats per minute
     */
    BPM(int tempo) {
        this.BPM = tempo;
    }
}
