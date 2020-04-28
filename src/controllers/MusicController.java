package controllers;

import components.enums.Instrument;
import components.PlayFile;
import components.State;
import listeners.GameProgressionListener;
import events.GameEvent;
import main.SalsaController;
import main.SalsaModel;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * MusicController abstract class extends the SalsaController class and implements the interface
 * GameProgressionListener. This class shall be derived by subclasses that need to play music for the game to help the
 * user locate the correct timing in Salsa music.
 *
 * @author Gareth Iguasnia
 * @date 12/03/2020
 */
public abstract class MusicController extends SalsaController implements GameProgressionListener {

    // The field is used to access the necessary WAV files for the MVC application
    //private final String sounds = "src/assets/sounds/";
    private final String sounds = "assets/sounds/";

    // To organise the PlayFile threads so that the sounds play after another
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    // A Random Generator to select a WAV file from a specific directory
    private Random randomGenerator;

    /**
     * Constructor for the MusicController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public MusicController(SalsaModel salsaModel, String controllerName) {
        super(salsaModel, controllerName);
        randomGenerator = new Random();
    }

    /**
     * Abstract method needs to be implemented by the derived subclass so that the right "fire" method is called in
     * the method once the countdown audio file has started playing.
     */
    protected abstract void countdownStarted();

    /**
     * Abstract method needs to be implemented by the derived subclass so that the right "fire" method is called in
     * this method once the countdown audio file has finished playing.
     */
    protected abstract void countdownFinished();

    /**
     * Abstract method needs to be implemented by the derived subclass so that the right "fire" method is called in
     * this method once the Salsa audio clip has been created.
     *
     * @param clipSalsaLength Long variable representing the length of the Salsa audio clip
     */
    protected abstract void clipReady(long clipSalsaLength);

    /**
     * Method used for the HardSimulationMusicController to take action when the fireSimulationStartedEvent method is
     * called in the model. The countdown clip and the first salsa audio clip are queued to be played in the
     * simulation. This method also fires off an event for the HardSimulationController to deal with
     *
     * @param e A GameEvent object that will be used to initialise the audio of the current state
     */
    @Override
    public void onGameStartedEvent(GameEvent e) {
        // Create the clip to play the countdown WAV file and join it to the queue
        String countdownFilePath = sounds + "countdown/countdown_5-0.wav";
        PlayFile countdown = new PlayFile(countdownFilePath);

        // The length of the Countdown clip
        Long countdownLength = countdown.getMillisecondLength();

        // Adding a Line Listener to the countdown clip so that the next pieces of logic are only executed once the
        // music has finished playing
        countdown.getClip().addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                // The moment the audio file starts playing
                if (event.getType() == LineEvent.Type.START) {
                    countdownStarted();
                }
                    //countdownStarted();

                // The moment the audio file has finished playing
                else if (event.getType() == LineEvent.Type.STOP) {
                    getSalsaModel().setCountdownCurrentlyPlaying(false);
                    countdownFinished();
                }
            }
        });
        initSoundClip(countdown);
    }

    /**
     * Method used for the HardSimulationMusicController to take action when the fireNewStateEvent method is called by
     * the model in the HardSimulationController (in the thread). This method queues the next salsa audio clip, created from
     * the new State the the simulation has moved on to, to be played straight after the previous salsa audio clip.
     *
     * @param e A GameEvent object that will be used to determine what Salsa audio clip should be played
     */
    @Override
    public void onNewStateEvent(GameEvent e) {
        // Getting the new State that the simulation is moving to
        State currentState = e.getCurrentState();

        PlayFile salsaAudio = getSalsaAudio(currentState); System.out.println("Adding the linelistener");

        // Adding a LineListener so that the user input matches the sound
        salsaAudio.getClip().addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                // Fire off the event to let the relevant GameController know about the Clip information
                if (event.getType() == LineEvent.Type.START) {
                    System.out.println("The Line listener has been added");
                    clipReady(salsaAudio.getMillisecondLength());
                }
            }
        });

        // Play the salsa audio clip and join it to the queue
        initSoundClip(salsaAudio);
    }

    /* Helper method that allows the Clips to be played one after the other during the simulation */
    private synchronized void initSoundClip( PlayFile clip ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Adds the thread PlayFile to the queue to be played once the current audio thread
                // in the ExecutorService is being played and will thus play afterwards
                System.out.println("Submitting the clip to the queue");
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
        String filePath = "src/" + sounds + currentState.getBpm().getBPM();

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
        //System.out.println("Is the file path correct?");
        //System.out.println(folder);

        // Create a new cLip with the salsa audio file path found
        String salsaFilePath = "";

        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        // This was added so that the directories can be found in the Jar File
        if(jarFile.isFile()) {
            try {
                // Preparing the list of files in the directory
                ArrayList<String> possibleFiles = new ArrayList<>();
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    String prefix = folder.substring(4) + "/";
                    if ((name.startsWith(prefix)) && (name.length() > prefix.length())){
                        //System.out.println(name);
                        possibleFiles.add(name);
                    }
                }

                // Randomly selecting a WAV file from this directory
                int rndIndex = randomGenerator.nextInt(possibleFiles.size());
                //System.out.println("The random index is: " + Integer.toString(rndIndex));

                // Create a new cLip with the salsa audio file path found
                salsaFilePath += possibleFiles.get(rndIndex);

                jar.close();
            }

            catch (IOException ioe) {
                // More sophisticated logic needs to be here to restart the simulation and throw a JOptionPane
                ioe.printStackTrace();
            }
        }

        else { // This code works when being executed from an IDE

            // Preparing the list of files in the directory
            File directory = new File(folder);
            //System.out.println("Directory has been created");
            //System.out.println("The directory is: " + directory.toString());
            File[] files = directory.listFiles();
            //System.out.println("List of Files have been created");
            //System.out.println("The list of files: " + files.toString());

            // Randomly selecting a WAV file from this directory
            int rndIndex = randomGenerator.nextInt(files.length);
            //System.out.println("The random index is: " + Integer.toString(rndIndex));

            // Create a new cLip with the salsa audio file path found
            salsaFilePath += files[rndIndex].getPath();
        }

        //System.out.println("What is the Salsa file playing?");

        // remove the src from the file path by using .substring()
        if (salsaFilePath.startsWith("src/")){
            salsaFilePath = salsaFilePath.substring(4);
        }
        System.out.println(salsaFilePath);
        PlayFile salsaAudio = new PlayFile(salsaFilePath);

        return salsaAudio;
    }

    public static void main(String[] args) throws IOException {
        // The folder used in the previous run was: assets/sounds/180/timbales_clave
        //String folder = "assets/sounds/180/timbales_clave";
        String folder = "src/assets/sounds/180/timbales_clave";

        // The directory used in the previous run was: assets\sounds\180\timbales_clave
        File directory = new File(folder);
        System.out.println(directory);

        // The issue is that the list of files being produced is blank
        File[] files = directory.listFiles();

        for (File file: files) {
            System.out.println(file);
        }
        System.out.println(files.toString());

        // Using the new way to list directories
        File file = new File("src/assets/sounds/180/timbales_clave");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });
        System.out.println(Arrays.toString(directories));

        // Trying to read from the JAR file -> switch MC.class with getClass()
        final File jarFile = new File(MusicController.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(folder + "/")){
                    System.out.println(name);
                }
            }
            jar.close();
        }

        else {
            System.out.println("I have my own way already");
        }
    }
}
