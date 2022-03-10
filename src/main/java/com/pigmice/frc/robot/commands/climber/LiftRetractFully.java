package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

public class LiftRetractFully extends LiftTo {
    public LiftRetractFully(Climber climber) {
        super(climber, ClimberConfig.minLiftHeight, false);
    }

    public LiftRetractFully(Climber climber, boolean infinite) {
        super(climber, ClimberConfig.minLiftHeight, infinite);
    }
}