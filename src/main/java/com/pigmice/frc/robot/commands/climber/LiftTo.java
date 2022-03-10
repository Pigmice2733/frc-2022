package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class LiftTo extends ProfiledPIDCommand {
    private Climber climber;
    private boolean infinite;

    /**
     * Create a command to move the lift arm to a vertical distance from where it
     * was when robot started
     * 
     * @param climber  Climber subsystem
     * @param distance Distance in inches
     */
    public LiftTo(Climber climber, double distance) {
        this(climber, distance, false);
    }

    /**
     * Create a command to move the lift arm to a vertical distance from where it
     * was when robot started
     * 
     * @param climber  Climber subsystem
     * @param distance Distance in inches
     * @param infinite True makes the command never finish
     */
    public LiftTo(Climber climber, double distance, boolean infinite) {
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

        this.climber = climber;
        this.infinite = infinite;

        this.m_requirements.clear();

        getController().setTolerance(ClimberProfileConfig.liftTolerableError,
                ClimberProfileConfig.liftTolerableEndVelocity);
    }

    @Override
    public void end(boolean interrupted) {
        this.climber.setLiftOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        System.out.println(
                "LIFT | DISTANCE FROM SETPOINT: "
                        + (getController().getSetpoint().position - climber.getLiftDistance())
                        + " SETPOINT: " + getController().getSetpoint().position + " | AT SETPOINT? "
                        + getController().atGoal());
        return !this.infinite && getController().atGoal();
    }
}