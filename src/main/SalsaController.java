package main;

/**
 * SalsaController Class that shall be derived by all controllers in this MVC application
 *
 * @author Gareth Iguasnia
 * @date 28/01/2020
 */
public abstract class SalsaController {

    // So that each controller can have access to the model to manipulate the data accordingly
    private SalsaModel salsaModel;

    // To identify what controller this is
    private String controllerName;

    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public SalsaController(SalsaModel salsaModel, String controllerName) {
        this.salsaModel = salsaModel;
        this.controllerName = controllerName;
    }

    /**
     * Method returns the SalsaModel object of the controller
     *
     * @return A SalsaModel object representing the model of the object
     */
    public SalsaModel getSalsaModel() { return salsaModel; }

    /**
     * Method returns the name of the controller
     *
     * @return A String object representing the name of the controller
     */
    public String getControllerName() { return controllerName; }
}