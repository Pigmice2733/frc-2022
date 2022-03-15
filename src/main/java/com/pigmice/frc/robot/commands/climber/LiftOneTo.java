package com.pigmice.frc.robot.commands.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.climber.AbstractLift;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class LiftOneTo extends ProfiledPIDCommand {
    private AbstractLift lifty;
    private boolean infinite;

    public LiftOneTo(AbstractLift lifty, double angle) {
        this(lifty, () -> angle);
    }

    public LiftOneTo(AbstractLift lifty, double angle, BooleanSupplier doPower) {
        this(lifty, angle, false, doPower);
    }

    public LiftOneTo(AbstractLift lifty, double angle, boolean infinite) {
        this(lifty, () -> angle, infinite);
    }

    public LiftOneTo(AbstractLift lifty, double angle, boolean infinite, BooleanSupplier doPower) {
        this(lifty, () -> angle, infinite, doPower);
    }

    /**
     * Create a command to move the lift arm to a vertical distance from where it
     * was when robot started
     * 
     * @param climber  Climber subsystem
     * @param distance Distance in inches
     */
    public LiftOneTo(AbstractLift lifty, DoubleSupplier distance) {
        this(lifty, distance, false);
    }

    public LiftOneTo(AbstractLift lifty, DoubleSupplier distance, boolean infinite) {
        this(lifty, distance, infinite, () -> false);
    }

    public LiftOneTo(AbstractLift lifty, DoubleSupplier distance, BooleanSupplier doPower) {
        this(lifty, distance, false, doPower);
    }

    /**
     * Create a command to move the lift arm to a vertical distance from where it
     * was when robot started
     * 
     * @param climber  Climber subsystem
     * @param distance Distance in inches
     * @param infinite True makes the command never finish
     */
    public LiftOneTo(AbstractLift lifty, DoubleSupplier distance, boolean infinite, BooleanSupplier doPower) {
        super(
                new ProfiledPIDController(
                        ClimberProfileConfig.liftP,
                        ClimberProfileConfig.liftI,
                        ClimberProfileConfig.liftD,
                        new TrapezoidProfile.Constraints(ClimberProfileConfig.maxLiftVelocity,
                                ClimberProfileConfig.maxLiftAcceleration)),
                lifty::getLiftDistance,
                distance,
                (output, setpoint) -> {
                    // System.out.println("LIFT MOTOR OUTPUT " + output);
                    lifty.setOutput(doPower.getAsBoolean() ? distance.getAsDouble() : output);
                },
                lifty);

        this.lifty = lifty;
        this.infinite = infinite;

        addRequirements(lifty);

        getController().setTolerance(ClimberProfileConfig.liftTolerableError,
                ClimberProfileConfig.liftTolerableEndVelocity);
    }

    @Override
    public void end(boolean interrupted) {
        this.lifty.setOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        // System.out.println(
        // "LIFT | DISTANCE FROM SETPOINT: "
        // + (getController().getSetpoint().position - lifty.getLiftDistance())
        // + " SETPOINT: " + getController().getSetpoint().position + " | AT SETPOINT? "
        // + getController().atGoal());
        return !this.infinite && getController().atGoal();
    }
}