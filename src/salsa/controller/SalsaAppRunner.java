package salsa.controller;

/**
 * Runner object for the MVC salsa application
 *
 * @author Gareth Iguasnia
 * @date 17/01/2020
 */
public class SalsaAppRunner {

    /**
     * Main starter method or entry point for the Java Program
     *
     * @param args Unused as this is a GUI salsa app
     */
    public static void main(String[] args) {
        SalsaAppController baseApp = new SalsaAppController();
        baseApp.start();
    }
}
