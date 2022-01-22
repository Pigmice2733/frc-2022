package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MotorPID extends SubsystemBase {
    private double kP, tI, tD;
    private double kI = kP / tI;
    private double kD = kP * tD;
    private double dt = 0.02;
    private double target;
    private double prevError = 0;
    private double error = 0;
    private double integral = 0;
    private double output;
    private double outScale;

    public TalonSRX motor;

    public MotorPID(double kP, double tI, double tD, double target, double os) {
        this.kP = kP;
        this.tI = tI;
        this.tD = tD;
        this.target = target;
        outScale = os;

        motor = new TalonSRX(7);
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        motor.setSensorPhase(true);
    }

    @Override
    public void periodic() {
        error = target - motor.getSelectedSensorVelocity();
        integral += error * dt;
        output = (kP * error) + (kI * integral) + (kD * (error - prevError) / dt);
        prevError = error;
        motor.set(ControlMode.PercentOutput, output * outScale);
    }
}