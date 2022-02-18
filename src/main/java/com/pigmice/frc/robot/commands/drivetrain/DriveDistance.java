package com.pigmice.frc.robot.commands.drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import com.pigmice.frc.lib.utils.Point;
import com.pigmice.frc.robot.subsystems.Drivetrain;

public class DriveDistance extends PIDCommand {
    private final Drivetrain drivetrain;
    private final double maxError = 0.05;
    private final double maxVelocity = 1;

    public DriveDistance(double distance, Drivetrain drivetrain) {
        super(
            new PIDController(2.0, 0.0, 0.0),
            drivetrain::getDistanceFromStart,
            distance,
            output -> drivetrain.tankDrive(output, output),
            drivetrain
        );

        this.drivetrain = drivetrain;

        getController().setTolerance(maxError, maxVelocity);
    }

    @Override
    public void initialize() {
        this.drivetrain.resetPose();
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}
