package com.pigmice.frc.robot.commands.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.subsystems.Lifty;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class LiftTo extends ParallelCommandGroup {
    private boolean infinite;

    public LiftTo(Lifty lifty, double distance) {
        this(lifty, distance, false, () -> false);
    }

    public LiftTo(Lifty lifty, double distance, BooleanSupplier doPower) {
        this(lifty, () -> distance, doPower);
    }

    public LiftTo(Lifty lifty, double distance, boolean infinite) {
        this(lifty, () -> distance, false);
    }

    public LiftTo(Lifty lifty, double distance, boolean infinite, BooleanSupplier doPower) {
        this(lifty, () -> distance, infinite, doPower);
    }

    public LiftTo(Lifty lifty, DoubleSupplier distance) {
        this(lifty, distance, false);
    }

    public LiftTo(Lifty lifty, DoubleSupplier distance, boolean infinite) {
        this(lifty, distance, infinite, () -> false);
    }

    public LiftTo(Lifty lifty, DoubleSupplier distance, BooleanSupplier doPower) {
        this(lifty, distance, false, doPower);
    }

    public LiftTo(Lifty lifty, DoubleSupplier distance, boolean infinite, BooleanSupplier doPower) {
        super(new LiftOneTo(lifty.getLeft(), distance, infinite, doPower),
                new LiftOneTo(lifty.getRight(), distance, infinite, doPower));

        this.infinite = infinite;

        addRequirements(lifty.getLeft(), lifty.getRight());
    }

    @Override
    public boolean isFinished() {
        return !this.infinite;
    }
}
