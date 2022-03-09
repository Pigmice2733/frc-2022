package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.Utils;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private CANSparkMax liftLead, liftFollow;
    private TalonSRX rotateLead, rotateFollow;

    private RelativeEncoder liftEncoder;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private final double LIFT_GEAR_RATIO = 12.0 / 68.0;
    private final double LIFT_GEAR_CIRC = Math.PI * 1.273; // diameter 1.273 inches

    private double liftSpeed, rotateSpeed;
    private double liftDistance, rotateAngle;
    private double liftAbsolute, rotateAbsolute; // while the commands change the ones above these never change

    private ShuffleboardTab climberTab;

    private boolean enabled = true;

    private double initialPosition = 0.0;

    /** Creates a new Climber. */
    public Climber() {
        this.liftLead = new CANSparkMax(ClimberConfig.liftLeadPort, MotorType.kBrushless);
        this.liftFollow = new CANSparkMax(ClimberConfig.liftFollowPort, MotorType.kBrushless);
        this.rotateLead = new TalonSRX(ClimberConfig.rotateLeadPort);
        this.rotateFollow = new TalonSRX(ClimberConfig.rotateFollowPort);

        this.liftLead.restoreFactoryDefaults();

        this.liftLead.setInverted(true);

        liftFollow.follow(liftLead);
        rotateFollow.follow(rotateLead);

        this.liftEncoder = this.liftLead.getEncoder();

        this.liftSpeed = 0;
        this.rotateSpeed = 0;

        this.liftDistance = 0;
        this.rotateAngle = 0;

        this.liftAbsolute = 0;
        this.rotateAbsolute = 0;

        this.climberTab = Shuffleboard.getTab("Climber");
        climberTab.addNumber("Encoder Position", this.liftEncoder::getPosition);

        this.zeroEncoders();
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

    public void zeroEncoders() {
        this.liftEncoder.setPosition(0);
    }

    @Override
    public void periodic() {
        if (!enabled) {
            return;
        }

        liftLead.set(liftSpeed);

        rotateLead.set(ControlMode.PercentOutput, rotateSpeed);
    }

    public void setLiftOutput(double output) {
        this.liftSpeed = output;
    }

    public void setRotateOutput(double output) {
        this.rotateSpeed = output;
    }

    @Override
    public void simulationPeriodic() {
        this.periodic();
    }

    /**
     * Moves lift arms up or down, thus moving the robot down or up, or stop the
     * lift motors.
     * 
     * @param kV The factor applied to the default speed.
     */
    public void setLiftSpeed(double kV) {
        liftSpeed = kV * ClimberConfig.defaultLiftMotorSpeed;
    }

    public void setRotateSpeed(double kV) {
        rotateSpeed = kV * ClimberConfig.defaultRotateMotorSpeed;
    }

    /** Returns the distance the lift arms have moved from its starting position. */
    public double getLiftDistance() {
        // TODO position to inch conversion is position * gear ratio * circumference
        // = position * (12 / 68.0) * Math.PI * 1.273
        return this.liftEncoder.getPosition();
    }

    /**
     * Returns the angle the rotate arms have rotated from their starting position.
     */
    public double getRotateAngle() {
        return this.rotateAngle;
    }

    /** Resets the counters for liftDistanRce and rotateAngle. */
    public void reset() {
        this.zeroEncoders();
        rotateAngle = 0;
        liftDistance = 0;
    }
}