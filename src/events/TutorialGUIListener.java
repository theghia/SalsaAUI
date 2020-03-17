package events;

import java.util.EventListener;

/**
 * TutorialGUIListener interface will be implemented by classes to deal with the GUI movement for the tutorial that will
 * let the user know how to play the game and give the user additional help to find the correct timing in Salsa music
 *
 * @author Gareth Iguasnia
 * @date 13/03/2020
 */
public interface TutorialGUIListener extends EventListener {

    /**
     * This method deals with the event of the Tutorial starting
     */
    void onTutorialStarted();

    /**
     * This method deals with the event of the Tutorial finishing
     */
    void onTutorialFinished();

    void onKeepLights();

    void onKeepInstrumentsAndTempo();

    void onKeepCurrentBeat();

    void onKeepNextBeat();

    void onKeepClicker();

    void onKeepGauge();

    void onFinalTutorial();
}
