package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

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

    private final double ROTATE_GEAR_RATIO = 1.0 / 2.0;

    private double liftOutput, rotateOutput;

    private ShuffleboardTab climberTab;

    private boolean enabled = true;

    /** Creates a new Climber. */
    public Climber() {
        this.liftLead = new CANSparkMax(ClimberConfig.liftLeadPort, MotorType.kBrushless);
        this.liftFollow = new CANSparkMax(ClimberConfig.liftFollowPort, MotorType.kBrushless);
        this.rotateLead = new TalonSRX(ClimberConfig.rotateLeadPort);
        this.rotateFollow = new TalonSRX(ClimberConfig.rotateFollowPort);

        this.liftLead.restoreFactoryDefaults();
        this.liftFollow.restoreFactoryDefaults();
        this.rotateLead.configFactoryDefault();
        this.rotateFollow.configFactoryDefault();

        this.liftFollow.setInverted(true);
        this.rotateLead.setInverted(true);

        this.liftLead.setIdleMode(IdleMode.kBrake);
        this.liftFollow.setIdleMode(IdleMode.kBrake);
        this.rotateLead.setNeutralMode(NeutralMode.Brake);
        this.rotateFollow.setNeutralMode(NeutralMode.Brake);

        // liftFollow.follow(liftLead);
        rotateFollow.follow(rotateLead);

        this.liftEncoder = this.liftLead.getEncoder();
        this.rotateLead.configSelectedFeedbackSensor(feedbackDevice);

        this.rotateLead.setSensorPhase(true);

        this.climberTab = Shuffleboard.getTab("Climber");
        climberTab.addNumber("Encoder Position", this::getRotateAngle);
        climberTab.addNumber("Lift Distance", this::getLiftDistance);

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
        this.rotateLead.setSelectedSensorPosition(0.0);
    }

    @Override
    public void periodic() {
        if (!enabled) {
            return;
        }

        liftLead.set(liftOutput);
        liftFollow.set(liftOutput);

        rotateLead.set(ControlMode.PercentOutput, rotateOutput);
    }

    public void setLiftOutput(double output) {
        this.liftOutput = output;
    }

    public void setRotateOutput(double output) {
        this.rotateOutput = output;
    }

    @Override
    public void simulationPeriodic() {
        this.periodic();
    }

    /**
     * Converts total angular displacement of lift arm motor to linear displacement
     * of arm.
     * 
     * @return Inches lift arm has traveled
     */
    public double getLiftDistance() {
        // encoder position 1:1 with rotations
        return this.liftEncoder.getPosition() * LIFT_GEAR_RATIO * LIFT_GEAR_CIRC;
    }

    /**
     * Converts arm encoder position to angle in degrees.
     * 
     * @return Degrees the arms have rotated
     */
    public double getRotateAngle() {
        // 4096 sensor ticks in one rotation for Talon SRX encoder
        return (this.rotateLead.getSelectedSensorPosition() / 4096.0) * ROTATE_GEAR_RATIO * 360.0;
    }
}