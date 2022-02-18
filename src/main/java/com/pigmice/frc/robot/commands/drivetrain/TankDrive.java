package com.pigmice.frc.robot.commands.drivetrain;

import java.util.function.DoubleSupplier;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class TankDrive extends CommandBase {
    private final Drivetrain drivetrain;
    private final DoubleSupplier left;
    private final DoubleSupplier right;

    public TankDrive(Drivetrain drivetrain, DoubleSupplier left, DoubleSupplier right) {
        this.drivetrain = drivetrain;
        this.left = left;
        this.right = right;
    }

    @Override
    public void execute() {
        drivetrain.tankDrive(left.getAsDouble(), right.getAsDouble());
    }
}