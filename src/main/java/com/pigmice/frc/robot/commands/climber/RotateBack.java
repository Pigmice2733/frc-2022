package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

/*
public class RotateBack extends CommandBase {
    private Climber climber;
    private double angle; // in degrees
    private double countAngle;

    public RotateBack(Climber climber, double angle) {
        this.climber = climber;
        this.angle = angle;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        countAngle = 0d;
        climber.setRotateSpeed(1);
    }

    @Override
    public void execute() {
        countAngle += ClimberConfig.defaultRotateMotorSpeed * 360 / 3000;
    }

    @Override
    public boolean isFinished() {
        return (countAngle >= angle);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setRotateSpeed(0);
    }
} */

public class RotateBack extends ProfiledPIDCommand {
    private Climber climber;
    private double tError, tVelocity;

    public RotateBack(Climber climber, double angle) {
        // if distance is positive the arm lifts out, if negative it lifts in
        super(
            new ProfiledPIDController(
                ClimberProfileConfig.rotateP,
                ClimberProfileConfig.rotateI,
                ClimberProfileConfig.rotateD,
                new TrapezoidProfile.Constraints(ClimberProfileConfig.maxRotateVelocity, ClimberProfileConfig.maxRotateAcceleration)
            ),
            climber::getRotateAngle,
            angle,
            (output, setpoint) -> climber.setRotateSpeed(output),
            climber
        );

        this.tError = ClimberProfileConfig.tolerableError;
        this.tVelocity = ClimberProfileConfig.tolerableEndVelo;

        this.climber = climber;
        addRequirements(climber);

        getController().setTolerance(tError, tVelocity);
    }

    @Override
    public void initialize() {this.climber.reset();}

    @Override
    public void end(boolean interrupted) {this.climber.setRotateSpeed(0);}
}