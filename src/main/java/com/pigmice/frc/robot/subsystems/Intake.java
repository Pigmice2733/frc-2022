package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.IntakeConfig;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final TalonSRX leftExtendMotor, rightExtendMotor;
    public final PIDController leftExtendPID, rightExtendPID;
    private final ArmFeedforward leftFeedForward, rightFeedForward;

    private final CANSparkMax intakeMotor;

    private boolean enabled, extended, backwards;
    private final double extendGearRatio = 1.0;

    private final ShuffleboardTab intakeTab;
    private final NetworkTableEntry enabledEntry;
    private final NetworkTableEntry leftMotorOutputEntry;
    private final NetworkTableEntry leftRotateAngleEntry;
    private final NetworkTableEntry rightMotorOutputEntry;
    private final NetworkTableEntry rightRotateAngleEntry;
    private final NetworkTableEntry leftAtSetpointEntry;
    private final NetworkTableEntry rightAtSetpointEntry;
    private final NetworkTableEntry leftSetpointEntry;
    private final NetworkTableEntry rightSetpointEntry;
    private final NetworkTableEntry intakeMotorOutputEntry;

    /** Creates a new Intake. */
    public Intake() {
        this.extended = true;
        // motorRun = new TalonSRX(IntakeConfig.intakeBottomPort);
        // motorRun.configFactoryDefault();

        // Left Extend Motor
        leftExtendMotor = new TalonSRX(IntakeConfig.extendLeftPort);
        leftExtendMotor.configFactoryDefault();
        leftExtendMotor.enableVoltageCompensation(true);
        leftExtendMotor.configVoltageCompSaturation(10);
        leftExtendMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        leftExtendMotor.setSelectedSensorPosition(0.0);
        leftExtendMotor.setInverted(true);
        leftExtendMotor.setSensorPhase(true);

        // Right Extend Motor
        rightExtendMotor = new TalonSRX(IntakeConfig.extendRightPort);
        rightExtendMotor.configFactoryDefault();
        rightExtendMotor.enableVoltageCompensation(true);
        rightExtendMotor.configVoltageCompSaturation(10);
        rightExtendMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        rightExtendMotor.setSelectedSensorPosition(0.0);

        // Left Extend PID
        leftExtendPID = new PIDController(IntakeConfig.extendP, IntakeConfig.extendI, IntakeConfig.extendD);
        leftExtendPID.setTolerance(IntakeConfig.extendTolError, IntakeConfig.extendTolEndVelo);
        // leftExtendPID.setSetpoint(0);

        // Right Extend PID
        rightExtendPID = new PIDController(IntakeConfig.extendP, IntakeConfig.extendI, IntakeConfig.extendD);
        rightExtendPID.setTolerance(IntakeConfig.extendTolError, IntakeConfig.extendTolEndVelo);
        rightExtendPID.setSetpoint(0);

        // Feed Forward
        leftFeedForward = new ArmFeedforward(IntakeConfig.extendS, IntakeConfig.extendG, IntakeConfig.extendV);
        rightFeedForward = new ArmFeedforward(IntakeConfig.extendS, IntakeConfig.extendG, IntakeConfig.extendV);

        this.enabled = false;
        this.extended = false;
        this.backwards = false;

        // Shuffleboard Entries
        this.intakeTab = Shuffleboard.getTab("Intake");
        this.enabledEntry = intakeTab.add("Enabled", enabled).getEntry();
        this.leftMotorOutputEntry = intakeTab.add("Left Motor Output", 0).getEntry();
        this.leftRotateAngleEntry = intakeTab.add("Left Rotate Angle", 0).getEntry();
        this.rightMotorOutputEntry = intakeTab.add("Right Motor Output", 0).getEntry();
        this.rightRotateAngleEntry = intakeTab.add("Right Rotate Angle", 0).getEntry();
        this.leftAtSetpointEntry = intakeTab.add("Left at Setpoint", false).getEntry();
        this.rightAtSetpointEntry = intakeTab.add("Right at Setpoint", false).getEntry();
        this.leftSetpointEntry = intakeTab.add("Left Setpoint", 0).getEntry();
        this.rightSetpointEntry = intakeTab.add("Right Setpoint", 0).getEntry();
        this.intakeMotorOutputEntry = intakeTab.add("Intake Motor Ouput", IntakeConfig.intakeMotorSpeed).getEntry();

        // Intake Motor
        this.intakeMotor = new CANSparkMax(IntakeConfig.intakeMotorPort, MotorType.kBrushless);
        intakeMotor.setInverted(true);
    }

    public void enable() {
        setExtendMotorOutputs(0, 0);
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
        setExtendMotorOutputs(0, 0);
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        enabledEntry.setBoolean(enabled);
        this.enabled = enabled;
    }

    @Override
    public void periodic() {
        // write to Shuffleboard
        this.leftRotateAngleEntry.setDouble(getLeftExtendAngle());
        this.rightRotateAngleEntry.setDouble(getRightExtendAngle());
        this.leftSetpointEntry.setDouble(leftExtendPID.getSetpoint());
        this.rightSetpointEntry.setDouble(rightExtendPID.getSetpoint());

        if (!enabled)
            return;

        if (extended) {
            intakeMotor.set(intakeMotorOutputEntry.getDouble(0.2));
            if (backwards) {
                // motorRun.set(ControlMode.PercentOutput, -runSpeed);
            } else {
                // motorRun.set(ControlMode.PercentOutput, runSpeed);
            }
        } else {
            // motorRun.set(ControlMode.PercentOutput, 0.0);
        }

        // motorExtend.set(ControlMode.PercentOutput, extendSpeed);
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        this.periodic();
    }

    public double getLeftExtendAngle() {
        return encoderPositionToAngle(leftExtendMotor.getSelectedSensorPosition());
    }

    public double getRightExtendAngle() {
        return encoderPositionToAngle(rightExtendMotor.getSelectedSensorPosition());
    }

    private double encoderPositionToAngle(double position) {
        return position * extendGearRatio * 360 / 4096.0;
    }

    public void extend() {
        this.extended = true;
        this.backwards = false;
    }

    public void retract() {
        this.extended = false;
    }

    public void setExtendMotorOutputs(double left, double right) {
        if (!enabled) {
            leftExtendMotor.set(ControlMode.PercentOutput, 0);
            rightExtendMotor.set(ControlMode.PercentOutput, 0);
            return;
        }

        leftMotorOutputEntry.setDouble(left);
        // speed = Math.min(0.3, Math.max(-0.3, speed));
        // left = Math.max(Math.abs(left), 0.1) * Math.signum(left);
        leftExtendMotor.set(ControlMode.PercentOutput,
                left + (leftFeedForward
                        .calculate(leftExtendPID.getSetpoint() > 0 ? IntakeConfig.maxExtendAngle * (Math.PI / 180)
                                : -IntakeConfig.maxExtendAngle * (Math.PI / 180), 2)
                        / 12));

        rightMotorOutputEntry.setDouble(right);
        // speed = Math.min(0.3, Math.max(-0.3, speed));
        // right = Math.max(Math.abs(right), 0.1) * Math.signum(right);
        rightExtendMotor.set(ControlMode.PercentOutput,
                right + (rightFeedForward
                        .calculate(rightExtendPID.getSetpoint() > 0 ? IntakeConfig.maxExtendAngle * (Math.PI / 180)
                                : -IntakeConfig.maxExtendAngle * (Math.PI / 180), 2)
                        / 12));
    }

    public void setControllerSetpoints(double setpoint) {

        leftExtendPID.setSetpoint(setpoint);
        rightExtendPID.setSetpoint(setpoint);
    }

    public double calculateLeftPID(double measurement) {
        return leftExtendPID.calculate(measurement, IntakeConfig.maxExtendAngle);
    }

    public double calculateRightPID(double measurement) {
        return rightExtendPID.calculate(measurement, IntakeConfig.maxExtendAngle);
    }

    public boolean leftAtSetpoint() {
        boolean atSetpoint = leftExtendPID.atSetpoint();

        leftAtSetpointEntry.setBoolean(atSetpoint);
        return atSetpoint;
    }

    public boolean rightAtSetpoint() {
        boolean atSetpoint = rightExtendPID.atSetpoint();

        rightAtSetpointEntry.setBoolean(atSetpoint);
        return atSetpoint;
    }

    public void setReverse(boolean backwards) {
        this.backwards = backwards;
    }

    public void reverseDirection() {
        this.backwards = !backwards;
    }

    public void resetEncoders() {
        leftExtendMotor.setSelectedSensorPosition(0);
        rightExtendMotor.setSelectedSensorPosition(0);
    }
}