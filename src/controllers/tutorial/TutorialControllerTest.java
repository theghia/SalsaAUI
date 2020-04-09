package controllers.tutorial;

import components.State;
import components.UserProfile;
import components.enums.BPM;
import controllers.simulation.hard.HardSimulationController;
import main.SalsaModel;
import org.junit.jupiter.api.Test;
import views.TutorialView;
import views.games.HardSimulationView;

import java.awt.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TutorialControllerTest {

    @Test
    void chooseStartingState() {

        SalsaModel model = new SalsaModel();
        TutorialView view = new TutorialView("test", new Dimension(10,10));
        TutorialController controller = new TutorialController(model, "test", view);

        State random = controller.chooseStartingState();

        assertTrue((random.getInstruments().size() <= 2) && (random.getBpm().equals(BPM.SLOW)));
    }
}