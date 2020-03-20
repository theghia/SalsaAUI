package views.games;

import views.GameView;

import java.awt.*;

/**
 * HardSimulationView class extends the GameView class. This view will not display the lights since we want to test the
 * user's ability.
 *
 * @author Gareth Iguasnia
 * @date 11/03/2020
 */
public class HardSimulationView extends GameView {
    /**
     * Constructor for the HardSimulationView class
     *
     * @param viewName  String variable representing the name of the view
     * @param dimension Dimension object representing the size needed for the JPanel to fit the JFrame
     */
    public HardSimulationView(String viewName, Dimension dimension) {
        super(viewName, dimension);
    }
}
