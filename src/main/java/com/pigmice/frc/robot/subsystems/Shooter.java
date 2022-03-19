package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.ShooterConfig;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterModes;
import com.pigmice.frc.robot.RPMPController;
import com.pigmice.frc.robot.Utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private boolean enabled = false;

    private TalonSRX topMotor, botMotor;

    private double shooterP, vThresh;

    private final RPMPController topController = new RPMPController(shooterP, 0.25);
    private final RPMPController botController = new RPMPController(shooterP, 0.25);

    private final ShuffleboardTab shooterTab;

    private final NetworkTableEntry topRPMEntry, bottomRPMEntry, actualTopRPM,
            actualBottomRPM, topCalculated, botCalculated, atTargetEntry;

    private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

    private static final double RPM_NOT_SET = -1;

    private double topTargetRPM = RPM_NOT_SET;
    private double botTargetRPM = RPM_NOT_SET;

    // private static final double MAX_RPM_775 = 18700;
    // private static final double MAX_RPS_775 = MAX_RPM_775 / 60;

    private boolean atTarget = false;

    private ShooterModes mode;

    // Create a new Shooter
    public Shooter() {
        this.topMotor = new TalonSRX(ShooterConfig.topMotorPort);
        this.botMotor = new TalonSRX(ShooterConfig.bottomMotorPort);

        topMotor.setInverted(true);

        topMotor.configSelectedFeedbackSensor(feedbackDevice);
        botMotor.configSelectedFeedbackSensor(feedbackDevice);

        topMotor.setSensorPhase(true);
        botMotor.setSensorPhase(true);

        // topMotor.setNeutralMode(NeutralMode.Coast);
        // botMotor.setNeutralMode(NeutralMode.Coast);

        this.shooterP = ShooterConfig.shooterP;
        this.vThresh = ShooterConfig.velocityThreshold;

        this.shooterTab = Shuffleboard.getTab("Shooter");

        this.topRPMEntry = shooterTab.add("Top RPM", 1).getEntry();
        this.bottomRPMEntry = shooterTab.add("Bottom RPM", 1).getEntry();

        this.actualTopRPM = shooterTab.add("Actual Top RPM", 1).getEntry();
        this.actualBottomRPM = shooterTab.add("Actual Bottom RPM", 1).getEntry();

        this.topCalculated = shooterTab.add("Top Calculated Velocity", 1).getEntry();
        this.botCalculated = shooterTab.add("Bottom Calculated Velocity", 1).getEntry();

        this.atTargetEntry = shooterTab.add("At Target", this.atTarget).getEntry();

        this.mode = ShooterModes.AUTO;
    }

    public void enable() {setEnabled(true);}
    public void disable() {setEnabled(false);}
    public void toggle() {this.setEnabled(!this.enabled);}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

    @Override
    public void periodic() {
        if (!enabled) {
            this.setTargetSpeeds(0, 0);
        } else {
            if (mode == ShooterModes.AUTO) {
                // calculate speeds based on distance
            } else {
                this.setTargetSpeeds(mode.getTopRPM(), mode.getBottomRPM());
            }
        }

        double topRPM = this.topTargetRPM == RPM_NOT_SET ? this.topRPMEntry.getDouble(ShooterConfig.topMotorSpeed)
                : this.topTargetRPM;
        double botRPM = this.botTargetRPM == RPM_NOT_SET ? this.bottomRPMEntry.getDouble(ShooterConfig.bottomMotorSpeed)
                : this.botTargetRPM;

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

        this.atTarget = Math.abs(topRPM - topActualRPM) <= vThresh
                && Math.abs(botRPM - botActualRPM) <= vThresh;

        this.atTargetEntry.setBoolean(this.atTarget);

        topMotor.set(ControlMode.PercentOutput, topTarget);
        botMotor.set(ControlMode.PercentOutput, botTarget);
    }

    @Override
    public void simulationPeriodic() {
        this.periodic();
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
        this.atTarget = false;
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
        this.atTarget = false; // explain why this is necessary
    }

    public boolean isAtTargetVelocity() {
        return topTargetRPM != 0 && botTargetRPM != 0 && this.atTarget;
    }

    public ShooterModes getMode() {
        return mode;
    }

    public void setMode(ShooterModes mode) {
        this.mode = mode;
    }
}