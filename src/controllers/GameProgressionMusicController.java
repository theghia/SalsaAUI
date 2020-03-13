package controllers;

import components.Instrument;
import components.PlayFile;
import components.State;
import events.GameProgressionListener;
import events.GameEvent;
import main.SalsaController;
import main.SalsaModel;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * GameProgressionMusicController Class extends the SalsaController Class and implements the GameProgressionListener Class. This
 * class deals with the logic to play the Salsa audio files one after the other during the simulation according to how
 * the simulation moves around the State objects.
 *
 * @author Gareth Iguasnia
 * @date 26/02/2020
 */
public class GameProgressionMusicController extends SalsaController implements GameProgressionListener {
    // The field is used to access the necessary WAV files for the MVC application
    private final String sounds = "src/assets/sounds/";

    // To organise the PlayFile threads so that the sounds play after another
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    // A Random Generator to select a WAV file from a specific directory
    private Random randomGenerator;

    /**
     * Constructor for the SalsaController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public GameProgressionMusicController(SalsaModel salsaModel, String controllerName) {
        super(salsaModel, controllerName);
        randomGenerator = new Random();
    }

    /**
     * Method used for the GameProgressionMusicController to take action when the fireSimulationStartedEvent method is
     * called in the model. The countdown clip and the first salsa audio clip are queued to be played in the
     * simulation. This method also fires off an event for the SimulationController to deal with
     *
     * @param e A GameEvent object that will be used to initialise the audio of the current state
     */
    @Override
    public void onGameStartedEvent(GameEvent e) {
        // Create the clip to play the countdown WAV file and join it to the queue
        String countdownFilePath = sounds + "countdown/countdown_5-0.wav";
        PlayFile countdown = new PlayFile(countdownFilePath);

        // Adding a Line Listener to the countdown clip so that the next pieces of logic are only executed once the
        // music has finished playing
        countdown.getClip().addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                // The moment the audio file starts playing
                if (event.getType() == LineEvent.Type.START)
                    getSalsaModel().fireCountdownStartedEvent(countdown.getMillisecondLength());

                // The moment the audio file has finished playing
                else if (event.getType() == LineEvent.Type.STOP) {
                    getSalsaModel().setCountdownCurrentlyPlaying(false);
                    getSalsaModel().fireCountdownFinishedEvent();
                    getSalsaModel().fireNewStateEvent();
                }
            }
        });

        initSoundClip(countdown);
    }

    /**
     * Method used for the GameProgressionMusicController to take action when the fireNewStateEvent method is called by
     * the model in the SimulationController (in the thread). This method queues the next salsa audio clip, created from
     * the new State the the simulation has moved on to, to be played straight after the previous salsa audio clip.
     *
     * @param e A GameEvent object that will be used to determine what Salsa audio clip should be played
     */
    @Override
    public void onNewStateEvent(GameEvent e) {
        // Getting the new State that the simulation is moving to
        State currentState = e.getCurrentState();

        PlayFile salsaAudio = getSalsaAudio(currentState);

        // Play the salsa audio clip and join it to the queue
        initSoundClip(salsaAudio);

        // Fire off the event to let SimulationController know about the Clip information
        getSalsaModel().fireClipInfoReadyEvent(salsaAudio.getMillisecondLength());
    }

    /* Helper method that allows the Clips to be played one after the other during the simulation */
    private synchronized void initSoundClip( PlayFile clip ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Adds the thread PlayFile to the queue to be played once the current audio thread
                // in the ExecutorService is being played and will thus play afterwards
                Future<?> f2 = executor.submit(clip);
                try {
                    f2.get();
                }
                catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /* Helper method that creates the final part of the file path needed to get the correct folder with
    * the necessary WAV files */
    private String getFilePath(State currentState) {
        String filePath = sounds + currentState.getBpm().getBPM();

        String instruments = "";

        ArrayList<Instrument> instrumentList = currentState.getInstruments();

        for (int i = 0; i < instrumentList.size() - 1; i++)
            instruments+= instrumentList.get(i).getName() + "_";
        instruments += instrumentList.get(instrumentList.size() - 1).getName();

        return filePath + "/" + instruments;
    }

    /* Helper method that creates a PlayFile object depending on the State object passed as a parameter */
    private PlayFile getSalsaAudio(State currentState) {
        // Creating the file path of the folder that contains WAV files that pertain to the current State object
        String folder = getFilePath(currentState);

        // Randomly selecting a WAV file from this directory
        File directory = new File(folder);
        File[] files = directory.listFiles();
        int rndIndex = randomGenerator.nextInt(files.length);

        // Create a new cLip with the salsa audio file path found
        String salsaFilePath = files[rndIndex].getPath();
        PlayFile salsaAudio = new PlayFile(salsaFilePath);

        return salsaAudio;
    }
}
