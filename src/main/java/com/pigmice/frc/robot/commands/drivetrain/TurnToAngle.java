package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class TurnToAngle extends ProfiledPIDCommand {
    private final double errorThreshold = 0.05;
    private final double turnSpeedThreshold = 1.0;

    private Drivetrain drivetrain;

    public TurnToAngle(double targetHeading, boolean absolute, Drivetrain drivetrain) {
        super(
                new ProfiledPIDController(0.1, 0.02, 0.0, new TrapezoidProfile.Constraints(0.1, 0.01)),
                drivetrain::getHeading,
                absolute ? targetHeading : targetHeading + drivetrain.getHeading(),
                (output, setpoint) -> drivetrain.arcadeDrive(0, output),
                drivetrain);

        this.drivetrain = drivetrain;

        getController().enableContinuousInput(-180, 180);

        getController().setTolerance(errorThreshold, turnSpeedThreshold);
    }

    @Override
    public boolean isFinished() {
        System.out.println(
                "SETPOINT: " + getController().getSetpoint().position + " | HEADING: " + drivetrain.getHeading()
                        + " | HEADING DIFFERENCE: "
                        + (getController().getSetpoint().position - this.drivetrain.getHeading())
                        + " | IS AT SETPOINT? " + getController().atGoal());
        return getController().atGoal();
    }
}