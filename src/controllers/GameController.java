package controllers;

import components.*;
import events.ClipInformationEvent;
import events.ClipInformationListener;
import main.SalsaController;
import main.SalsaModel;
import views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public abstract class GameController extends SalsaController implements ClipInformationListener {
    // To have access to the buttons in the SimulationView Class
    private GameView gameView;

    // As we are programming to an interface, we can switch out the GameStatusFunction with ease
    private GameStatusFunction gameStatusFunction;

    // Random Generator to be used to get the next beat that the user will be tested on
    // and to find a starting state
    private Random randomGenerator;

    private final int TIME_WINDOW = 3;

    /**
     * Constructor for the GameController. This will only be called by sub classes using the super
     * keyword as this class should never be instantiated.
     *
     * @param salsaModel     A SalsaModel object that contains the data of the MVC
     * @param controllerName A String object representing the name of the controller
     */
    public GameController(SalsaModel salsaModel, String controllerName, GameView gameView) {
        super(salsaModel, controllerName);
        this.gameView = gameView;
        this.gameStatusFunction = new DynamicGameDifficultyBalancing();
        this.randomGenerator = new Random();

        initClickerButton();
        initStartButton();
    }

    public abstract void calculateErrorValue();

    public abstract void initStartButton();

    public abstract State chooseStartingState();

    @Override
    public abstract void onClipInfoReadyEvent(ClipInformationEvent e);

    private void initClickerButton() {
        ActionListener click = new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                System.out.println("The current beat I am trying to find: " + getSalsaModel().getCurrentBeat());
                System.out.println("The time I perceive the beat to be: " + System.currentTimeMillis());
                System.out.println();

                // If a Salsa audio clip is currently playing, then proceed
                if (!getSalsaModel().isCountdownCurrentlyPlaying()) {
                    // Checking if has previously clicked in the assigned time windows
                    System.out.println("In InitButtonClicker");
                    System.out.println("Button Clicker b4 change: " + getSalsaModel().getButtonClickerTracker());
                    System.out.println("Window Tracker b4 change: " + getSalsaModel().getWindowTracker());
                    System.out.println("hasClickedOnce1 b4 change: " + getSalsaModel().hasClickedOnce1());
                    System.out.println("hasClickedOnce2 b4 change: " + getSalsaModel().hasClickedOnce2());

                    if (getSalsaModel().getButtonClickerTracker() == 1 && !getSalsaModel().hasClickedOnce1()) {
                        getSalsaModel().setHasClickedOnce1(true);
                        getSalsaModel().increaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: " + getSalsaModel().getButtonClickerTracker());
                        System.out.println("Window Tracker after change: " + getSalsaModel().getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + getSalsaModel().hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + getSalsaModel().hasClickedOnce2());
                        calculateErrorValue();

                        System.out.println("I have been printed");System.out.println();
                    }

                    else if (getSalsaModel().getButtonClickerTracker() == 2 && !getSalsaModel().hasClickedOnce2()) {
                        getSalsaModel().setHasClickedOnce2(true);
                        getSalsaModel().decreaseButtonClickerTracker();
                        System.out.println("Button Clicker after change: " + getSalsaModel().getButtonClickerTracker());
                        System.out.println("Window Tracker after change: " + getSalsaModel().getWindowTracker());
                        System.out.println("hasClickedOnce1 after change: " + getSalsaModel().hasClickedOnce1());
                        System.out.println("hasClickedOnce2 after change: " + getSalsaModel().hasClickedOnce2());
                        calculateErrorValue();
                        System.out.println("I have been printed");System.out.println();
                    }
                }
            }
        };
        gameView.getBeatClicker().addActionListener(click);
    }

    public GameStatusFunction getGameStatusFunction() {
        return gameStatusFunction;
    }

    public Random getRandomGenerator() {
        return randomGenerator;
    }

    public int getTIME_WINDOW() {
        return TIME_WINDOW;
    }

    public GameView getGameView() {
        return gameView;
    }
}
