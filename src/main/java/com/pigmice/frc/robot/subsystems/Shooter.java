package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants;
import com.pigmice.frc.robot.Constants.ShooterConfig;
import com.pigmice.frc.robot.Utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private boolean enabled = true;

    private TalonSRX topShooterMotor;
    private TalonSRX bottomShooterMotor;

    private final double SHOOTER_KP = .04D;

    private final double SHOOTER_KS = 0;
    private final double SHOOTER_KV = .5;

    private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(SHOOTER_KS, SHOOTER_KV);

    private final ShuffleboardTab shooterTab;

    private final NetworkTableEntry topRPMEntry;
    private final NetworkTableEntry bottomRPMEntry;

    private final NetworkTableEntry actualTopRPM;
    private final NetworkTableEntry actualBottomRPM;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private static final double RPM_NOT_SET = -1;

    private double topTargetRPM = RPM_NOT_SET;
    private double botTargetRPM = RPM_NOT_SET;

    // tune this
    private static final double VELOCITY_THRESHOLD = 10;

    // Create a new Shooter
    public Shooter() {
        topShooterMotor = new TalonSRX(Constants.ShooterConfig.topMotorPort);
        bottomShooterMotor = new TalonSRX(Constants.ShooterConfig.bottomMotorPort);

        topShooterMotor.configSelectedFeedbackSensor(feedbackDevice);
        bottomShooterMotor.configSelectedFeedbackSensor(feedbackDevice);

        topShooterMotor.setSensorPhase(true);
        bottomShooterMotor.setSensorPhase(true);

        topShooterMotor.config_kP(0, SHOOTER_KP);
        topShooterMotor.config_kI(0, 0);
        topShooterMotor.config_kD(0, 0);

        bottomShooterMotor.config_kP(0, SHOOTER_KP);
        bottomShooterMotor.config_kI(0, 0);
        bottomShooterMotor.config_kD(0, 0);

        this.shooterTab = Shuffleboard.getTab("Shooter");

        this.topRPMEntry = shooterTab.add("Top RPM", 1).getEntry();
        this.bottomRPMEntry = shooterTab.add("Bottom RPM", 1).getEntry();

        this.actualTopRPM = shooterTab.add("Actual Top RPM", 1).getEntry();
        this.actualBottomRPM = shooterTab.add("Actual Bottom RPM", 1).getEntry();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleEnabled() {
        setEnabled(!enabled);
    }

    @Override
    public void periodic() {
        if (!enabled)
            return;

        double topRPM = this.topTargetRPM == RPM_NOT_SET ? this.topRPMEntry.getDouble(ShooterConfig.topMotorSpeed)
                : this.topTargetRPM;
        double botRPM = this.botTargetRPM == RPM_NOT_SET ? this.bottomRPMEntry.getDouble(ShooterConfig.bottomMotorSpeed)
                : this.botTargetRPM;

        double topTicksPerDs = Utils.calculateTicksPerDs(topRPM, feedbackDevice);
        double botTicksPerDs = Utils.calculateTicksPerDs(botRPM, feedbackDevice);

        double topVelocity = topShooterMotor.getSelectedSensorVelocity();
        double botVelocity = bottomShooterMotor.getSelectedSensorVelocity();

        double topRPS = topRPM / 60;
        double botRPS = botRPM / 60;

        double topFFVolts = feedforward.calculate(topRPS);
        double botFFVolts = feedforward.calculate(botRPS);

        double topFFNormalized = topFFVolts / 12;
        double botFFNormalized = botFFVolts / 12;

        this.actualTopRPM.setDouble(Utils.calculateRPM(topVelocity, feedbackDevice));
        this.actualBottomRPM.setDouble(Utils.calculateRPM(botVelocity, feedbackDevice));

        topShooterMotor.set(ControlMode.Velocity, topTicksPerDs, DemandType.ArbitraryFeedForward, topFFNormalized);
        bottomShooterMotor.set(ControlMode.Velocity, botTicksPerDs, DemandType.ArbitraryFeedForward,
                botFFNormalized);
    }

    /**
     * Sets the target RPMs of the top and bottom shooter motors.
     * 
     * @param top    Target RPM of top motor
     * @param bottom Target RPM of bottom motor
     */
    public void setTargetSpeeds(double top, double bottom) {
        this.topTargetRPM = top;
        this.botTargetRPM = bottom;
    }

    /**
     * Unsets motor target RPMs so they use the ones set in Shuffleboard.
     */
    public void useShuffleboardSpeeds() {
        this.topTargetRPM = this.botTargetRPM = RPM_NOT_SET;
    }

    public void stopMotors() {
        this.topTargetRPM = this.botTargetRPM = 0;
    }

    public boolean isAtTargetVelocity() {
        return this.topShooterMotor.getClosedLoopError() <= VELOCITY_THRESHOLD
                && this.bottomShooterMotor.getClosedLoopError() <= VELOCITY_THRESHOLD;
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }
}