package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Utils;
import com.pigmice.frc.robot.Constants.ClimberConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {    
    private TalonSRX liftLead;
    private TalonSRX liftFollow;
    private TalonSRX rotateLead;
    private TalonSRX rotateFollow;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private double liftSpeed;
    private double rotateSpeed;

    private boolean enabled = false;

    /** Creates a new Climber. */
    public Climber() {
        this.liftLead = new TalonSRX(ClimberConfig.liftLeadPort);
        this.liftFollow = new TalonSRX(ClimberConfig.liftFollowPort);
        this.rotateLead = new TalonSRX(ClimberConfig.rotateLeadPort);
        this.rotateFollow = new TalonSRX(ClimberConfig.rotateFollowPort);

        this.liftSpeed = 0;
        this.rotateSpeed = 0;
    }

    public void enable() {setEnabled(true);}
    public void disable() {setEnabled(false);}
    public void toggle() {this.setEnabled(!this.enabled);}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

    @Override
    public void periodic() {
        liftFollow.follow(liftLead);
        rotateFollow.follow(rotateLead);

        double liftTicks = Utils.calculateTicksPerDs(liftSpeed, feedbackDevice);
        double rotateTicks = Utils.calculateTicksPerDs(rotateSpeed, feedbackDevice);

        liftLead.set(ControlMode.Velocity, liftTicks);
        rotateLead.set(ControlMode.Velocity, rotateTicks);
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        this.periodic();
    }

    /** Moves lift arms up or down, thus moving the robot down or up, or stop the lift motors.
     * @param kV The factor applied to the default speed.
     */
    public void setLiftSpeed(double kV) {liftSpeed = kV * ClimberConfig.defaultLiftMotorSpeed;}

    /** Rotates rotate arms, thus turning the robot about the attached motors, or stop the lift motors.
     * @param kV The factor applied to the default speed.
     */
    public void setRotateSpeed(double kV) {rotateSpeed = kV * ClimberConfig.defaultRotateMotorSpeed;}
}