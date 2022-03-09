package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class RotateFront extends ProfiledPIDCommand {
    private Climber climber;
    private double tError, tVelocity;

    public RotateFront(Climber climber, double angle) {
        // if distance is positive the arm lifts out, if negative it lifts in
        super(
                new ProfiledPIDController(
                        ClimberProfileConfig.rotateP,
                        ClimberProfileConfig.rotateI,
                        ClimberProfileConfig.rotateD,
                        new TrapezoidProfile.Constraints(ClimberProfileConfig.maxRotateVelocity,
                                ClimberProfileConfig.maxRotateAcceleration)),
                () -> -1 * climber.getRotateAngle(),
                angle,
                (output, setpoint) -> climber.setRotateSpeed(-1 * output),
                climber);

        this.tError = ClimberProfileConfig.tolerableError;
        this.tVelocity = ClimberProfileConfig.tolerableEndVelo;

        this.climber = climber;
        addRequirements(climber);

        getController().setTolerance(tError, tVelocity);
    }

    @Override
    public void initialize() {
        this.climber.reset();
    }

    @Override
    public void end(boolean interrupted) {
        this.climber.setRotateSpeed(0);
    }
}