package com.pigmice.frc.robot;

/* import java.util.LinkedList;
import java.util.Queue; */

/** this here class is to find where yo ballz at */
public class BallTracker {
    /*
     * Queue<BallColor> balls = new LinkedList<>();
     * 
     * // set the first position to a color and shift everything else up
     * public void newBallStored(BallColor color) {
     * balls.add(color);
     * }
     * 
     * // get rid of the second position and shift everything else up
     * public void ballLaunched() {
     * balls.remove();
     * }
     * 
     * // return the color of ball in a spot. 0 is the next one to be shot, 1 is the
     * // second one to be shot
     * public BallColor getCompartment(int spotNumber) {
     * if (spotNumber == 0) {
     * return balls.peek();
     * } else {
     * return balls.element();
     * }
     * }
     * 
     * enum BallColor {
     * RED,BLUE,NONE
     * }
     */
    enum Colors {
        RED, BLUE, NONE
    }

    Colors[] balls = { Colors.NONE, Colors.NONE };

    public boolean isEmpty() {
        if (balls[0] == Colors.NONE && balls[1] == Colors.NONE) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (balls[0] == Colors.NONE || balls[1] == Colors.NONE) {
            return false;
        }
        return true;
    }

    public void newBall(Colors color) {
        if (isEmpty()) {
            balls[0] = color;
        } else {
            balls[1] = color;
        }
    }

    public void ballLaunched() {
        balls[0] = balls[1];
        balls[1] = Colors.NONE;
    }

    public Colors getColor(int spot) {
        return balls[spot];
    }
}