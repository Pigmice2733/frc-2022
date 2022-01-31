package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ShooterConfig;
import com.pigmice.frc.robot.Utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private boolean enabled = true;

    private TalonSRX topMotor;
    private TalonSRX botMotor;

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

    private static final double MAX_RPM_775 = 18700;
    private static final double MAX_RPS_775 = MAX_RPM_775 / 60;

    // tune this
    private static final double VELOCITY_THRESHOLD = 10;

    // Create a new Shooter
    public Shooter() {
        this.topMotor = new TalonSRX(ShooterConfig.topMotorPort);
        this.botMotor = new TalonSRX(ShooterConfig.bottomMotorPort);

        topMotor.configSelectedFeedbackSensor(feedbackDevice);
        botMotor.configSelectedFeedbackSensor(feedbackDevice);

        topMotor.setSensorPhase(true);
        botMotor.setSensorPhase(true);

        topMotor.config_kP(0, SHOOTER_KP);
        topMotor.config_kI(0, 0);
        topMotor.config_kD(0, 0);

        botMotor.config_kP(0, SHOOTER_KP);
        botMotor.config_kI(0, 0);
        botMotor.config_kD(0, 0);

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

        double topVelocity = topMotor.getSelectedSensorVelocity();
        double botVelocity = botMotor.getSelectedSensorVelocity();

        double topRPS = topRPM / 60;
        double botRPS = botRPM / 60;

        double topFFRPS = feedforward.calculate(topRPS);
        double botFFRPS = feedforward.calculate(botRPS);

        double topFFNormalized = topFFRPS / MAX_RPS_775;
        double botFFNormalized = botFFRPS / MAX_RPS_775;

        this.actualTopRPM.setDouble(Utils.calculateRPM(topVelocity, feedbackDevice));
        this.actualBottomRPM.setDouble(Utils.calculateRPM(botVelocity, feedbackDevice));

        topMotor.set(ControlMode.Velocity, topTicksPerDs, DemandType.ArbitraryFeedForward, topFFNormalized);
        botMotor.set(ControlMode.Velocity, botTicksPerDs, DemandType.ArbitraryFeedForward, botFFNormalized);
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
        return this.topMotor.getClosedLoopError() <= VELOCITY_THRESHOLD
                && this.botMotor.getClosedLoopError() <= VELOCITY_THRESHOLD;
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }
}