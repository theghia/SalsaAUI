package views;

import components.Instrument;
import components.RotateImage;
import main.SalsaView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * GameView abstract class that extends the SalsaView abstract class. The classes that will derive this class will be
 * views that will be used to test the user in finding the correct timing in the produced Salsa music.
 *
 * @author Gareth Iguasnia
 * @date 11/03/2020
 */
public abstract class GameView extends SalsaView {

    // Buttons that will be used exclusively for this view
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

    // Gauge
    private JLabel gaugeGUI;
    private RotateImage rotateNeedle;

    // 8 light buttons to represent when the beat is occurring
    private ArrayList<JPanel> lights;

    // 6 numbers to display the countdown from 5 to 0
    private JPanel numberCountdown;

    // Dimensions for the "Start" button
    private final int START_HEIGHT = 120;
    private final int START_WIDTH = 120;

    // Dimensions for the "Clicker" button
    private final int CLICKER_HEIGHT = 120;
    private final int CLICKER_WIDTH = 120;

    // Dimensions for the "Countdown" numbers
    private final int COUNTDOWN_HEIGHT = 240;
    private final int COUNTDOWN_WIDTH = 240;

    /**
     * Constructor for the abstract class GameView
     * This will only be used when the subclass calls the super() method.
     *
     * @param viewName   String variable representing the name of the view
     * @param dimension  Dimension object representing the size needed for the JPanel to fit the JFrame
     */
    public GameView(String viewName, Dimension dimension) {
        super(viewName, dimension, false);

        // Using CardLayout to swap out the JLabels as the beat changes every 8-beat bar in the Salsa audio clip
        this.nextBeat = new JPanel(new CardLayout());
        this.currentBeat = new JPanel(new CardLayout());
        addDigitalNumbers(currentBeat);
        addDigitalNumbers(nextBeat);

        // Creating JLabels for all of the instruments to be used in this MVC application
        this.instrumentsGUI = new ArrayList<>();

        // Using CardLayout to swap out the JLabels if the tempo changes in a State transition
        this.tempos = new JPanel(new CardLayout());

        // Instantiating the JPanels for the 8 lights
        this.lights = new ArrayList<>(8);

        // Using CardLayout to swap out the numbers to be in sync with the countdown audio file
        this.numberCountdown = new JPanel(new CardLayout());

        // Setting up the JButtons, JLabels and JPanels to be added to this view
        setupJLabelsText();
        setupButtons();
        setupJLabelsInstruments();
        setupGauge();
        setupLights();
        setupCountdown();

        // Laying out the JButtons, JLabels and JPanels in the desired format
        layoutTempo();
        layoutDigitalNumbers();
        layoutButtons();
        layoutInstruments();
        layoutGauge();
        layoutLights();
        layoutCountdown();
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

    /**
     * Method returns the ArrayList of JLabels that hold all of the instruments that will be used in this MVC
     * application.
     *
     * @return An ArrayList object of JLabels that contain instrument PNG files
     */
    public ArrayList<JLabel> getInstrumentsGUI() {
        return instrumentsGUI;
    }

    /**
     * Method returns currentBeatLabel
     *
     * @return A JLabel containing the words "Current Beat"
     */
    public JLabel getCurrentBeatLabel() {
        return currentBeatLabel;
    }

    /**
     * Method returns nextBeatLabel
     *
     * @return A JLabel containing the words "Next Beat"
     */
    public JLabel getNextBeatLabel() {
        return nextBeatLabel;
    }

    /**
     * Method returns the needle of the gauge
     *
     * @return A RotateImage object that contains a needle png
     */
    public RotateImage getRotateNeedle() {
        return rotateNeedle;
    }

    /**
     * Method loops through all of the JPanels that hold the light PNG files and will set them all visible
     */
    public void displayLights() {
        for (JPanel light: this.lights) {
            light.setVisible(true);
        }
    }

    /**
     * Method returns the JPanel using a CardLayout that holds JLabels that will be used to represent the current tempo
     * that the music will be playing in the MVC application.
     *
     * @return A JPanel using a CardLayout with JLabels representing the tempos that the MVC application will be using
     */
    public JPanel getTempos() {
        return tempos;
    }

    /**
     * Method returns the Gauge icon
     *
     * @return JLabel that contains the Gauge png file
     */
    public JLabel getGaugeGUI() {
        return gaugeGUI;
    }

    /**
     * Method returns the rotateNeedle field that will be used to rotate the GUI needle of the gauge to represent the
     * error value of the user's input
     *
     * @return A RotateImage object that will be used to rotate the GUI needle of the gauge
     */
    public RotateImage getRotateImage() {
        return rotateNeedle;
    }

    /**
     * Method returns the JPanel using CardLayout that holds all of the numbers to be used for the countdown
     *
     * @return A JPanel holding JLabels with all of the necessary number png files
     */
    public JPanel getNumberCountdown() {
        return numberCountdown;
    }

    /**
     * Method returns the ArrayList of JPanels that represents the lights to display the timing
     *
     * @return An ArrayList of JPanels where each JPanel will hold the light png files (on and off)
     */
    public ArrayList<JPanel> getLights() {
        return lights;
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

    /* Helper method to set up the buttons in this view */
    private void setupButtons() {
        // Scaling the "Clicker" png file
        ImageIcon clicker = new ImageIcon(getGRAPHICS() + "finger_icon.png");
        Image scaled1 = clicker.getImage().getScaledInstance(CLICKER_WIDTH - 40,
                CLICKER_HEIGHT - 40, Image.SCALE_SMOOTH);
        ImageIcon scaledClicker = new ImageIcon(scaled1);

        // Button to be used by the user to click for the beat
        this.beatClicker = new JButton(scaledClicker);
        beatClicker.setPreferredSize(new Dimension(CLICKER_WIDTH,CLICKER_HEIGHT));

        beatClicker.setEnabled(false); // This will be set to true by the onCountdownFinishedEvent
        beatClicker.setVisible(false); // This will be set to true by the onSimulationStartedEvent
        this.add(beatClicker); beatClicker.setBackground(null);

        // Scaling the "Start" png file
        ImageIcon start = new ImageIcon(getGRAPHICS() + "start.png");
        Image scaled2 = start.getImage().getScaledInstance(START_WIDTH - 20,
                START_HEIGHT - 20, Image.SCALE_SMOOTH);
        ImageIcon scaledStart = new ImageIcon(scaled2);

        // Button to be used by the user to start the simulation
        this.startButton = new JButton(scaledStart);
        startButton.setPreferredSize(new Dimension(START_WIDTH, START_HEIGHT));
        startButton.setEnabled(true);
        this.add(startButton); startButton.setBackground(null);
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

    /* Helper method that sets up the gauge and the needle of the gauge */
    private void setupGauge() {
        // Adding the needle of the Gauge to this view
        this.rotateNeedle = new RotateImage(getGRAPHICS() + "pointer_turning.png");
        this.add(rotateNeedle);

        // Adding the Gauge image to this view
        ImageIcon imgGauge = new ImageIcon(getGRAPHICS() + "gauge.png");
        Image scaled = imgGauge.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledImgGUI = new ImageIcon(scaled);
        this.gaugeGUI = new JLabel(scaledImgGUI);
        this.gaugeGUI.setOpaque(false);
        this.add(gaugeGUI);
    }

    /* Helper method that sets up the lights that will visually show the user the timing of the music */
    private void setupLights() {
        // There will be 8 lights
        for (int i = 0; i < 8; i++) {
            JPanel light = new JPanel(new CardLayout());

            // Adding the off light to a JLabel
            ImageIcon light1 = new ImageIcon(getGRAPHICS() + "off_light.png");
            JLabel off_light = new JLabel(light1);

            // Adding the on light to a JLabel
            ImageIcon light2 = new ImageIcon(getGRAPHICS() + "on_light.png");
            JLabel on_light = new JLabel(light2);

            // Adding the JLabels to the JPanel using the CardLayout()
            light.add(off_light, "off_light");
            light.add(on_light, "on_light");

            // Adding the JPanel to the ArrayList of JPanels for the lights
            this.lights.add(light);

            // The lights will only be visible in the Tutorial View
            light.setVisible(false);

            // Adding the JPanel to the SimulationView
            this.add(light);
        }
    }

    /* Helper method to setup the countdownNumber JPanel */
    private void setupCountdown() {
        // Looping 6 times for each of the numbered png files
        for (int i = 5; i > -1; i--) {
            ImageIcon number = new ImageIcon(getGRAPHICS() + "num_" + i + ".png");
            Image scaled = number.getImage().getScaledInstance(COUNTDOWN_WIDTH, COUNTDOWN_HEIGHT, Image.SCALE_SMOOTH);
            ImageIcon scaledNumber = new ImageIcon(scaled);
            JLabel countdownNum = new JLabel(scaledNumber);
            this.numberCountdown.add(countdownNum, Integer.toString(i));
        }
        // Making the numbered png files invisible
        this.numberCountdown.setVisible(false);

        // Adding the JPanel containing all of the numbered png files to the GameView
        this.add(numberCountdown);
    }

    /* Helper method lays out the tempo JLabels on the GameView */
    private void layoutTempo() {
        // Centering the tempo label
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, tempos, 50,
                SpringLayout.SOUTH, instrumentsGUI.get(1));
        this.getPanelLayout().putConstraint(SpringLayout.WEST, tempos, 150,
                SpringLayout.EAST, instrumentsGUI.get(0));
    }

    /* Helper method to layout the JLabels for the digital numbers on the SimulationView */
    private void layoutDigitalNumbers() {
        // Measurements:
        int bufferLightsANDBeatLabel = 10;
        int bufferBeatLabelANDDigitalBeat = 10;
        int horizontalBufferFrameANDDigitalBeat = 150;

        // The JLabel that will hold the text "Next Beat:"
        this.getPanelLayout().putConstraint(SpringLayout.EAST, nextBeatLabel, -100 - 20,
                SpringLayout.EAST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, nextBeatLabel, bufferLightsANDBeatLabel,
                SpringLayout.SOUTH, lights.get(0)); // Lights to this - 25

        // The JLabel that will hold the text "Current Beat:"
        this.getPanelLayout().putConstraint(SpringLayout.WEST, currentBeatLabel,100,
                SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, currentBeatLabel, bufferLightsANDBeatLabel,
                SpringLayout.SOUTH, lights.get(0));

        // The JPanel holding the digital number png files to let the user know what the next beat is
        this.getPanelLayout().putConstraint(SpringLayout.EAST, nextBeat, -horizontalBufferFrameANDDigitalBeat,
                SpringLayout.EAST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, nextBeat, bufferBeatLabelANDDigitalBeat,
                SpringLayout.SOUTH, nextBeatLabel);

        // The JPanel holding the digital number png files to let the user know what the current beat is
        this.getPanelLayout().putConstraint(SpringLayout.WEST, currentBeat,horizontalBufferFrameANDDigitalBeat,
                SpringLayout.WEST,this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, currentBeat, bufferBeatLabelANDDigitalBeat,
                SpringLayout.SOUTH, currentBeatLabel);
    }

    /* Helper method to layout the buttons (except the home button) as desired on the SimulationView*/
    private void layoutButtons() {
        // The Start button - Dimensions to center the button
        int widthPositionStart = (int) (getDimension().getWidth() - START_WIDTH)/2;
        int heightPositionStart = (int) (getDimension().getHeight() - START_HEIGHT)/2;

        // The Start button - Centering the button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, startButton, widthPositionStart,
                SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, startButton, heightPositionStart,
                SpringLayout.NORTH, this);

        // The Beat Clicker button - Positioning
        this.getPanelLayout().putConstraint(SpringLayout.SOUTH, beatClicker,
                - (this.getHeightBuffer() + 85), SpringLayout.SOUTH, this);
        this.getPanelLayout().putConstraint(SpringLayout.EAST, beatClicker,
                - (this.getWidthBuffer() + 160), SpringLayout.EAST, this);
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

    /* Helper method to layout the gauge and the needle of the gauge */
    private void layoutGauge() {
        // Centering the needle image on the center of the Gauge image
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, rotateNeedle, 500,
                SpringLayout.WEST, instrumentsGUI.get(0));
        this.getPanelLayout().putConstraint(SpringLayout.WEST, rotateNeedle, 50,
                SpringLayout.WEST, this);

        // Moving the Gauge to the bottom left corner
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, gaugeGUI, 500,
                SpringLayout.WEST, instrumentsGUI.get(0));
        this.getPanelLayout().putConstraint(SpringLayout.WEST, gaugeGUI, 50,
                SpringLayout.WEST, this);
    }

    /* Helper method to layout the lights to represent the timing of the music */
    private void layoutLights() {
        // 35 is the length of light - 15
        this.getPanelLayout().putConstraint(SpringLayout.EAST, lights.get(7), -180, SpringLayout.EAST, this);

        // All lights should be 5 units apart from each other
        for (int i = 6; i > -1; i--) {
            this.getPanelLayout().putConstraint(SpringLayout.EAST, lights.get(i), -5,
                    SpringLayout.WEST, lights.get(i+1));
        }

        // Positioning the lights vertically - 550
        for (int i = 0; i < 8; i++) {
            this.getPanelLayout().putConstraint(SpringLayout.NORTH, lights.get(i), 25,
                    SpringLayout.NORTH, this);
        }
    }

    /* Helper method to layout the number png files that will display the numbers as the countdown file plays */
    private void layoutCountdown() {
        // The numberCountdown JPanel - Dimensions to center the JPanel
        int widthPositionStart = (int) (getDimension().getWidth() - COUNTDOWN_WIDTH)/2;
        int heightPositionStart = (int) (getDimension().getHeight() - COUNTDOWN_HEIGHT)/2;

        // The numberCountdown JPanel - Centering the JPanel
        this.getPanelLayout().putConstraint(SpringLayout.WEST, numberCountdown, widthPositionStart,
                SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, numberCountdown, heightPositionStart,
                SpringLayout.NORTH, this);
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
}
