package components;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.io.File;

/**
 * PlayFile class that extends the Thread class. This has been done to allow multiple sounds be played at the same
 * time to create Polyphonic music in this MVC application
 *
 * @author Gareth Iguasnia
 * @date 26/12/2019
 */
public class PlayFile extends Thread {
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
     * Method to get the length of the WAV file being played
     *
     * @return A long object representing the length of the WAV file in microseconds
     */
    public long getMicrosecondLength() {
        return this.clip.getMicrosecondLength();
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

    public static void main(String[] args) throws InterruptedException {

        String piano1 = "C:\\Users\\User\\Desktop\\music\\180\\piano\\piano1.wav";
        String clave_piano = "C:\\Users\\User\\Desktop\\music\\200\\clave_piano\\son-clave_piano4.wav";

        PlayFile file1 = new PlayFile(piano1);
        //PlayFile file2 = new PlayFile(clave_piano);

        file1.start();
        file1.join();
        // Then here is where we can write some code so that after a certain amount of time,
        // we calculate the next state

        PlayFile file2 = new PlayFile(clave_piano);
        file2.start();
        file2.join();
    }


}
