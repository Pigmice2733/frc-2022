package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class RotateTo extends ProfiledPIDCommand {
    private Climber climber;
    private boolean infinite;

    /**
     * Create a command to rotate the rotating arm to an angle
     * 
     * @param climber Climber subsystem
     * @param angle   Angle to rotate to in degrees
     */
    public RotateTo(Climber climber, double angle) {
        this(climber, angle, false);
    }

    /**
     * Create a command to rotate rotating arm to an angle in degrees.
     * 
     * @param climber  Climber subsystem
     * @param angle    Angle to rotate to in degrees
     * @param infinite True makes the command never finish
     */
    public RotateTo(Climber climber, double angle, boolean infinite) {
        super(
                new ProfiledPIDController(
                        ClimberProfileConfig.rotateP,
                        ClimberProfileConfig.rotateI,
                        ClimberProfileConfig.rotateD,
                        new TrapezoidProfile.Constraints(ClimberProfileConfig.maxRotateVelocity,
                                ClimberProfileConfig.maxRotateAcceleration)),
                climber::getRotateAngle,
                angle,
                (output, setpoint) -> {
                    System.out.println("LIFT MOTOR OUTPUT " + output);
                    climber.setRotateOutput(output);
                },
                climber);

        this.climber = climber;
        this.infinite = infinite;

        this.m_requirements.clear();

        getController().setTolerance(ClimberProfileConfig.angleTolerableError,
                ClimberProfileConfig.angleTolerableEndVelocity);
    }

    @Override
    public void end(boolean interrupted) {
        this.climber.setRotateOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        System.out.println(
                "ROTATE | DISTANCE FROM SETPOINT: "
                        + (getController().getSetpoint().position - climber.getRotateAngle())
                        + " SETPOINT: " + getController().getSetpoint().position + " | AT SETPOINT? "
                        + getController().atGoal());
        return !this.infinite && getController().atGoal();
    }
}