package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Climber;

public class RotateToHook extends RotateTo {
    // exact angle to rung 57.41º
    public RotateToHook(Climber climber) {
        super(climber, 55.0, false);
    }

    public RotateToHook(Climber climber, boolean infinite) {
        super(climber, 55.0, infinite);
    }
}
