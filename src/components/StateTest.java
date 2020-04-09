package components;

import components.enums.BPM;
import components.enums.Instrument;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void getNeighbours() {
        // Set up the neighbours
        UserProfile up = new UserProfile();

        // The State we will get is SLOW, TIMBALES and CLAVE
        Map<String, State> states = up.getStates();
        State state = states.get("13a");
        ArrayList<State> actualNeighbours = state.getNeighbours();

        // Preparing the neighbours that should be related to the above State
        ArrayList<State> neighbours = new ArrayList<>(9);

        // Adding PIANO, TIMBALES, CLAVE and SLOW
        ArrayList<Instrument> instruments1 = new ArrayList<>(3);
        instruments1.add(Instrument.PIANO); instruments1.add(Instrument.TIMBALES); instruments1.add(Instrument.CLAVE);
        State state1 = new State(BPM.SLOW, instruments1);
        neighbours.add(state1);

        // Adding TIMBALES, BASS, CLAVE and SLOW
        ArrayList<Instrument> instruments2 = new ArrayList<>(3);
        instruments2.add(Instrument.TIMBALES); instruments2.add(Instrument.BASS); instruments2.add(Instrument.CLAVE);
        State state2 = new State(BPM.SLOW, instruments2);
        neighbours.add(state2);

        // Adding TIMBALES and SLOW
        ArrayList<Instrument> instruments3 = new ArrayList<>(1);
        instruments3.add(Instrument.TIMBALES);
        State state3 = new State(BPM.SLOW, instruments3);
        neighbours.add(state3);

        // Adding CLAVE and SLOW
        ArrayList<Instrument> instruments4 = new ArrayList<>(1);
        instruments4.add(Instrument.CLAVE);
        State state4 = new State(BPM.SLOW, instruments4);
        neighbours.add(state4);

        // Adding TIMBALES, CLAVE and MEDIUM
        ArrayList<Instrument> instruments5 = new ArrayList<>(2);
        instruments5.add(Instrument.TIMBALES); instruments5.add(Instrument.CLAVE);
        State state5 = new State(BPM.MEDIUM, instruments5);
        neighbours.add(state5);

        // Adding PIANO, TIMBALES and SLOW
        ArrayList<Instrument> instruments6 = new ArrayList<>(2);
        instruments6.add(Instrument.PIANO); instruments6.add(Instrument.TIMBALES);
        State state6 = new State(BPM.SLOW, instruments6);
        neighbours.add(state6);

        // Adding PIANO, CLAVE and SLOW
        ArrayList<Instrument> instruments7 = new ArrayList<>(2);
        instruments7.add(Instrument.PIANO); instruments7.add(Instrument.CLAVE);
        State state7 = new State(BPM.SLOW, instruments7);
        neighbours.add(state7);

        // Adding TIMBALES, BASS and SLOW
        ArrayList<Instrument> instruments8 = new ArrayList<>(2);
        instruments8.add(Instrument.TIMBALES); instruments8.add(Instrument.BASS);
        State state8 = new State(BPM.SLOW, instruments8);
        neighbours.add(state8);

        // Adding BASS, CLAVE and SLOW
        ArrayList<Instrument> instruments9 = new ArrayList<>(2);
        instruments9.add(Instrument.BASS); instruments9.add(Instrument.CLAVE);
        State state9 = new State(BPM.SLOW, instruments9);
        neighbours.add(state9);

        int counter = 0;

        for (State checkState: neighbours) {

            for (State actualState: actualNeighbours) {
                if (checkState.equals(actualState)) {
                    counter++;
                }
            }
        }

        for (State actualState: actualNeighbours) {

            for (State checkState: neighbours) {
                if (actualState.equals(checkState))
                    counter++;
            }
        }

        assertEquals(18, counter);

    }
}