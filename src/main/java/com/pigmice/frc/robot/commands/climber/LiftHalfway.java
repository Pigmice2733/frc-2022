package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

public class LiftHalfway extends LiftTo {
    public LiftHalfway(Climber climber) {
        super(climber, ClimberConfig.maxLiftHeight / 2.0, false);
    }

    public LiftHalfway(Climber climber, boolean infinite) {
        super(climber, ClimberConfig.maxLiftHeight / 2.0, infinite);
    }
}
