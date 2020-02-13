package components;

import org.apache.commons.math3.analysis.function.Gaussian;

/**
 * GaussianErrorFunction Class that implements the interface ErrorFunction. This object will be used to determine
 * how close the user's input is to the beat that the application requested them to identify.
 *
 * @author Gareth Iguasnia
 * @date 01/02/2020
 */
public class GaussianErrorFunction implements ErrorFunction {
    // This value will not change as part of the specification for this project
    private final double SIGMA = 1/(Math.sqrt(2 * Math.PI));

    @Override
    public double calculateErrorValue(long clickTimeStamp, long correctTimeStamp) {
        // Setting up the Gaussian function with the mean and sigma
        Gaussian gFunction = new Gaussian((double) correctTimeStamp/1000, SIGMA);

        // Calculating the value by using the timestamp of the user's input
        return gFunction.value((double) clickTimeStamp/1000);
    }

    public static void main(String[] args) {
        double test1 = 0.1;
        double test2 = 3.60;

        Gaussian gfunc = new Gaussian(1,test2, 1/(Math.sqrt(2 * Math.PI)));

        System.out.println(gfunc.value(test1));
    }
}
