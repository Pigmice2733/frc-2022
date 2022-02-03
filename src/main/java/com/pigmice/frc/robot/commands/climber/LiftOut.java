package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class LiftOut extends CommandBase {
    private Climber climber;
    private double revolutions;

    public LiftOut(Climber climber) {
        this.climber = climber;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        revolutions = ClimberConfig.liftLength / 2*Math.PI;
    }

    
}
