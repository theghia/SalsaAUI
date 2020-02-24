package components;

/**
 * ErrorFunction interface that will be implemented by the error function chosen to be used in this MVC application.
 * This makes the code extensible as I can swap out different error function without affecting the application's
 * overall behaviour.
 *
 * @author Gareth Iguasnia
 * @date 30/01/2020
 */
public interface ErrorFunction {

    /**
     * Method calculates the error value of the user's perceived notion of the timing of the music. The closer
     * the value is to 1, the closer the user's input was to the correct beat. The value should only range between
     * 0 and 1.
     *
     * @param clickTimeStamp The timestamp in which the user clicked the button
     * @param correctTimeStamp The timestamp in which the correct beat actually occurred
     * @return A Double object representing the "goodness" of the user's input
     */
    double calculateErrorValue(long clickTimeStamp, long correctTimeStamp);
}
