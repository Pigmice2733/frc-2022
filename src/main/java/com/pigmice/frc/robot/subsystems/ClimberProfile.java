package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.Constants.ClimberProfileConfig;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.pigmice.frc.robot.Utils;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileSubsystem;

// https://docs.wpilib.org/en/stable/docs/software/commandbased/profile-subsystems-commands.html
// DO NOT USE THIS CLASS until and unless it is necessary, for now continue using Climber

public class ClimberProfile extends TrapezoidProfileSubsystem {
    private CANSparkMax liftLead, liftFollow;
    private TalonSRX rotateLead, rotateFollow;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private double liftSpeed;
    private double rotateSpeed;

    private boolean enabled = false;

    
    public ClimberProfile() {
        super(new TrapezoidProfile.Constraints(ClimberProfileConfig.maxLiftVelocity, ClimberProfileConfig.maxLiftAcceleration));

        this.liftLead = new CANSparkMax(ClimberConfig.liftLeadPort, MotorType.kBrushless);
        this.liftFollow = new CANSparkMax(ClimberConfig.liftFollowPort, MotorType.kBrushless);
        this.rotateLead = new TalonSRX(ClimberConfig.rotateLeadPort);
        this.rotateFollow = new TalonSRX(ClimberConfig.rotateFollowPort);

        this.liftSpeed = 0;
        this.rotateSpeed = 0;
        
        liftFollow.follow(liftLead);
        rotateFollow.follow(rotateLead);
    }

    public void enable() {setEnabled(true);}
    public void disable() {setEnabled(false);}
    public void toggle() {this.setEnabled(!this.enabled);}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

    public void useState(TrapezoidProfile.State currentState) {
        if (!enabled) {return;}
        
        double rotateTicks = Utils.calculateTicksPerDs(rotateSpeed, feedbackDevice);

        liftLead.set(liftSpeed);
        rotateLead.set(ControlMode.Velocity, rotateTicks);
    }

    /** Moves lift arms up or down, thus moving the robot down or up, or stop the lift motors.
     * @param speed The fraction of the max speed to move at; a number between -1 and 1.
     */
    public void setLiftSpeed(double speed) {liftSpeed = speed;}

    /** Rotates rotate arms, thus turning the robot about the attached motors, or stop the lift motors.
     * @param kV The factor applied to the default speed.
     */
    public void setRotateSpeed(double kV) {rotateSpeed = kV * ClimberConfig.defaultRotateMotorSpeed;}
}