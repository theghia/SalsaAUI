package views;

import java.awt.*;

/**
 * TutorialView class extends the GameView class. This view will display the lights as it will be used to help train
 * the user's ability in recognising the timing in Salsa music.
 *
 * @author Gareth Iguasnia
 * @date 11/03/2020
 */
public class TutorialView extends GameView {

    /**
     * Constructor for the TutorialView class
     *
     * @param viewName String variable representing the name of the view
     * @param dimension Dimension object representing the size needed for the JPanel to fit the JFrame
     */
    public TutorialView(String viewName, Dimension dimension) {
        super(viewName, dimension);
        this.displayLights();
    }

}
