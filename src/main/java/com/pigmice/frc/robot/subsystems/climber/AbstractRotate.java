package com.pigmice.frc.robot.subsystems.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ClimberConfig.RotatoSetpoint;
import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.subsystems.Subsystem;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

public abstract class AbstractRotate extends Subsystem {
    protected final double ROTATE_GEAR_RATIO = 1.0 / 2.0;

    protected TalonSRX motor;

    protected double output = 0.0;

    private PIDController controller;
    private RotatoSetpoint setpoint = RotatoSetpoint.BACK;

    protected BooleanSupplier usePower;
    protected DoubleSupplier powerSupplier;

    public AbstractRotate(int motorPort, boolean sensorPhase, BooleanSupplier usePower, DoubleSupplier powerSupplier) {
        this.usePower = usePower;
        this.powerSupplier = powerSupplier;

        this.motor = new TalonSRX(motorPort);

        this.motor.configFactoryDefault();

        this.motor.setNeutralMode(NeutralMode.Brake);

        this.motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        this.motor.setSensorPhase(sensorPhase);

        this.motor.setSelectedSensorPosition(0.0);

        controller = new PIDController(ClimberProfileConfig.rotateP, ClimberProfileConfig.rotateI,
                ClimberProfileConfig.rotateD);

        controller.setTolerance(ClimberProfileConfig.angleTolerableError,
                ClimberProfileConfig.angleTolerableEndVelocity);

        Shuffleboard.getTab("Climber").add(this.controller);
    }

    @Override
    public void periodic() {
        if (this.isTestMode()) {
            this.useOutput(0.0);
            return;
        }
        if (usePower.getAsBoolean()) {
            double power = this.powerSupplier.getAsDouble();
            if ((power > 0 && this.getRotateAngle() < ClimberConfig.maxRotateAngle)
                    || (power < 0 && this.getRotateAngle() > ClimberConfig.minRotateAngle)) {
                this.useOutput(this.powerSupplier.getAsDouble());
            } else {
                this.useOutput(0.0);
            }
        } else {
            this.useOutput(controller.calculate(getRotateAngle(), setpoint.getAngle()));
        }
    }

    @Override
    public void nonTestInit() {
        this.motor.setSelectedSensorPosition(0.0);
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

    public void setSetpoint(RotatoSetpoint setpoint) {
        this.setpoint = setpoint;
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

    public void testPeriodic() {
        this.powerSupplier = () -> 0.0;
        this.usePower = () -> true;
        this.periodic();
    }
}
