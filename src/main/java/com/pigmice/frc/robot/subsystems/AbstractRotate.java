package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class AbstractRotate extends SubsystemBase {
    protected final double ROTATE_GEAR_RATIO = 1.0 / 2.0;

    protected TalonSRX motor;

    protected double output = 0.0;

    public AbstractRotate(int motorPort, boolean sensorPhase) {
        this.motor = new TalonSRX(motorPort);

        this.motor.configFactoryDefault();

        this.motor.setNeutralMode(NeutralMode.Brake);

        this.motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        this.motor.setSensorPhase(sensorPhase);

        this.motor.setSelectedSensorPosition(0.0);
    }

    @Override
    public void periodic() {
        this.useOutput(this.output);
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
    public double getRotateAngle() {
        return (this.getEncoderValue() / 4096.0)
                * ROTATE_GEAR_RATIO * 360.0;
    }
}
