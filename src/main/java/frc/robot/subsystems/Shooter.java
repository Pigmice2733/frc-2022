package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConfig;

public class Shooter extends SubsystemBase {
    private boolean enabled = true;

    private TalonSRX topShooterMotor;
    private TalonSRX bottomShooterMotor;

    private final double SHOOTER_KP = .04D;

    private final PIDController topPID = new PIDController(SHOOTER_KP, 0, 0);

    private final double SHOOTER_KS = 0;
    private final double SHOOTER_KV = .5;

    private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(SHOOTER_KS, SHOOTER_KV);

    private final ShuffleboardTab shooterTab;

    private final NetworkTableEntry topRPMEntry;
    private final NetworkTableEntry bottomRPMEntry;

    private final NetworkTableEntry actualTopRPM;
    private final NetworkTableEntry actualBottomRPM;

    // Create a new Shooter
    public Shooter() {
        topShooterMotor = new TalonSRX(Constants.ShooterConfig.topMotorPort);
        bottomShooterMotor = new TalonSRX(Constants.ShooterConfig.bottomMotorPort);

        topShooterMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        bottomShooterMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

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

        this.shooterTab.add(topPID);
    }

    private double calculateRPM(double raw) {
        return ((raw * 600) / Constants.SENSOR_UNITS_PER_ROTATION);
    }

    private double calculatePower(double RPM) {
        return RPM / 180D;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleEnabled() {
        setEnabled(!enabled);
    }

    @Override
    public void periodic() {
        double topRPM = this.topRPMEntry.getDouble(ShooterConfig.topMotorSpeed);
        double botRPM = this.bottomRPMEntry.getDouble(ShooterConfig.bottomMotorSpeed);

        double topTicksPerDs = rpmToTicksPerDs(topRPM);
        double botTicksPerDs = rpmToTicksPerDs(botRPM);

        double topVelocity = topShooterMotor.getSelectedSensorVelocity();
        double botVelocity = bottomShooterMotor.getSelectedSensorVelocity();

        double topRPS = topRPM / 60;
        double botRPS = botRPM / 60;

        double topFFVolts = feedforward.calculate(topRPS);
        double botFFVolts = feedforward.calculate(botRPS);

        double topFFNormalized = topFFVolts / 12;
        double botFFNormalized = botFFVolts / 12;

        this.actualTopRPM.setDouble(calculateRPM(topVelocity));// calculateRPM(topShooterMotor.getSelectedSensorVelocity()));
        this.actualBottomRPM.setDouble(calculateRPM(botVelocity));// calculateRPM(bottomShooterMotor.getSelectedSensorVelocity()));

        // this.shooterTab.add("Top Velocity",
        // topShooterMotor.getSelectedSensorVelocity());

        if (enabled) {
            topShooterMotor.set(ControlMode.Velocity, topTicksPerDs, DemandType.ArbitraryFeedForward, topFFNormalized);
            bottomShooterMotor.set(ControlMode.Velocity, botTicksPerDs, DemandType.ArbitraryFeedForward,
                    botFFNormalized);
            // bottomShooterMotor.set(ControlMode.PercentOutput, bottomOutput);
        }
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }

    public double rpmToTicksPerDs(int rpm) {
        double ticksPerMinute = rpm * ShooterConfig.TICKS_PER_ROTATION;
        double ticksPerS = ticksPerMinute / 60d;
        double ticksPerDs = ticksPerS / 10d;
        return ticksPerDs;
    }

    public double rpmToTicksPerDs(double rpm) {
        double ticksPerMinute = rpm * ShooterConfig.TICKS_PER_ROTATION;
        double ticksPerS = ticksPerMinute / 60d;
        double ticksPerDs = ticksPerS / 10d;
        return ticksPerDs;
    }

}