package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RotateToTarget extends ParallelCommandGroup {

    public RotateToTarget(Rotato rotato) {
        this(rotato, false);
    }

    public RotateToTarget(Rotato rotato, boolean infinite) {
        super(new RotateOneTo(rotato.getLeft(), ClimberConfig.angleToRung + 0.4, infinite),
                new RotateOneTo(rotato.getRight(), ClimberConfig.angleToRung + 0.4, infinite));

        addRequirements(rotato.getLeft(), rotato.getRight());
    }
}
