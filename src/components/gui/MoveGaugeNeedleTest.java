package components.gui;

import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;

class MoveGaugeNeedleTest {

    @Test
    void setStartingPosition() {
        RotateImage rotateImage = new RotateImage("src/assets/graphics/" + "pointer_turning.png");
        MoveGaugeNeedle moveGaugeNeedle = new MoveGaugeNeedle(rotateImage);
        moveGaugeNeedle.setStartingPosition();

        assertEquals(225, moveGaugeNeedle.getCurrentAngle());
    }

    @Test
    void moveNeedle() {
        RotateImage rotateImage = new RotateImage("src/assets/graphics/" + "pointer_turning.png");
        MoveGaugeNeedle moveGaugeNeedle = new MoveGaugeNeedle(rotateImage);
        moveGaugeNeedle.setStartingPosition();
        moveGaugeNeedle.moveNeedle(0.5);

        TimerTask task = new TimerTask () {

            @Override
            public void run() {
                assertEquals(360, moveGaugeNeedle.getCurrentAngle());
            }
        };

        Timer timer = new Timer("Timer");

        long delay = 155;
        timer.schedule(task, delay);


    }
}