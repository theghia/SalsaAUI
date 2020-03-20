package views.games;

import views.GameView;

import java.awt.*;

public class EasySimulationView extends GameView {
    /**
     * Constructor for the EasySimulationView. This is the view for the easy version of the game
     * This will only be used when the subclass calls the super() method.
     *
     * @param viewName  String variable representing the name of the view
     * @param dimension Dimension object representing the size needed for the JPanel to fit the JFrame
     */
    public EasySimulationView(String viewName, Dimension dimension) {
        super(viewName, dimension);
        this.displayLights();
    }
}
