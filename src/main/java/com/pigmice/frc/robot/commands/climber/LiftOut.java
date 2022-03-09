package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class LiftOut extends ProfiledPIDCommand {
    private Climber climber;
    private double tError, tVelocity;

    public LiftOut(Climber climber, double distance) {
        super(
                new ProfiledPIDController(
                        ClimberProfileConfig.liftP,
                        ClimberProfileConfig.liftI,
                        ClimberProfileConfig.liftD,
                        new TrapezoidProfile.Constraints(ClimberProfileConfig.maxLiftVelocity,
                                ClimberProfileConfig.maxLiftAcceleration)),
                climber::getLiftDistance,
                distance,
                (output, setpoint) -> {
                    System.out.println("LIFT MOTOR OUTPUT " + output);
                    climber.setLiftOutput(output);
                },
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

    @Override
    public boolean isFinished() {
        System.out.println(
                "DISTANCE FROM SETPOINT: "
                        + (getController().getSetpoint().position - climber.getLiftDistance())
                        + " SETPOINT: " + getController().getSetpoint().position + " | AT SETPOINT? "
                        + getController().atGoal());
        return getController().atGoal();
    }
}