package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.IntakeConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private TalonSRX motorRun, motorExtend;
    private boolean enabled, extended;
    private static double runSpeed;
    private byte runDirection; // 1 is forward, -1 is backward
    private double extendSpeed;
    private final double extendGearRatio = 0.5;

    /** Creates a new Intake. */
    public Intake() {
        motorRun = new TalonSRX(IntakeConfig.intakeBottomPort);
        motorRun.configFactoryDefault();

        motorExtend = new TalonSRX(IntakeConfig.intakeTopPort);
        motorExtend.configFactoryDefault();
        motorExtend.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        motorExtend.setSelectedSensorPosition(0.0);

        runSpeed = IntakeConfig.intakeSpeed;
        runDirection = 1;
        extendSpeed = 0.0;

        this.enabled = false;
        this.extended = false;
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void periodic() {
        if (!enabled)
            return;

        if (extended) {
            motorRun.set(ControlMode.PercentOutput, runSpeed * runDirection);
        } else {
            motorRun.set(ControlMode.PercentOutput, 0.0);
        }

        motorExtend.set(ControlMode.PercentOutput, extendSpeed);
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        this.periodic();
    }

    public double extendAngle() {
        return motorExtend.getSelectedSensorPosition() * extendGearRatio * 360 / 4096;
    }

    public void extend() {
        this.extended = true;
        this.runForward();
    }

    public void retract() {
        this.extended = false;
    }

    public void setExtendSpeed(double speed) {
        this.extendSpeed = speed;
    }

    public void runForward() {
        this.runDirection = 1;
    }

    public void runBackward() {
        this.runDirection = -1;
    }

    public void switchRunDirection() {
        runDirection *= -1;
    }
}