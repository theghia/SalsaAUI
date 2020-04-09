package controllers.simulation.hard;

import components.State;
import components.UserProfile;
import main.SalsaModel;
import org.junit.jupiter.api.Test;
import views.games.HardSimulationView;

import java.awt.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HardSimulationControllerTest {

    @Test
    void chooseStartingState() {
        // The state must be one from the User Profile
        UserProfile up = new UserProfile();

        SalsaModel model = new SalsaModel();
        HardSimulationView view = new HardSimulationView("test", new Dimension(10,10));
        HardSimulationController controller = new HardSimulationController(model, "test", view);

        State random = controller.chooseStartingState();
        Map<String, State> states = up.getStates();

        boolean isStateFound = false;

        for (State state: states.values()) {
            if (state.equals(random))
                isStateFound = true;
        }

        assertTrue(isStateFound);
    }
}