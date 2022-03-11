package com.pigmice.frc.robot.commands.climber;

import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.subsystems.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RotateTo extends ParallelCommandGroup {
    public RotateTo(Rotato rotato, double angle) {
        this(rotato, angle, false);
    }

    public RotateTo(Rotato rotato, double angle, boolean infinite) {
        this(rotato, () -> angle, false);
    }

    public RotateTo(Rotato rotato, DoubleSupplier angle) {
        this(rotato, angle, false);
    }

    public RotateTo(Rotato rotato, DoubleSupplier angle, boolean infinite) {
        super(new RotateOneTo(rotato.getLeft(), angle, infinite), new RotateOneTo(rotato.getRight(), angle, infinite));

        addRequirements(rotato.getLeft(), rotato.getRight());
    }
}
