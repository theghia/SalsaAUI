package views;

import components.Instrument;
import main.SalsaView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationView extends SalsaView {

    private JButton beatClicker;
    private JButton startButton;

    // JPanels that will hold 9 JLabels that were created from the Digital Number PNG files
    private JPanel currentBeat;
    private JPanel nextBeat;

    // JLabels to be used to let the user know which beat they need to find for the current 8-beat bar of music
    private JLabel currentBeatLabel;
    private JLabel nextBeatLabel;

    // An ArrayList of JLabels for the instruments GUI
    private ArrayList<JLabel> instrumentsGUI;

    // JPanel that will hold 3 JLabels representing the different tempos that the MVC application will use
    private JPanel tempos;

    public SimulationView(String name, Dimension dimension) {
        super(name, dimension, false);

        // Using CardLayout to swap out the JLabels as the beat changes every 8-beat bar in the Salsa audio clip
        this.nextBeat = new JPanel(new CardLayout());
        this.currentBeat = new JPanel(new CardLayout());
        addDigitalNumbers(currentBeat);
        addDigitalNumbers(nextBeat);

        // Creating JLabels for all of the instruments to be used in this MVC application
        this.instrumentsGUI = new ArrayList<>();

        // Using CardLayout to swap out the JLabels if the tempo changes in a State transition
        this.tempos = new JPanel(new CardLayout());

        setupJLabelsText();
        setupButtons();
        setupJLabelsInstruments();
        layoutTempo();
        layoutButtons();
        layoutDigitalNumbers();
        layoutInstruments();
    }

    /**
     * Method returns the JButton beatClicker.
     *
     * @return A JButton object that is the beat clicker
     */
    public JButton getBeatClicker() {
        return beatClicker;
    }

    /**
     * Method returns the JButton startButton.
     *
     * @return A JButton object that is the start button
     */
    public JButton getStartButton() { return startButton; }

    /**
     * Method returns the JPanel currentBeat. To be used by the SimulationGUIController so that we can swap the JLabels
     * according to the model changes for a new current and next beat.
     *
     * @return A JPanel to hold the digital number png files for the currentBeat
     */
    public JPanel getCurrentBeat() {
        return currentBeat;
    }

    /**
     * Method returns the JPanel nextBeat. To be used by the SimulationGUIController so that we can swap the JLabels
     * according to the model changes for a new current and next beat.
     *
     * @return A JPanel to hold the digital number png files for the nextBeat.
     * */
    public JPanel getNextBeat() {
        return nextBeat;
    }

    public ArrayList<JLabel> getInstrumentsGUI() {
        return instrumentsGUI;
    }

    public JPanel getTempos() {
        return tempos;
    }

    /* Helper method to set up the buttons in this view */
    private void setupButtons() {
        // Button to be used by the user to click for the beat
        this.beatClicker = new JButton("+");
        beatClicker.setPreferredSize(new Dimension(50,50));
        beatClicker.setEnabled(false); // This will be set to true by the onCountdownFinishedEvent
        beatClicker.setVisible(false); // This will be set to true by the onSimulationStartedEvent
        this.add(beatClicker); beatClicker.setBackground(null);
        //beatClicker.setBorder(new RoundedBorder());

        // Button to be used by the user to start the simulation
        this.startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(75, 75));
        startButton.setEnabled(true);
        this.add(startButton); startButton.setBackground(null);
    }

    /* Helper method to layout the buttons (except the home button) as desired on the SimulationView*/
    private void layoutButtons() {
        // The Start button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, startButton, 400, SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, startButton, 600, SpringLayout.NORTH, this);

        // The Beat Clicker button
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, beatClicker, 500, SpringLayout.NORTH, this);
    }

    /* Helper method to layout the JLabels for the digital numbers on the SimulationView */
    private void layoutDigitalNumbers() {
        // The JLabel that will hold the text "Next Beat:"
        this.getPanelLayout().putConstraint(SpringLayout.EAST, nextBeatLabel, -100 - 20, SpringLayout.EAST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, nextBeatLabel, 25, SpringLayout.NORTH, this);

        // The JLabel that will hold the text "Current Beat:"
        this.getPanelLayout().putConstraint(SpringLayout.WEST, currentBeatLabel,100,SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, currentBeatLabel, 25, SpringLayout.NORTH, this);

        // The JPanel holding the digital number png files to let the user know what the next beat is
        this.getPanelLayout().putConstraint(SpringLayout.EAST, nextBeat, -150, SpringLayout.EAST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, nextBeat, 50 + 25, SpringLayout.NORTH, this);

        // The JPanel holding the digital number png files to let the user know what the current beat is
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, currentBeat, 50 + 25, SpringLayout.NORTH, this);
        this.getPanelLayout().putConstraint(SpringLayout.WEST, currentBeat,150, SpringLayout.WEST,this);
    }

    /* Helper method to layout the JLabels for the instrument GUI */
    private void layoutInstruments() {
        // Constants that we can use to toggle the position of the instrument GUI
        int bufferDigitalToBottom = 130;
        int bufferDigitalToTop = 50;
        int bufferFrameToBottomSide = 50;
        int bufferTopToBottomSide = 175;

        // The JLabel that holds the piano png file
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, instrumentsGUI.get(0), bufferDigitalToBottom,
                SpringLayout.SOUTH, currentBeat);
        this.getPanelLayout().putConstraint(SpringLayout.WEST, instrumentsGUI.get(0), bufferFrameToBottomSide,
                SpringLayout.WEST, this);

        // The JLabel that holds the clave png file
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, instrumentsGUI.get(3), bufferDigitalToBottom,
                SpringLayout.SOUTH, currentBeat);
        this.getPanelLayout().putConstraint(SpringLayout.EAST, instrumentsGUI.get(3), -bufferFrameToBottomSide,
                SpringLayout.EAST, this);

        // The JLabel that holds the timbales png file
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, instrumentsGUI.get(1), bufferDigitalToTop,
                SpringLayout.SOUTH, currentBeat);
        this.getPanelLayout().putConstraint(SpringLayout.WEST, instrumentsGUI.get(1), bufferTopToBottomSide,
                SpringLayout.WEST, instrumentsGUI.get(0));

        // The JLabel that holds the bass png file
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, instrumentsGUI.get(2), bufferDigitalToTop,
                SpringLayout.SOUTH, currentBeat);
        this.getPanelLayout().putConstraint(SpringLayout.EAST, instrumentsGUI.get(2), -bufferTopToBottomSide,
                SpringLayout.EAST, instrumentsGUI.get(3));
    }

    /* Helper method to add JLabels (PNG files) to the JPanel using CardLayout to display the beats */
    private void addDigitalNumbers(JPanel displayBeats) {
        // Nine digital png files to be placed onto a JLabel
        for (int i = 0; i < 9; i++) {
            if (i == 0) {
                // The default png file before the Simulation starts
                ImageIcon digitalNum = new ImageIcon(getGRAPHICS() + "no_beat.png");
                JLabel digNum = new JLabel(digitalNum);

                // Adding the JLabel to the JPanel using the CardLayout()
                displayBeats.add(digNum, "no_beat");
            }
            else {
                // The digital numbers that will display the current and next beats during the simulation
                ImageIcon digitalNum = new ImageIcon(getGRAPHICS() + i + ".png");
                JLabel digNum = new JLabel(digitalNum);

                // Adding the JLabel to the JPanel using the CardLayout()
                displayBeats.add(digNum, Integer.toString(i));
            }
        }
        // Adding the group of JLabels to the SimulationView
        this.add(displayBeats);
    }

    /* Helper method that sets up the JLabels to let the user know which Digital Number is the current and next beat
    and adds them to the SimulationView */
    private void setupJLabelsText() {
        // Getting the desired font and size for the beat labels
        Font f = new Font("TimesRoman",Font.BOLD,25);

        // Current Beat Label
        this.currentBeatLabel = new JLabel("Current Beat:");
        this.currentBeatLabel.setFont(f);
        this.add(currentBeatLabel);

        // Next Beat Label
        this.nextBeatLabel = new JLabel("Next Beat:");
        this.nextBeatLabel.setFont(f);
        this.add(nextBeatLabel);

        // Tempo Label
        Font f1 = new Font("Bahnschrift",Font.BOLD,45);
        String[] speeds = {"Slow", "Medium","Fast"};
        for (String speed: speeds) {
            JLabel tempo = new JLabel(speed);
            tempo.setFont(f1);
            this.tempos.add(tempo, speed);
        }
        this.tempos.setVisible(false);
        this.add(tempos);

    }

    private void layoutTempo() {
      // Centering the tempo label
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, tempos, 50,
                SpringLayout.SOUTH, instrumentsGUI.get(1));
        this.getPanelLayout().putConstraint(SpringLayout.WEST, tempos, 150,
                SpringLayout.EAST, instrumentsGUI.get(0));
    }

    /* Helper method sets up the JLabels for the instrument GUI */
    private void setupJLabelsInstruments() {
        // Adding the instrument png files to the SimulationView
        Instrument aInstrument = Instrument.PIANO;
        Instrument[] possibleValues = aInstrument.getDeclaringClass().getEnumConstants();
        for (Instrument instrument: possibleValues) {
            // Scaling the image as it is very big in size
            ImageIcon instrumentGUI = new ImageIcon(getGRAPHICS() + instrument.getName() + ".png");
            Image scaled = instrumentGUI.getImage().getScaledInstance(125,125, Image.SCALE_SMOOTH);
            ImageIcon scaledInsGUI = new ImageIcon(scaled);
            JLabel insGUI = new JLabel(scaledInsGUI);

            // Adding the JLabel to the SimulationView
            this.add(insGUI);

            // We do not want the instruments to be visible before pressing the Start button
            insGUI.setVisible(false);

            // Adding the JLabel to the ArrayList of JLabels for the instrument GUI
            this.instrumentsGUI.add(insGUI);
        }
    }

}
