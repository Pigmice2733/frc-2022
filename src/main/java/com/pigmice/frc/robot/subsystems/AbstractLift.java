package com.pigmice.frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class AbstractLift extends SubsystemBase {
    protected final double LIFT_GEAR_RATIO = 12.0 / 68.0;
    protected final double LIFT_GEAR_CIRC = Math.PI * 1.273; // diameter 1.273 inches

    protected CANSparkMax motor;
    protected RelativeEncoder encoder;

    protected double output;

    public AbstractLift(int motorPort) {
        this.motor = new CANSparkMax(motorPort, MotorType.kBrushless);

        this.motor.restoreFactoryDefaults();

        this.motor.setIdleMode(IdleMode.kBrake);

        this.encoder = this.motor.getEncoder();

        this.encoder.setPosition(0.0);
    }

    @Override
    public void periodic() {
        this.useOutput(output);
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
