package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RotateToHook extends ParallelCommandGroup {

    public RotateToHook(Rotato rotato) {
        this(rotato, false);
    }

    public RotateToHook(Rotato rotato, boolean infinite) {
        super(new RotateOneTo(rotato.getLeft(), ClimberConfig.angleToRung - 5.0, infinite),
                new RotateOneTo(rotato.getRight(), ClimberConfig.angleToRung - 5.0, infinite));

        addRequirements(rotato.getLeft(), rotato.getRight());
    }
}
