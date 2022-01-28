package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class RandomMotor extends SubsystemBase {
    private final TalonSRX motor;
    public double targetVelocity = 10;

    private final double gearRatio = 5d;
    private final double sensorUnitsPerRotation = 4096;

    /** Creates a new random motor. */
    public RandomMotor() {
        motor = new TalonSRX(7);
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        motor.setSensorPhase(true);
    }

    private double calculateRPM(double raw, double output) {
        return ((raw * 600) / sensorUnitsPerRotation);
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public void setTargetVelocity(double targetVelocity) {
        this.targetVelocity = targetVelocity;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        double output = 0.3;
        motor.set(ControlMode.PercentOutput, output);
        double velocity = motor.getSelectedSensorVelocity(); // can only be run once per tick?
        SmartDashboard.putNumber("Motor Velocity (rpm)", calculateRPM(velocity, output));
        SmartDashboard.putNumber("Raw Velocity", velocity);
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}