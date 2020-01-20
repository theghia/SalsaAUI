package salsa.controller;

import salsa.view.SalsaFrame;

/**
 * Controller object for the MVC salsa application
 *
 * @author Gareth Iguasnia
 * @date 17/01/2020
 */
public class SalsaAppController {

    // Reference to the application SalsaFrame
    private SalsaFrame appFrame;

    /**
     * Sets up the MVC application
     */
    public void start() {

        appFrame = new SalsaFrame(this);

    }
}
