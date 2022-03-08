package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Utils;
import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private CANSparkMax liftLead, liftFollow;
    private TalonSRX rotateLead, rotateFollow;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private double liftSpeed, rotateSpeed;
    private double liftDistance, rotateAngle;
    private double liftAbsolute, rotateAbsolute; // while the commands change the ones above these never change

    private boolean enabled = false;

    /** Creates a new Climber. */
    public Climber() {
        this.liftLead = new CANSparkMax(ClimberConfig.liftLeadPort, MotorType.kBrushless);
        this.liftFollow = new CANSparkMax(ClimberConfig.liftFollowPort, MotorType.kBrushless);
        this.rotateLead = new TalonSRX(ClimberConfig.rotateLeadPort);
        this.rotateFollow = new TalonSRX(ClimberConfig.rotateFollowPort);

        this.liftSpeed = 0;
        this.rotateSpeed = 0;
        this.liftDistance = 0;
        this.rotateAngle = 0;
        this.liftAbsolute = 0;
        this.rotateAbsolute = 0;
        
        liftFollow.follow(liftLead);
        rotateFollow.follow(rotateLead);
    }

    public void enable() {setEnabled(true);}
    public void disable() {setEnabled(false);}
    public void toggle() {this.setEnabled(!this.enabled);}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

    @Override
    public void periodic() {
        if (enabled) {return;}
        
        if (liftAbsolute <= ClimberConfig.minLiftHeight && liftSpeed < 0) {liftSpeed = 0;}
        if (liftAbsolute >= ClimberConfig.maxLiftHeight && liftSpeed > 0) {liftSpeed = 0;}
        liftLead.set(liftSpeed);
        liftDistance += liftSpeed * ClimberConfig.maxLiftMotorSpeed / 3000;
        liftAbsolute += liftSpeed * ClimberConfig.maxLiftMotorSpeed / 3000;

        if (rotateAbsolute <= ClimberConfig.minRotateAngle && rotateSpeed < 0) {rotateSpeed = 0;}
        if (rotateAbsolute >= ClimberConfig.maxRotateAngle && rotateSpeed > 0) {rotateSpeed = 0;}
        double rotateTicks = Utils.calculateTicksPerDs(rotateSpeed, feedbackDevice);
        rotateLead.set(ControlMode.Velocity, rotateTicks);
        rotateAngle += rotateSpeed * 360 / 3000;
        rotateAbsolute += rotateSpeed * 360 / 3000;
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        this.periodic();
    }

    /** Moves lift arms up or down, thus moving the robot down or up, or stop the lift motors.
     * @param speed The fraction of the max speed to move at between -1 and 1.
     */
    public void setLiftSpeed(double speed) {liftSpeed = speed;}

    /** Rotates rotate arms, thus turning the robot about the attached motors, or stop the lift motors.
     * @param kV The factor applied to the default speed.
     */
    public void setRotateSpeed(double kV) {rotateSpeed = kV * ClimberConfig.defaultRotateMotorSpeed;}

    /** Returns the distance the lift arms have moved from its starting position. */
    public double getLiftDistance() {return this.liftDistance;}

    /** Returns the angle the rotate arms have rotated from their starting position. */
    public double getRotateAngle() {return this.rotateAngle;}

    /** Resets the counters for liftDistance and rotateAngle. */
    public void reset() {
        rotateAngle = 0;
        liftDistance = 0;
    }
}