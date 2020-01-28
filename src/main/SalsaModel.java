package main;

import java.util.Map;

public class SalsaModel {

    // Name of the user of the application
    private String nameOfUser;
    // Going to be used as a marker to know what panel we are at
    private int currentPanel = 0;

    // Might be overkill to have this...
    // Key - The int value of the currentPanel
    // Value - The Panel that the int corresponds too
    // but then this way you can actually not need to have the
    // I THINK THIS SHOULD BE IN THE MAIN FRAME!
    //private Map<Integer, String> panelList;
}
