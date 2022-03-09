package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class LiftIn extends ProfiledPIDCommand {
    private Climber climber;
    private double tError, tVelocity;

    public LiftIn(Climber climber, double distance) {
        // if distance is positive the arm lifts out, if negative it lifts in
        super(
                new ProfiledPIDController(
                        ClimberProfileConfig.liftP,
                        ClimberProfileConfig.liftI,
                        ClimberProfileConfig.liftD,
                        new TrapezoidProfile.Constraints(ClimberProfileConfig.maxLiftVelocity,
                                ClimberProfileConfig.maxLiftAcceleration)),
                () -> -1 * (climber.getLiftDistance()),
                distance,
                (output, setpoint) -> climber.setLiftOutput(-1 * output),
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
        this.climber.setLiftSpeed(0);
    }
}