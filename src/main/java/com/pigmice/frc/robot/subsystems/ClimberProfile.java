package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.pigmice.frc.robot.Utils;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileSubsystem;

public class ClimberProfile extends TrapezoidProfileSubsystem {
    private TalonSRX liftLead;
    private TalonSRX liftFollow;
    private TalonSRX rotateLead;
    private TalonSRX rotateFollow;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private double liftSpeed;
    private double rotateSpeed;

    private boolean enabled = false;
    
    public ClimberProfile() {
        super(new TrapezoidProfile.Constraints(ClimberProfileConfig.maxVelocity, ClimberProfileConfig.maxAcceleration));

        this.liftLead = new TalonSRX(ClimberConfig.liftLeadPort);
        this.liftFollow = new TalonSRX(ClimberConfig.liftFollowPort);
        this.rotateLead = new TalonSRX(ClimberConfig.rotateLeadPort);
        this.rotateFollow = new TalonSRX(ClimberConfig.rotateFollowPort);

        liftFollow.follow(liftLead);
        rotateFollow.follow(rotateLead);

        this.liftSpeed = 0;
        this.rotateSpeed = 0;
    }

    public void enable() {setEnabled(true);}
    public void disable() {setEnabled(false);}
    public void toggle() {this.setEnabled(!this.enabled);}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

    public void useState(TrapezoidProfile.State currentState) {
        if (enabled) return;
        
        double liftTicks = Utils.calculateTicksPerDs(liftSpeed, feedbackDevice);
        double rotateTicks = Utils.calculateTicksPerDs(rotateSpeed, feedbackDevice);

        liftLead.set(ControlMode.Velocity, liftTicks);
        rotateLead.set(ControlMode.Velocity, rotateTicks);
    }
}
