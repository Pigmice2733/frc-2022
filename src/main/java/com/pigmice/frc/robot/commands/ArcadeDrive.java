package com.pigmice.frc.robot.commands;

import java.util.function.DoubleSupplier;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ArcadeDrive extends CommandBase {
    private final Drivetrain drivetrain;
    private final DoubleSupplier forward;
    private final DoubleSupplier rotation;

    public ArcadeDrive(Drivetrain drivetrain, DoubleSupplier forward, DoubleSupplier rotation) {
        this.drivetrain = drivetrain;
        this.forward = forward;
        this.rotation = rotation;
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(forward.getAsDouble(), rotation.getAsDouble());
    }
}