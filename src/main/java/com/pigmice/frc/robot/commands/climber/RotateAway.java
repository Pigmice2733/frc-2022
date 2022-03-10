package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Climber;

/**
 * Rotate the rotating hook out of the way for the upwards climb.
 */
public class RotateAway extends RotateTo {
    public RotateAway(Climber climber) {
        super(climber, -20.0, false);
    }

    public RotateAway(Climber climber, boolean infinite) {
        super(climber, -20.0, infinite);
    }
}
