package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.IntakeConfig;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private TalonSRX motorExtend;
    private boolean enabled, extended, backwards;
    private static double runSpeed;
    private double extendSpeed;
    private final double extendGearRatio = 1;

    private final ShuffleboardTab intakeTab;
    private final NetworkTableEntry enabledEntry;
    private final NetworkTableEntry motorOutputEntry;
    private final NetworkTableEntry rotateAngleEntry;

    /** Creates a new Intake. */
    public Intake() {
        // motorRun = new TalonSRX(IntakeConfig.intakeBottomPort);
        // motorRun.configFactoryDefault();

        motorExtend = new TalonSRX(IntakeConfig.intakeTopPort);
        motorExtend.configFactoryDefault();
        motorExtend.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        motorExtend.setSelectedSensorPosition(0.0);
        motorExtend.setInverted(true);
        motorExtend.setSensorPhase(true);

        runSpeed = IntakeConfig.intakeSpeed;
        extendSpeed = 0.0;

        this.enabled = false;
        this.extended = false;
        this.backwards = false;

        this.intakeTab = Shuffleboard.getTab("Intake");
        this.enabledEntry = intakeTab.add("Enabled", enabled).getEntry();
        this.motorOutputEntry = intakeTab.add("Motor Output", 0).getEntry();
        this.rotateAngleEntry = intakeTab.add("Rotate Angle", 0).getEntry();
    }

    public void enable() {
        setExtendSpeed(0);
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
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
        if (!enabled)
            return;

        if (extended) {
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

    public double extendAngle() {
        //return 0;
        double angle = motorExtend.getSelectedSensorPosition() * extendGearRatio * 360 / 4096;
        this.rotateAngleEntry.setDouble(angle);
        return angle;
    }

    public void extend() {
        this.extended = true;
        this.backwards = false;
    }

    public void retract() {
        this.extended = false;
    }

    public void setExtendSpeed(double speed) {
        if (!enabled) {
            motorExtend.set(ControlMode.PercentOutput, 0);
            return;
        }

        motorOutputEntry.setDouble(speed);
        //speed = Math.min(0.3, Math.max(-0.3, speed));
        speed = Math.max(Math.abs(speed), 0.1) * Math.signum(speed);
        motorExtend.set(ControlMode.PercentOutput, speed);
    }

    public void setReverse(boolean backwards) {
        this.backwards = backwards;
    }

    public void reverseDirection() {
        this.backwards = !backwards;
    }

    public void resetEncoder() {
        motorExtend.setSelectedSensorPosition(0);
    }
}