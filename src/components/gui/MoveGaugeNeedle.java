package components.gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.*;

/**
 * MoveGaugeNeedle Class will be used in conjunction with the RotateImage Class so that we can rotate the image to a
 * desired angle
 *
 * @author Gareth Iguasnia
 * @date 06/03/2020
 */
public class MoveGaugeNeedle {

    private RotateImage rotateImage;

    // The range that the needle will move from
    private final int RANGE = 270;
    private final int START = 225;
    private final int END = 495;

    // The amount of time the pointer will be held at the desired angle in milliseconds
    private final int HOLDING_POINTER = 200;

    // The amount of delay between each successive execution of a thread in milliseconds
    private final int PERIOD = 3;

    // Delta is the increments/decrements that the angle of the needle will move per 0.1 of the error value
    // There are 100 possible error values when rounding the error value to two decimal places
    private final double DELTA = (double) RANGE/100;
    private double currentAngle;

    // Executor service to manage the thread in charge of moving the needle to the angle produced from the error value
    private ScheduledExecutorService scheduledExecutorService;

    // Executor service to manage the thread in charge of moving the needle back to the START angle
    private ScheduledExecutorService returningNeedleExecutor;

    /**
     * Constructor for the MoveGaugeNeedle Class. The Executors are started up so that we can start using their
     * functionality.
     *
     * @param rotateImage A JPanel that holds the BufferedImage to be rotated
     */
    public MoveGaugeNeedle(RotateImage rotateImage) {
        this.rotateImage = rotateImage;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.returningNeedleExecutor = Executors.newScheduledThreadPool(1);
    }

    /**
     * Method sets the angle of the image to the desired starting position
     */
    public void setStartingPosition() {
        // Rotating the png image
        BufferedImage rotated = rotateImageByDegrees(rotateImage.getMaster(), START);
        rotateImage.setRotated(rotated);

        // Setting the current angle the png is at
        this.currentAngle = START;
    }

    /**
     * Method that moves the needle according to the error value. The error value should be a double to decimal places
     * and will be in the range of [0, 1].
     *
     * @param errorValue A double to 2 decimal places in the range of [0, 1]
     */
    public void moveNeedle(double errorValue) {
        // Restarting the executor service
        this.scheduledExecutorService.shutdownNow();
        this.returningNeedleExecutor.shutdownNow();

        // Awaiting for the executor to shut down
        try {
            this.scheduledExecutorService.awaitTermination(3, TimeUnit.MILLISECONDS);
            this.returningNeedleExecutor.awaitTermination(3, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.returningNeedleExecutor = Executors.newScheduledThreadPool(1);

        // Calculate the angle that the gauge needle needs to be at
        System.out.println("Delta: " + DELTA);
        System.out.println("Percent error value: " + (errorValue * 100));
        System.out.println("Angle being added: " + (DELTA * (errorValue * 100)) );
        double angleDestination = START + (DELTA * (errorValue * 100));
        System.out.println("The angle destination: " + angleDestination);
        // Calculating the time steps needed to get from the current angle to the desired angle
        double timeSteps = (angleDestination - currentAngle)/DELTA;
        System.out.println("The timeStep is: " + timeSteps);
        int time = Math.abs((int) timeSteps) * PERIOD;
        //System.out.println("The amount of milliseconds that will take to move the needle up is: " + time);

        // If the current angle > desired angle, then we need to move the needle backwards
        if (currentAngle > angleDestination) {
            //System.out.println("Current angle is bigger than the angle destination");
            moveNeedleOnce(angleDestination, false, scheduledExecutorService,
                    0, 3);
        }

        // If current angle < desired angle, then we need to move the needle forwards
        else {
            //System.out.println("Current angle is less than or equal to the angle destination");
            moveNeedleOnce(angleDestination, true, scheduledExecutorService,
                    0, 3);
        }

        // After we have finished moving the needle to the desired spot, we bring it back to the start
        moveNeedleOnce(225, false, returningNeedleExecutor,
                time + HOLDING_POINTER, 3);
    }

    /**
     * Method that rotates a BufferedImage by a specified angle
     *
     * @param img The BufferedImage to rotate
     * @param angle A double representing the angle we will rotate clockwise from the center of the image
     * @return A BufferedImage that has been rotated by the specified angle
     */
    public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w*cos + h*sin);
        int newHeight = (int) Math.floor(h*cos + w*sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w/2;
        int y = h/2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.setComposite(AlphaComposite.Clear);
        g2d.setComposite(AlphaComposite.Src);
        g2d.drawImage(img, 0, 0, rotateImage);
        g2d.dispose();

        return rotated;
    }

    /**
     * NeedleMovement inner private Class that implements Runnable and changes the angle by DELTA towards the desired angle. Once the
     * desired angle has been reached, the executor must be shutdown.
     *
     * @author Gareth Iguasnia
     * @date 06/03/2020
     */
    private class NeedleMovement implements Runnable {
        // The desired angle that we want to move the gauge needle
        private double desiredAngle;

        // Is the needle moving forward or backward
        private boolean isNeedleGoingForward;

        // The ScheduledExecutorService that this thread is being executed by
        private ScheduledExecutorService exec;

        /**
         * Constructor for the NeedleMovement inner class.
         *
         * @param desiredAngle The desired angle we want the image to rotate to
         * @param isNeedleGoingForward Is the needle image moving to the right (true) or to the left(false)?
         * @param exec The ScheduledExecutorService that is being used to executes this thread
         */
        public NeedleMovement(double desiredAngle, boolean isNeedleGoingForward, ScheduledExecutorService exec) {
            this.desiredAngle = desiredAngle;
            this.isNeedleGoingForward = isNeedleGoingForward;
            this.exec = exec;
        }

        /**
         * Overridden run method that moves the current angle to the desired angle by one DELTA. If desired angle has
         * been reached, then we shutdown the scheduled executor service
         */
        @Override
        public void run() {
            // Needle is going forward
            if (isNeedleGoingForward) {
                // If we have not reached or passed the desired angle and not passed the END bound
                if (!(currentAngle >= desiredAngle) && !(currentAngle >= END)) {
                    //System.out.println("(Increasing) Current angle: " + currentAngle);
                    // Needle is going forward - Adding delta to the current angle
                    currentAngle += DELTA;
                    BufferedImage rotated = rotateImageByDegrees(rotateImage.getMaster(), currentAngle);
                    rotateImage.setRotated(rotated);
                }
                // Once we have reached or just passed the desired angle
                else
                    exec.shutdownNow();
            }

            // Needle is going backward
            else {
                // If we have not reached or passed below the desired angle and not passed below the START bound
                if (!(currentAngle <= desiredAngle) && !(currentAngle <= START)) {
                    //System.out.println("(Decreasing) Current angle: " + currentAngle);
                    // Needle is going backward - Subtracting delta from the current angle
                    currentAngle -= DELTA;
                    BufferedImage rotated = rotateImageByDegrees(rotateImage.getMaster(), currentAngle);
                    rotateImage.setRotated(rotated);
                }
                // Once we have reached or just passed below the desired angle
                else
                    exec.shutdownNow();
            }
        }
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    /* Helper method to run the logic in a thread as sometimes the needle would not move in sync with the input */
    private void moveNeedleOnce(double desiredAngle, boolean isNeedleGoingForward,
                                ScheduledExecutorService scheduledExecutorService, int initialDelay, int period) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NeedleMovement task = new NeedleMovement(desiredAngle, isNeedleGoingForward, scheduledExecutorService);
                scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
            }
        }).start();
    }

    /*private static class Practice implements Runnable {

        int time = 3;
        ScheduledExecutorService scheduledExecutorService;
        private String message;

        public Practice(ScheduledExecutorService scheduledExecutorService, String message) {
            this.scheduledExecutorService = scheduledExecutorService;
            this.message = message;
        }

        @Override
        public void run() {
            if (time > 0) {
                time--;
                System.out.println(message);
            }
            else {
                scheduledExecutorService.shutdown();
            }
        }
    }

    private static class Practice2 implements Runnable {

        ScheduledExecutorService scheduledExecutorService;

        public Practice2(ScheduledExecutorService scheduledExecutorService) {
            this.scheduledExecutorService = scheduledExecutorService;
        }

        @Override
        public void run() {
            scheduledExecutorService.shutdownNow();
            System.out.println("No more print statements after this");
        }
    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Practice task = new Practice(scheduledExecutorService, "Practice makes perfect");




        //System.out.println("Submitting task at " + System.nanoTime() + " to be executed after 5 seconds.");
        //scheduledExecutorService.schedule(task, 5, TimeUnit.SECONDS);

        //scheduledExecutorService.shutdown();
        scheduledExecutorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Practice task2 = new Practice(scheduledExecutorService, "Again");
        scheduledExecutorService.scheduleAtFixedRate(task2, 3, 1, TimeUnit.SECONDS);

        //scheduledExecutorService = Executors.newScheduledThreadPool(1);
        //Practice2 task3 = new Practice2(scheduledExecutorService);
        //scheduledExecutorService.scheduleAtFixedRate(task3, 2, 1, TimeUnit.SECONDS);
    }*/
}
