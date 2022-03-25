package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class DriveDistance extends ProfiledPIDCommand {
    private final Drivetrain drivetrain;
    private final double maxError = 0.05;
    private final double maxVelocity = 1.0;

    public DriveDistance(Drivetrain drivetrain, double distance) {
        super(
                new ProfiledPIDController(1.25, 0.0005, 0.005, new TrapezoidProfile.Constraints(1.0, 1.5)),
                drivetrain::getDistanceFromStart,
                Math.abs(distance),
                (output, setpoint) -> {
                    SmartDashboard.putNumber("Distance", drivetrain.getDistanceFromStart());
                    output = MathUtil.clamp(output, -0.40, 0.40);
                    drivetrain.arcadeDrive(output * (distance < 0 ? -1 : 1), 0);
                },
                drivetrain);

        this.drivetrain = drivetrain;

        this.setName("Drive Distance");

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
