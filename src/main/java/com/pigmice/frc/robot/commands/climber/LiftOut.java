package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

/*
public class LiftOut extends CommandBase {
    private Climber climber;
    private double revolutions;
    private double distance; // in inches
    private double countRevo;

    public LiftOut(Climber climber, double distance) {
        this.climber = climber;
        this.distance = distance;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        revolutions = distance / (ClimberConfig.liftMotorRadius * 2 * Math.PI);
        countRevo = 0d;
        climber.setLiftSpeed(1);
    }

    @Override
    public void execute() {
        countRevo += ClimberConfig.maxLiftMotorSpeed / 3000;
    }

    @Override
    public boolean isFinished() {
        return (countRevo >= revolutions);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setLiftSpeed(0);
    }
} */

public class LiftOut extends ProfiledPIDCommand {
    private Climber climber;
    private double tError, tVelocity;

    public LiftOut(Climber climber, double distance) {
        super(
            new ProfiledPIDController(
                ClimberProfileConfig.liftP,
                ClimberProfileConfig.liftI,
                ClimberProfileConfig.liftD,
                new TrapezoidProfile.Constraints(ClimberProfileConfig.maxLiftVelocity, ClimberProfileConfig.maxLiftAcceleration)
            ),
            climber::getLiftDistance,
            distance,
            (output, setpoint) -> climber.setLiftSpeed(output),
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
    public void end(boolean interrupted) {this.climber.setLiftSpeed(0);}
}