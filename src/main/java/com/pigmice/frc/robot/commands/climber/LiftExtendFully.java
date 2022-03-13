package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Lifty;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class LiftExtendFully extends ParallelCommandGroup {
    public LiftExtendFully(Lifty lifty) {
        this(lifty, false);
    }

    public LiftExtendFully(Lifty lifty, boolean infinite) {
        super(new LiftOneTo(lifty.getLeft(), ClimberConfig.maxLiftHeight, infinite),
                new LiftOneTo(lifty.getRight(), ClimberConfig.maxLiftHeight, infinite));

        addRequirements(lifty.getLeft(), lifty.getRight());
    }
}