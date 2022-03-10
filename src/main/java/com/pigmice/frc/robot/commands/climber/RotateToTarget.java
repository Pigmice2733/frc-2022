package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Climber;

public class RotateToTarget extends RotateTo {
    // exact angle to rung 57.41º

    public RotateToTarget(Climber climber) {
        super(climber, 60.0, false);
    }

    public RotateToTarget(Climber climber, boolean infinite) {
        super(climber, 60.0, infinite);
    }
}
