package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

public class LiftExtendFully extends LiftTo {
    public LiftExtendFully(Climber climber) {
        super(climber, ClimberConfig.maxLiftHeight, false);
    }

    public LiftExtendFully(Climber climber, boolean infinite) {
        super(climber, ClimberConfig.maxLiftHeight, infinite);
    }
}