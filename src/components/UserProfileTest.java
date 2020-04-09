package components;

import components.enums.BPM;
import components.enums.Instrument;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @org.junit.jupiter.api.Test
    void createID() {
        // Slow will have an ID of 'a'
        BPM bpm = BPM.SLOW;

        // The ArrayList should be ordered from the Instruments ID from small to big
        ArrayList<Instrument> instruments = new ArrayList<>(2);
        instruments.add(Instrument.TIMBALES); // ID number 1
        instruments.add(Instrument.CLAVE); // ID number 3

        // Create a UP and create an ID with a particular state and Instrument
        UserProfile up = new UserProfile();
        String ID = up.createID(bpm, instruments);

        assertEquals("13a", ID);
    }
}