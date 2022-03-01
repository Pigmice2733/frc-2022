package com.pigmice.frc.robot;

import java.util.Deque;
import java.util.ArrayDeque;

//this here class is to find where yo ballz at
public class BallTracker{
    Deque<BallColor> balls = new ArrayDeque<BallColor>();

    public boolean isFull() {
        if (balls[1] == Colors.NONE) {
            return false;
        }
        else {return true;}
    }

    public int numBalls() {
        if (isEmpty()) {return 0;}
        if (isFull()) {return 2;}
        else return 1;
    }

    public void newBall(Colors color) {
        balls[1] = color;
        updateArray();
    }

    public void ballLaunched() {
        balls.removeFirst();
    }

    // return the color of ball in a spot. 0 is the next one to be shot, 1 is the
    // second one to be shot
    public BallColor getCompartment(int spotNumber) {
        if (spotNumber == 0) {
            return balls.getFirst();
        } else {
            if(balls.size() == 2){
                return balls.getLast();
            } else {
                return BallColor.NONE;
            }
        }
    }

    public boolean holdingColor(BallColor color){
        return balls.contains(color);
    }

    public enum BallColor {
        RED,BLUE,NONE
    }
}
