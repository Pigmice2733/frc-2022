package com.pigmice.frc.robot.commands.climber;

import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.subsystems.Lifty;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class LiftTo extends ParallelCommandGroup {
    public LiftTo(Lifty lifty, double distance) {
        this(lifty, distance, false);
    }

    public LiftTo(Lifty lifty, double distance, boolean infinite) {
        this(lifty, () -> distance, false);
    }

    public LiftTo(Lifty lifty, DoubleSupplier distance) {
        this(lifty, distance, false);
    }

    public LiftTo(Lifty lifty, DoubleSupplier distance, boolean infinite) {
        super(new LiftOneTo(lifty.getLeft(), distance, infinite), new LiftOneTo(lifty.getRight(), distance, infinite));

        addRequirements(lifty.getLeft(), lifty.getRight());
    }
}
