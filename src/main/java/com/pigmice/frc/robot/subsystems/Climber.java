package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Utils;
import com.pigmice.frc.robot.Constants.ClimberConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private boolean enabled = false;
    
    private TalonSRX liftLead;
    private TalonSRX liftFollow;
    private TalonSRX rotateLead;
    private TalonSRX rotateFollow;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private double liftSpeed;
    private double rotateSpeed;

    /** Creates a new Climber. */
    public Climber() {
        this.liftLead = new TalonSRX(ClimberConfig.liftLeadPort);
        this.liftFollow = new TalonSRX(ClimberConfig.liftFollowPort);
        this.rotateLead = new TalonSRX(ClimberConfig.rotateLeadPort);
        this.rotateFollow = new TalonSRX(ClimberConfig.rotateFollowPort);

        this.liftSpeed = 0;
        this.rotateSpeed = 0;
    }

    public void on() {
        this.enabled = true;
    }

    public void off() {
        this.enabled = false;
        liftLead.set(ControlMode.Velocity, 0);
        rotateLead.set(ControlMode.Velocity, 0);
    }

    public void toggleEnabled() {
        this.enabled = enabled ? false : true;
    }

    @Override
    public void periodic() {
        if (!enabled) return;

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

    public void liftForward() {liftSpeed = ClimberConfig.liftMotorSpeed;} // lift arms up or robot down
    public void liftReverse() {liftSpeed = -ClimberConfig.liftMotorSpeed;} // lift arms down or robot up
    public void liftOff() {liftSpeed = 0;}
    public void rotateForward() {rotateSpeed = ClimberConfig.rotateMotorSpeed;} // rotate arms forwards or robot backwards
    public void rotateReverse() {rotateSpeed = -ClimberConfig.rotateMotorSpeed;} // rotate arms backwards or robot forwards
    public void rotateOff() {rotateSpeed = 0;}
}