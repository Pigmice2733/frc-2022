package com.pigmice.frc.robot;

import java.util.LinkedList;

// this here class is to find where yo ballz at
public class BallTracker {

    private static final int SIZE = 2;

    LinkedList<BallType> balls = new LinkedList<>();

    /**
     * Adds a ball to the queue. Used when a ball has been collected.
     */
    public void newBallStored(BallType color) {
        if (!isFull()) {
            balls.add(color);
        }
    }

    /**
     * Removes the ball at the head of the queue. Used when a ball is shot.
     */
    public BallType ballLaunched() {
        return balls.isEmpty() ? BallType.NONE : balls.poll();
    }

    /**
     * Returns the ball in a specific position in the indexer.
     */
    public BallType getBallInPosition(int slot) {
        // BallType.NONE should never be in the queue
        return balls.size() > slot ? BallType.NONE : balls.get(slot);
    }

    public boolean isFull() {
        return balls.size() >= SIZE;
    }

    enum BallType {
        RED, BLUE, NONE
    }
}
