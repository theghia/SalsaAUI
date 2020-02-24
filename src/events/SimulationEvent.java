package events;

import java.util.EventObject;

public class SimulationEvent extends EventObject {

    double errorValue;

    public SimulationEvent(Object source) {
        super(source);
    }

    // All objects that will be passed through here are going to be MODELSSSS
    // so that the model does not have to be involved with any logic bar:
    // SimulationEvent e = new SimulationEvent(this); BUUUT this is the modelllllll
    // this.listener...onSimulationStartEvent(e);

    // To be used with onNewErrorValue as it will be hard to know which was the most recent value added
    public SimulationEvent(Object source, double errorValue) {
        super(source);
        this.errorValue = errorValue;
    }

}
