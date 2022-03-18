package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.climber.Lifty;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class LiftHalfway extends ParallelCommandGroup {
    public LiftHalfway(Lifty lifty) {
        this(lifty, false);
    }

    public LiftHalfway(Lifty lifty, boolean infinite) {
        super(new LiftOneTo(lifty.getLeft(), ClimberConfig.maxLiftHeight / 2.0, infinite),
                new LiftOneTo(lifty.getRight(), ClimberConfig.maxLiftHeight / 2.0, infinite));

        addRequirements(lifty.getLeft(), lifty.getRight());
    }
}
