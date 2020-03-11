package views;

import java.awt.*;

/**
 * SimulationView class extends the GameView class. This view will not display the lights since we want to test the
 * user's ability.
 *
 * @author Gareth Iguasnia
 * @date 11/03/2020
 */
public class SimulationView extends GameView {
    /**
     * Constructor for the SimulationView class
     *
     * @param viewName  String variable representing the name of the view
     * @param dimension Dimension object representing the size needed for the JPanel to fit the JFrame
     */
    public SimulationView(String viewName, Dimension dimension) {
        super(viewName, dimension);
    }
}
