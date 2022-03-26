package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.IntakeConfig;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Intake extends Subsystem {
    private final TalonSRX leftExtendMotor, rightExtendMotor;
    public final PIDController leftExtendPID, rightExtendPID;

    private final CANSparkMax intakeMotor;

    private boolean enabled, extended, backwards, fullyExtended, overrideOutput;

    private final ShuffleboardTab intakeTab;
    private final NetworkTableEntry enabledEntry;
    private final NetworkTableEntry extendedEntry;
    private final NetworkTableEntry leftMotorOutputEntry;
    private final NetworkTableEntry leftRotateAngleEntry;
    private final NetworkTableEntry rightMotorOutputEntry;
    private final NetworkTableEntry rightRotateAngleEntry;
    private final NetworkTableEntry leftAtSetpointEntry;
    private final NetworkTableEntry rightAtSetpointEntry;
    private final NetworkTableEntry leftSetpointEntry;
    private final NetworkTableEntry rightSetpointEntry;
    private final NetworkTableEntry intakeMotorOutputEntry;
    private final NetworkTableEntry toggleIntakeEntry;

    /** Creates a new Intake. */
    public Intake() {
        // motorRun = new TalonSRX(IntakeConfig.intakeBottomPort);
        // motorRun.configFactoryDefault();

        // Left Extend Motor
        leftExtendMotor = new TalonSRX(IntakeConfig.extendLeftPort);
        leftExtendMotor.configFactoryDefault();
        leftExtendMotor.enableVoltageCompensation(true);
        leftExtendMotor.configVoltageCompSaturation(10);
        leftExtendMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        leftExtendMotor.setSelectedSensorPosition(0.0);
        leftExtendMotor.setInverted(false);
        leftExtendMotor.setSensorPhase(true);

        // Right Extend Motor
        rightExtendMotor = new TalonSRX(IntakeConfig.extendRightPort);
        rightExtendMotor.configFactoryDefault();
        rightExtendMotor.enableVoltageCompensation(true);
        rightExtendMotor.configVoltageCompSaturation(10);
        rightExtendMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        rightExtendMotor.setSelectedSensorPosition(0.0);
        rightExtendMotor.setInverted(true);
        rightExtendMotor.setSensorPhase(true);

        // Left Extend PID
        leftExtendPID = new PIDController(IntakeConfig.leftExtendP, IntakeConfig.leftExtendI, IntakeConfig.leftExtendD);
        leftExtendPID.setTolerance(IntakeConfig.extendTolError, IntakeConfig.extendTolEndVelo);
        leftExtendPID.setSetpoint(0);

        // Right Extend PID
        rightExtendPID = new PIDController(IntakeConfig.rightExtendP, IntakeConfig.rightExtendI,
                IntakeConfig.rightExtendD);
        rightExtendPID.setTolerance(IntakeConfig.extendTolError, IntakeConfig.extendTolEndVelo);
        rightExtendPID.setSetpoint(0);

        this.enabled = false;
        this.extended = false;
        this.backwards = false;
        this.fullyExtended = false;
        this.overrideOutput = false;

        // Shuffleboard Entries
        this.intakeTab = Shuffleboard.getTab("Intake");
        this.enabledEntry = intakeTab.add("Enabled", enabled).getEntry();
        this.extendedEntry = intakeTab.add("Extended", extended).getEntry();
        this.leftMotorOutputEntry = intakeTab.add("Left Motor Output", 0).getEntry();
        this.leftRotateAngleEntry = intakeTab.add("Left Rotate Angle", 0).getEntry();
        this.rightMotorOutputEntry = intakeTab.add("Right Motor Output", 0).getEntry();
        this.rightRotateAngleEntry = intakeTab.add("Right Rotate Angle", 0).getEntry();
        this.leftAtSetpointEntry = intakeTab.add("Left at Setpoint", false).getEntry();
        this.rightAtSetpointEntry = intakeTab.add("Right at Setpoint", false).getEntry();
        this.leftSetpointEntry = intakeTab.add("Left Setpoint", 0).getEntry();
        this.rightSetpointEntry = intakeTab.add("Right Setpoint", 0).getEntry();
        this.intakeMotorOutputEntry = intakeTab.add("Intake Motor Ouput", IntakeConfig.intakeSpeed).getEntry();
        this.toggleIntakeEntry = intakeTab.add("Toggle Intake", false).getEntry();
        intakeTab.add("Left PID", leftExtendPID);
        intakeTab.add("Right PID", rightExtendPID);

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

        if (overrideOutput)
            return;

        double leftAngle = this.getLeftExtendAngle();
        double rightAngle = this.getRightExtendAngle();

        final double limit = 0.30;

        double leftOutput = MathUtil.clamp(this.calculateLeftPID(leftAngle), -limit, limit);
        double rightOutput = MathUtil.clamp(this.calculateRightPID(rightAngle), -limit, limit);

        this.setExtendMotorOutputs(leftOutput, rightOutput);

        if (extended || this.isTestMode()) {
            runIntakeMotor();
        } else
            intakeMotor.set(0);
    }

    public void setEncoderPositions(double positions) {
        this.leftExtendMotor.setSelectedSensorPosition(positions);
        this.rightExtendMotor.setSelectedSensorPosition(positions);
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        this.periodic();
    }

    // Sets an output to intake motor based on if reversed
    public void runIntakeMotor() {
        double output = IntakeConfig.intakeSpeed;
        if (isTestMode())
            output = intakeMotorOutputEntry.getDouble(output);
        if (!backwards)
            intakeMotor.set(output);
        else
            intakeMotor.set(-output);
    }

    public double getLeftExtendAngle() {
        return encoderPositionToAngle(leftExtendMotor.getSelectedSensorPosition());
    }

    public double getRightExtendAngle() {
        return encoderPositionToAngle(rightExtendMotor.getSelectedSensorPosition());
    }

    private double encoderPositionToAngle(double position) {
        return position * IntakeConfig.extendGearRator * 360 / 4096.0;
    }

    public void toggleExtended() {
        setExtended(!this.extended);
    }

    public void retract() {
        setExtended(false);
        this.backwards = false;
        this.setControllerSetpoints(0.0);
    }

    public void extend() {
        setExtended(true);
        this.backwards = false;
        this.setControllerSetpoints(IntakeConfig.maxExtendAngle);
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
        extendedEntry.setBoolean(extended);
    }

    // Feedforward values will automaticaly be added to speed, to stop motors set
    // enabled or extended to false
    public void setExtendMotorOutputs(double left, double right) {
        if (!enabled) {
            leftExtendMotor.set(ControlMode.PercentOutput, 0);
            rightExtendMotor.set(ControlMode.PercentOutput, 0);
            return;
        }

        // Set percent outputs to motors
        leftExtendMotor.set(ControlMode.PercentOutput, left);
        rightExtendMotor.set(ControlMode.PercentOutput, right);

        // Update values on shuffleboard
        leftMotorOutputEntry.setDouble(left);
        rightMotorOutputEntry.setDouble(right);
    }

    public void setControllerSetpoints(double setpoint) {

        leftExtendPID.setSetpoint(setpoint);
        rightExtendPID.setSetpoint(setpoint);
    }

    public double calculateLeftPID(double measurement) {
        return leftExtendPID.calculate(measurement);
    }

    public double calculateRightPID(double measurement) {
        return rightExtendPID.calculate(measurement);
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

    public void setFullyExtended(boolean fullyExtended) {
        this.fullyExtended = fullyExtended;
    }

    public void setOverrideOutput(boolean overrideOutput) {
        this.overrideOutput = overrideOutput;
    }

    public void resetEncoders() {
        leftExtendMotor.setSelectedSensorPosition(0);
        rightExtendMotor.setSelectedSensorPosition(0);
    }

    boolean prevToggleValue = false;

    public void testPeriodic() {
        intakeMotor.set(intakeMotorOutputEntry.getDouble(0.0));

        if (toggleIntakeEntry.getBoolean(false) != prevToggleValue) {
            if (!extended) {
                this.enable();
                this.extend();
            } else if (extended) {
                this.enable();
                this.retract();
            }

            prevToggleValue = toggleIntakeEntry.getBoolean(false);
        }

        this.periodic();
    }
}