package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

public class TurnToAngle extends PIDCommand {
    private final double errorThreshold = 0.05;
    private final double turnSpeedThreshold = 1.0;

    private Drivetrain drivetrain;

    public TurnToAngle(double targetHeading, boolean absolute, Drivetrain drivetrain) {
        super(
                new PIDController(0.1, 0.02, 0.0),
                drivetrain::getHeading,
                absolute ? targetHeading : targetHeading + drivetrain.getHeading(),
                output -> drivetrain.arcadeDrive(0, output),
                drivetrain);

        this.drivetrain = drivetrain;

        getController().enableContinuousInput(-180, 180);

        getController().setTolerance(errorThreshold, turnSpeedThreshold);
    }

    @Override
    public boolean isFinished() {
        System.out.println(
                "SETPOINT: " + getController().getSetpoint() + " | HEADING: " + drivetrain.getHeading()
                        + " | HEADING DIFFERENCE: "
                        + (getController().getSetpoint() - this.drivetrain.getHeading())
                        + " | IS AT SETPOINT? " + getController().atSetpoint());
        return getController().atSetpoint();
    }
}