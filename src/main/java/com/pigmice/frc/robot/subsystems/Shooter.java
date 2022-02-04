package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ShooterConfig;
import com.pigmice.frc.robot.RPMPController;
import com.pigmice.frc.robot.Utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private boolean enabled = true;

    private TalonSRX topMotor;
    private TalonSRX botMotor;

    private final double SHOOTER_KP = .005D;

    private final double SHOOTER_KS = 0;
    private final double SHOOTER_KV = .5;

    private final RPMPController topController = new RPMPController(SHOOTER_KP, 0.25);
    private final RPMPController botController = new RPMPController(SHOOTER_KP, 0.25);

    private final ShuffleboardTab shooterTab;

    private final NetworkTableEntry topRPMEntry;
    private final NetworkTableEntry bottomRPMEntry;

    private final NetworkTableEntry actualTopRPM;
    private final NetworkTableEntry actualBottomRPM;

    private final NetworkTableEntry topCalculated;
    private final NetworkTableEntry botCalculated;

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

        topMotor.setInverted(true);

        topMotor.configSelectedFeedbackSensor(feedbackDevice);
        botMotor.configSelectedFeedbackSensor(feedbackDevice);

        topMotor.setSensorPhase(true);
        botMotor.setSensorPhase(true);

        topMotor.setNeutralMode(NeutralMode.Coast);
        botMotor.setNeutralMode(NeutralMode.Coast);

        this.shooterTab = Shuffleboard.getTab("Shooter");

        this.topRPMEntry = shooterTab.add("Top RPM", 1).getEntry();
        this.bottomRPMEntry = shooterTab.add("Bottom RPM", 1).getEntry();

        this.actualTopRPM = shooterTab.add("Actual Top RPM", 1).getEntry();
        this.actualBottomRPM = shooterTab.add("Actual Bottom RPM", 1).getEntry();

        topCalculated = shooterTab.add("Top Calculated Velocity", 1).getEntry();
        botCalculated = shooterTab.add("Bottom Calculated Velocity", 1).getEntry();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            this.stopMotors();
        }
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

        this.setTargetSpeeds(topRPM, botRPM);

        double topVelocity = topMotor.getSelectedSensorVelocity();
        double botVelocity = botMotor.getSelectedSensorVelocity();

        SmartDashboard.putNumber("Top Raw Velocity", topVelocity);
        SmartDashboard.putNumber("Bot Raw Velocity", botVelocity);

        double topActualRPM = Utils.calculateRPM(topVelocity, feedbackDevice);
        double botActualRPM = Utils.calculateRPM(botVelocity, feedbackDevice);

        double topTarget = topController.update(topActualRPM);
        double botTarget = botController.update(botActualRPM);

        this.actualTopRPM.setDouble(topActualRPM);
        this.actualBottomRPM.setDouble(botActualRPM);

        topCalculated.setDouble(topTarget);
        botCalculated.setDouble(botTarget);

        topMotor.set(ControlMode.PercentOutput, topTarget);
        botMotor.set(ControlMode.PercentOutput, botTarget);
    }

    /**
     * Sets the target RPMs of the top and bottom shooter motors.
     * 
     * @param top    Target RPM of top motor
     * @param bottom Target RPM of bottom motor
     */
    public void setTargetSpeeds(double top, double bottom) {
        this.topController.setTargetRPM(top);
        this.botController.setTargetRPM(bottom);
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
        this.topController.setTargetRPM(0);
        this.botController.setTargetRPM(0);
    }

    public boolean isAtTargetVelocity() {
        return this.topMotor.getClosedLoopError() <= VELOCITY_THRESHOLD
                && this.botMotor.getClosedLoopError() <= VELOCITY_THRESHOLD;
    }

    private double calculate(double velocity) {
        return SHOOTER_KS * Math.signum(velocity) + SHOOTER_KV * velocity;
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }
}