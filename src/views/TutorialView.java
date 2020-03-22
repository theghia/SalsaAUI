package views;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 * TutorialView class extends the GameView class. This view will display the lights as it will be used to help train
 * the user's ability in recognising the timing in Salsa music.
 *
 * @author Gareth Iguasnia
 * @date 11/03/2020
 */
public class TutorialView extends GameView {

    // JButtons to be used to go through the tutorial
    private JButton next;
    private JButton back;
    private JButton skip;
    private JButton practice;
    private JButton no;

    // The dimensions of the buttons on the tutorial view
    private final int WIDTH_BUTTONS = 120;
    private final int HEIGHT_BUTTONS = 60;

    // The JTextFields that will be used to describe how to use the system
    ArrayList<JTextArea> explanations;

    /**
     * Constructor for the TutorialView class
     *
     * @param viewName String variable representing the name of the view
     * @param dimension Dimension object representing the size needed for the JPanel to fit the JFrame
     */
    public TutorialView(String viewName, Dimension dimension) {
        super(viewName, dimension);
        this.displayLights();

        // Setting up the ArrayList of JTextFields
        explanations = new ArrayList<>(6);

        setupButtons();
        setupText();

        layoutButtons();
        layoutText();
    }

    public JButton getNext() {
        return next;
    }

    public JButton getBack() {
        return back;
    }

    public JButton getSkip() {
        return skip;
    }

    public JButton getPractice() {
        return practice;
    }

    public JButton getNo() {
        return no;
    }

    public ArrayList<JTextArea> getExplanations() {
        return explanations;
    }

    private void setupButtons() {
        // Setting up the Skip button
        skip = new JButton("Skip Tutorial");
        skip.setVisible(false);
        skip.setPreferredSize(new Dimension( WIDTH_BUTTONS, HEIGHT_BUTTONS));
        this.add(skip);

        // Setting up the Next button
        next = new JButton("Next");
        next.setVisible(false);
        next.setPreferredSize(new Dimension(WIDTH_BUTTONS, HEIGHT_BUTTONS));
        this.add(next);

        // Setting up the Back button
        back = new JButton("Back");
        back.setVisible(false);
        back.setPreferredSize(new Dimension(WIDTH_BUTTONS, HEIGHT_BUTTONS));
        this.add(back);

        // Setting up the Practice button
        practice = new JButton("Practice");
        practice.setVisible(false);
        practice.setPreferredSize(new Dimension(WIDTH_BUTTONS, HEIGHT_BUTTONS));
        this.add(practice);

        // Setting up the No button
        no = new JButton("No");
        no.setVisible(false);
        no.setPreferredSize(new Dimension(WIDTH_BUTTONS, HEIGHT_BUTTONS));
        this.add(no);
    }

    private void setupText() {
        // Create a line border with the specified color and width
        Border border = BorderFactory.createLineBorder(Color.BLACK, 5);

        // Create a font to make the text readable
        Font font = new Font("Open Sans", Font.BOLD, 20);

        setupLightsText(font, border);
        setupInstrumentsText(font, border);
        setupCurrentBeatText(font, border);
        setupNextBeatText(font, border);
        setupClickerText(font, border);
        setupGaugeText(font, border);
    }

    private void setupGaugeText(Font font, Border border) {
        JTextArea gauge = new JTextArea("The needle will move depending on how close \nyour " +
                "guess was to the actual beat.\nThe goal is to move the needle to the green side.");

        // Setting up the JTextArea
        setupJTextArea(gauge, font, border);
    }

    private void setupClickerText(Font font, Border border) {
        JTextArea clicker = new JTextArea("Click the hand button when you believe" +
                "\nthe current beat is occurring");

        // Setting up the JTextArea
        setupJTextArea(clicker, font, border);
    }

    private void setupNextBeatText(Font font, Border border) {
        JTextArea nextBeat = new JTextArea("The 'Next Beat' is the beat \nthat you will need " +
                "to find in the next \nbar of Salsa music");

        // Setting up the JTextArea
        setupJTextArea(nextBeat, font, border);
    }

    private void setupCurrentBeatText(Font font, Border border) {
        JTextArea currentBeat = new JTextArea("The 'Current Beat' is the beat \nthat you must find in the current" +
                "\nbar of Salsa music");

        // Setting up the JTextArea
        setupJTextArea(currentBeat, font, border);
    }

    private void setupInstrumentsText(Font font, Border border) {
        JTextArea newState = new JTextArea("These graphics will let you know what instruments are" +
                "\ncurrently playing and at what tempo");

        // Setting up the JTextArea
        setupJTextArea(newState, font, border);
    }

    private void setupLightsText(Font font, Border border) {
        JTextArea salsaBeats = new JTextArea("Salsa music in general follows a 4/4 structure." +
                "\nThere are 4 quarter notes per bar." +
                "\nSalsa is danced across 2 bars of music, i.e. 8 quarter \nnotes or beats (1 to 8)." +
                "\n\nThe lights above will display the beats of the Salsa \nmusic. " +
                "The first light on the left hand side represents" +
                "\nbeat number 1, and the last light on the right hand \nside represents beat number 8.");

        // Setting up the JTextArea
        setupJTextArea(salsaBeats, font, border);
    }

    private void layoutText() {
        // Layout the text for the first JTextArea
        int widthCenter = (int) (getDimension().width - 515)/2;

        this.getPanelLayout().putConstraint(SpringLayout.WEST, explanations.get(0), widthCenter,
                SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, explanations.get(0), 175,
                SpringLayout.NORTH, this);

        // Layout the text for the second JTextArea
        int center_instruments = (int) (getDimension().width - 520)/2;

        this.getPanelLayout().putConstraint(SpringLayout.WEST, explanations.get(1), center_instruments,
                SpringLayout.WEST, this);
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, explanations.get(1), 100,
                SpringLayout.NORTH, this);

        // Layout the text for the third JTextArea
        this.getPanelLayout().putConstraint(SpringLayout.WEST, explanations.get(2),
                50, SpringLayout.EAST, getCurrentBeat());
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, explanations.get(2),
                300, SpringLayout.NORTH, getLights().get(0));

        // Layout the text for the fourth JTextArea
        this.getPanelLayout().putConstraint(SpringLayout.EAST, explanations.get(3),
                -50, SpringLayout.WEST, getNextBeat());
        this.getPanelLayout().putConstraint(SpringLayout.NORTH, explanations.get(3),
                300, SpringLayout.NORTH, getLights().get(0));

        // Layout the text for the fifth JTextArea
        this.getPanelLayout().putConstraint(SpringLayout.WEST, explanations.get(4),
                10, SpringLayout.EAST, getCurrentBeat());
        this.getPanelLayout().putConstraint(SpringLayout.SOUTH, explanations.get(4),
                -100, SpringLayout.NORTH, getSkip());

        // Layout the text for the sixth JTextArea
        this.getPanelLayout().putConstraint(SpringLayout.WEST, explanations.get(5),
                10, SpringLayout.EAST, getGaugeGUI());
        this.getPanelLayout().putConstraint(SpringLayout.SOUTH, explanations.get(5),
                -100, SpringLayout.NORTH, getSkip());
    }

    private void layoutButtons() {
        GameView gameView = (GameView) this;

        // Dimensions to center the buttons
        int widthPosition = (int) (getDimension().getWidth() - WIDTH_BUTTONS)/2;

        // Layout for the Skip button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, skip,
                widthPosition, SpringLayout.WEST, this);

        this.getPanelLayout().putConstraint(SpringLayout.NORTH, skip,
                10, SpringLayout.SOUTH, gameView.getTempos());

        // Layout for the Practice button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, practice,
                widthPosition, SpringLayout.WEST, this);

        this.getPanelLayout().putConstraint(SpringLayout.NORTH, practice,
                10, SpringLayout.SOUTH, gameView.getTempos());

        // Layout for the next button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, next,
                widthPosition, SpringLayout.WEST, this);

        this.getPanelLayout().putConstraint(SpringLayout.NORTH, next,
                15, SpringLayout.SOUTH, skip);

        // Layout for the no button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, no,
                widthPosition, SpringLayout.WEST, this);

        this.getPanelLayout().putConstraint(SpringLayout.NORTH, no,
                15, SpringLayout.SOUTH, practice);

        // Layout for the back button
        this.getPanelLayout().putConstraint(SpringLayout.WEST, back,
                widthPosition, SpringLayout.WEST, this);

        this.getPanelLayout().putConstraint(SpringLayout.NORTH, back,
                15, SpringLayout.SOUTH, next);
    }

    /* Helper method to set up a JTextArea */
    private void setupJTextArea(JTextArea text, Font font, Border border) {
        // Setting up the font and border
        text.setFont(font); text.setBorder(border);

        // Transparent background and set the JTextArea as invisible
        text.setBackground(null); text.setVisible(false);

        // Adding the JTextArea to the ArrayList explanations
        this.explanations.add(text);

        // Adding the JTextAreas to the view
        this.add(text);
    }
}
