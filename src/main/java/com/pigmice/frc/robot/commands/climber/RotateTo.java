package com.pigmice.frc.robot.commands.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.subsystems.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RotateTo extends ParallelCommandGroup {
    private boolean infinite;

    public RotateTo(Rotato rotato, double angle) {
        this(rotato, angle, false, () -> false);
    }

    public RotateTo(Rotato rotato, double angle, BooleanSupplier orPower) {
        this(rotato, angle, false, orPower);
    }

    public RotateTo(Rotato rotato, double angle, boolean infinite) {
        this(rotato, () -> angle, false, () -> false);
    }

    public RotateTo(Rotato rotato, DoubleSupplier angle, boolean infinite) {
        this(rotato, angle, infinite, () -> false);
    }

    public RotateTo(Rotato rotato, double angle, boolean infinite, BooleanSupplier orPower) {
        this(rotato, () -> angle, false, orPower);
    }

    public RotateTo(Rotato rotato, DoubleSupplier angle, BooleanSupplier orPower) {
        this(rotato, angle, false, orPower);
    }

    public RotateTo(Rotato rotato, DoubleSupplier angle, boolean infinite, BooleanSupplier orPower) {
        super(new RotateOneTo(rotato.getLeft(), angle, infinite, orPower),
                new RotateOneTo(rotato.getRight(), angle, infinite, orPower));

        this.infinite = infinite;

        addRequirements(rotato.getLeft(), rotato.getRight());
    }

    @Override
    public boolean isFinished() {
        return !this.infinite;
    }
}
