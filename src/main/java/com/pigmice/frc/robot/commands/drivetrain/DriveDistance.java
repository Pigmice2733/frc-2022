package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class DriveDistance extends ProfiledPIDCommand {
    private final Drivetrain drivetrain;
    private final double maxError = 0.01;
    private final double maxVelocity = 1.0;

    public DriveDistance(double distance, Drivetrain drivetrain) {
        super(
                new ProfiledPIDController(1.5, 0.5, 0.3, new TrapezoidProfile.Constraints(1.0, 1.5)),
                drivetrain::getDistanceFromStart,
                distance,
                (output, setpoint) -> drivetrain.tankDrive(output, output),
                drivetrain);

        this.drivetrain = drivetrain;

        getController().setTolerance(maxError, maxVelocity);
    }

    @Override
    public void initialize() {
        this.drivetrain.resetPose();
    }

    @Override
    public boolean isFinished() {
        System.out.println(
                "DISTANCE FROM SETPOINT: "
                        + (getController().getSetpoint().position - drivetrain.getDistanceFromStart())
                        + " | AT SETPOINT? " + getController().atGoal());
        return getController().atGoal();
    }
}
