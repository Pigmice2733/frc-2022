package com.pigmice.frc.robot;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

// this here class is to find where yo ballz at
public class BallTracker {

    private static final int SIZE = 2;

    LinkedList<Alliance> balls = new LinkedList<>();

    /**
     * Adds a ball to the queue. Used when a ball has been collected.
     */
    public void newBallStored(Alliance color) {
        if (!isFull()) {
            balls.add(color);
        }
    }

    /**
     * Removes the ball at the head of the queue. Used when a ball is shot.
     */
    public Alliance ballLaunched() {
        return balls.isEmpty() ? Alliance.Invalid : balls.poll();
    }

    /**
     * Returns the ball in a specific position in the indexer.
     */
    public Alliance getBallInPosition(int slot) {
        // BallType.NONE should never be in the queue
        return balls.size() > slot ? balls.get(slot) : Alliance.Invalid;
    }

    public boolean isFull() {
        return balls.size() >= SIZE;
    }

    public int getSize() {
        return balls.size();
    }
}
