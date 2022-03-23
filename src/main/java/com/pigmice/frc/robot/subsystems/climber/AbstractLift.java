package com.pigmice.frc.robot.subsystems.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.Constants.ClimberConfig.LiftySetpoint;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class AbstractLift extends SubsystemBase {
    protected final double LIFT_GEAR_RATIO = 12.0 / 68.0;
    protected final double LIFT_GEAR_CIRC = Math.PI * 1.273; // diameter 1.273 inches

    protected CANSparkMax motor;
    protected RelativeEncoder encoder;

    protected double output;

    private PIDController controller;
    private LiftySetpoint setpoint = LiftySetpoint.DOWN;

    protected BooleanSupplier usePower;
    protected DoubleSupplier powerSupplier;

    public AbstractLift(int motorPort, BooleanSupplier usePower, DoubleSupplier powerSupplier) {
        this.motor = new CANSparkMax(motorPort, MotorType.kBrushless);

        this.motor.restoreFactoryDefaults();

        this.motor.setIdleMode(IdleMode.kBrake);

        this.encoder = this.motor.getEncoder();

        this.encoder.setPosition(0.0);

        this.usePower = usePower;
        this.powerSupplier = powerSupplier;

        controller = new PIDController(ClimberProfileConfig.liftP, ClimberProfileConfig.liftI,
                ClimberProfileConfig.liftD);

        controller.setTolerance(ClimberProfileConfig.liftTolerableError, ClimberProfileConfig.liftTolerableEndVelocity);

        Shuffleboard.getTab("Climber").add(this.controller);
    }

    @Override
    public void periodic() {
        if (usePower.getAsBoolean()) {
            this.useOutput(this.powerSupplier.getAsDouble());
        } else {
            this.useOutput(controller.calculate(getLiftDistance(), setpoint.getDistance()));
        }
    }

    protected abstract void useOutput(double output);

    protected abstract double getEncoderValue();

    public void setOutput(double output) {
        this.output = output;
    }

    public double getOutput() {
        return output;
    }

    @Override
    public void simulationPeriodic() {
        this.periodic();
    }

    public void setSetpoint(LiftySetpoint setpoint) {
        this.setpoint = setpoint;
    }

    /**
     * Converts total angular displacement of lift arm motor to linear displacement
     * of arm.
     * 
     * @return Inches lift arm has traveled
     */
    public double getLiftDistance() {
        // encoder position 1:1 with rotations
        return this.getEncoderValue() * LIFT_GEAR_RATIO * LIFT_GEAR_CIRC;
    }
}
