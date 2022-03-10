package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Climber;

public class RotateToVertical extends RotateTo {
    public RotateToVertical(Climber climber) {
        super(climber, 0.0, false);
    }

    public RotateToVertical(Climber climber, boolean infinite) {
        super(climber, 0.0, infinite);
    }
}