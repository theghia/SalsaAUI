package components;

import javax.sound.sampled.*;
import java.io.File;

/**
 * PlayFile class that extends the Thread class. This has been done to allow multiple sounds be played at the same
 * time to create Polyphonic music in this MVC application
 *
 * @author Gareth Iguasnia
 * @date 26/12/2019
 */
public class PlayFile implements Runnable {
    // The name of the file to play
    private String filename;

    // Holding the Clip of the WAV file
    private Clip clip;

    /**
     * Constructor for the PlayFile Class.
     *
     * @param filename String object that represents the file path of the WAV file to be played
     */
    public PlayFile(String filename) {
        this.filename = filename;

        // This converts the WAV into a clip
        initiateClip();
    }

    /**
     * Method to initiate the thread object. The PlayFile object will play the WAV file
     * associated to the clip
     */
    public void run() {
        try {
            this.clip.start();

            // To keep the sound playing for the exact amount of time needed
            while(this.clip.getMicrosecondLength() != this.clip.getMicrosecondPosition());

            // Cleaning up
            this.clip.drain();
            this.clip.stop();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to get the Clip value that this PlayFile object contains
     *
     * @return The Clip object that pertains to the PlayFile object
     */
    public Clip getClip() {
        return clip;
    }

    /**
     * Method to get the length of the WAV file being played in Milliseconds
     *
     * @return A long object representing the length of the WAV file in milliseconds
     */
    public long getMillisecondLength() {
        // 1000 Microseconds = 1 Milliseconds
        return this.clip.getMicrosecondLength()/1000;
    }

    /* Method to convert the WAV file into a clip */
    private void initiateClip() {
        try {
            File file = new File(this.filename);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
            this.clip = clip;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PlayFile testing = new PlayFile("src/assets/sounds/countdown/countdown_5-0.wav");
        PlayFile test = new PlayFile("src/assets/sounds/countdown/countdown_5-0.wav");

        System.out.println(testing.getClip().equals(test.getClip()));
        testing.getClip().addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                if(event.getType() == LineEvent.Type.STOP)
                    System.out.println("The clip has stopped playing");
                else if (event.getType() == LineEvent.Type.START)
                    System.out.println("The clip has started playing");
            }
        });

        testing.run();
    }
}
