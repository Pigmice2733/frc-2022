package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Climber;

/**
 * Lifts lift arm off of bar when suspended by both arms so that it's then only
 * suspended by the rotating one.
 * 
 * @param climber Climber subsystem
 */
public class LiftOffBar extends LiftTo {
    public LiftOffBar(Climber climber) {
        super(climber, 6.0, false);
    }

    public LiftOffBar(Climber climber, boolean infinite) {
        super(climber, 6.0, infinite);
    }
}
