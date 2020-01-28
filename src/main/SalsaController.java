package main;

public abstract class SalsaController {

    // So that each controller can have access to the model to manipulate the data accordingly
    private SalsaModel salsaModel;

    // To identify what controller this is
    private String controllerName;

    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel A SalsaModel object that
     * @param controllerName
     */
    public SalsaController(SalsaModel salsaModel, String controllerName) {
        this.salsaModel = salsaModel;
        this.controllerName = controllerName;
    }

    public SalsaModel getSalsaModel() {
        return salsaModel;
    }

    public String getControllerName() {
        return controllerName;
    }
}