package com.pigmice.frc.robot.commands.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.climber.AbstractRotate;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class RotateOneTo extends ProfiledPIDCommand {
    private AbstractRotate rotato;
    private boolean infinite;

    public RotateOneTo(AbstractRotate rotato, double angle) {
        this(rotato, () -> angle, () -> false);
    }

    public RotateOneTo(AbstractRotate rotato, double angle, BooleanSupplier orPower) {
        this(rotato, () -> angle, orPower);
    }

    public RotateOneTo(AbstractRotate rotato, double angle, boolean infinite) {
        this(rotato, () -> angle, infinite, () -> false);
    }

    public RotateOneTo(AbstractRotate rotato, double angle, boolean infinite, BooleanSupplier orPower) {
        this(rotato, () -> angle, infinite, orPower);
    }

    /**
     * Create a command to rotate the rotating arm to an angle
     * 
     * @param climber Climber subsystem
     * @param angle   Angle to rotate to in degrees
     */
    public RotateOneTo(AbstractRotate rotato, DoubleSupplier angle, BooleanSupplier orPower) {
        this(rotato, angle, false, orPower);
    }

    public RotateOneTo(AbstractRotate rotato, DoubleSupplier angle) {
        this(rotato, angle, false, () -> false);
    }

    /**
     * Create a command to rotate rotating arm to an angle in degrees.
     * 
     * @param climber  Climber subsystem
     * @param angle    Angle to rotate to in degrees
     * @param infinite True makes the command never finish
     */
    public RotateOneTo(AbstractRotate rotato, DoubleSupplier angle, boolean infinite, BooleanSupplier orPower) {
        super(
                new ProfiledPIDController(
                        ClimberProfileConfig.rotateP,
                        ClimberProfileConfig.rotateI,
                        ClimberProfileConfig.rotateD,
                        new TrapezoidProfile.Constraints(ClimberProfileConfig.maxRotateVelocity,
                                ClimberProfileConfig.maxRotateAcceleration)),
                rotato::getRotateAngle,
                angle,
                (output, setpoint) -> {
                    System.out.println("ROTATE MOTOR OUTPUT " + output);
                    rotato.setOutput(orPower.getAsBoolean() ? angle.getAsDouble() : output);
                },
                rotato);

        this.rotato = rotato;
        this.infinite = infinite;

        addRequirements(rotato);

        getController().setTolerance(ClimberProfileConfig.angleTolerableError,
                ClimberProfileConfig.angleTolerableEndVelocity);
    }

    @Override
    public void end(boolean interrupted) {
        this.rotato.setOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        System.out.println(
                "ROTATE | DISTANCE FROM SETPOINT: "
                        + (getController().getSetpoint().position - rotato.getRotateAngle())
                        + " SETPOINT: " + getController().getSetpoint().position + " | AT SETPOINT? "
                        + getController().atGoal());
        return !this.infinite && getController().atGoal();
    }
}