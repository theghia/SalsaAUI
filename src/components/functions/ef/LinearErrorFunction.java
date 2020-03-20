package components.functions.ef;

import components.functions.ErrorFunction;

/**
 * LinearErrorFunction Class that implements the ErrorFunction interface. This Class uses a linear function where f(x)
 * equals 0 when x is the timestamp of the left and right window, f(x) equals one when x is the timestamp of the
 * required beat
 *
 * @author Gareth Iguasnia
 * @date 07/03/2020
 */
public class LinearErrorFunction implements ErrorFunction {
    // The time it takes for one beat to go to another
    private long one_beat;

    // The timestamp of the left window that the system will take an input from the system
    private long left_time_window;

    private int BUFFER_SLICE = 3;

    /**
     * Constructor for the LinearErrorFunction. This is a one time use function as we need
     * @param one_beat The time in milliseconds for one beat to occur
     * @param left_time_window The time of the start of the time window
     */
    public LinearErrorFunction(long one_beat, long left_time_window) {
        this.one_beat = one_beat;
        this.left_time_window = left_time_window;
    }

    @Override
    public double calculateErrorValue(long clickTimeStamp, long correctTimeStamp) {
        // Normalising the input/x value
        long new_x = normaliseInput(clickTimeStamp, correctTimeStamp);
        System.out.println("Normalised x: " + new_x);

        double denominator = (double) one_beat * 3;
        double numerator = (double) new_x - left_time_window;

        // Taking into account human time reaction. The third is a value that can be adjusted.
        double time_reaction = Math.round( (double) one_beat/BUFFER_SLICE );
        double buffered_numerator = numerator + time_reaction;

        System.out.println("The denominator: " + denominator + " The numerator: " + numerator);
        double y = numerator/denominator;
        System.out.println("The y output is: " + y);
        return y;
    }

    /* Normalise the input to take into account timestamps after the desired beat's timestamp */
    private Long normaliseInput(long clickTimeStamp, long correctTimeStamp) {
        long normalised;
        if (clickTimeStamp > correctTimeStamp) {
            long difference = clickTimeStamp - correctTimeStamp;
            normalised = correctTimeStamp - difference;
        }
        else {
            normalised = clickTimeStamp;
        }
        return normalised;
    }
}
