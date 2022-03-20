package com.pigmice.frc.robot.subsystems;

import com.pigmice.frc.robot.Constants.ShooterConfig;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
	private boolean enabled = true;

	private CANSparkMax topMotor, botMotor;

	private double vThresh;

	// private final RPMPController topController = new RPMPController(shooterP,
	// 0.25);
	// private final RPMPController botController = new RPMPController(shooterP,
	// 0.25);
	private final SparkMaxPIDController topController, botController;
	private final RelativeEncoder topEncoder, botEncoder;

	public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;

	private final ShuffleboardTab shooterTab;

	private final NetworkTableEntry topRPMEntry, bottomRPMEntry, actualTopRPM,
			actualBottomRPM, atTargetEntry;

	// private static final double MAX_RPM_775 = 18700;
	// private static final double MAX_RPS_775 = MAX_RPM_775 / 60;

	private boolean atTarget = false;

	private static final double INDEX_MODE_RPM = -200.0;

	private ShooterMode mode;

	// Create a new Shooter
	public Shooter() {
		this.topMotor = new CANSparkMax(ShooterConfig.topMotorPort, MotorType.kBrushless);
		this.botMotor = new CANSparkMax(ShooterConfig.bottomMotorPort, MotorType.kBrushless);

		this.topController = topMotor.getPIDController();
		this.botController = botMotor.getPIDController();

		this.topEncoder = this.topMotor.getEncoder();
		this.botEncoder = this.botMotor.getEncoder();

		topMotor.setInverted(true);

		maxRPM = 5700;

		maxVel = 2000; // rpm
		maxAcc = 1500;

		// configure topController
		this.topController.setP(ShooterConfig.kP);
		this.topController.setI(ShooterConfig.kI);
		this.topController.setD(ShooterConfig.kD);
		this.topController.setIZone(ShooterConfig.kIz);
		this.topController.setFF(ShooterConfig.kFF);
		this.topController.setOutputRange(ShooterConfig.kMinOutput, ShooterConfig.kMaxOutput);

		// configure botController
		this.botController.setP(ShooterConfig.kP);
		this.botController.setI(ShooterConfig.kI);
		this.botController.setD(ShooterConfig.kD);
		this.botController.setIZone(ShooterConfig.kIz);
		this.botController.setFF(ShooterConfig.kFF);
		this.botController.setOutputRange(ShooterConfig.kMinOutput, ShooterConfig.kMaxOutput);

		// configure topController smart motion
		int smartMotionSlot = 0;
		this.topController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
		this.topController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
		this.topController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
		this.topController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

		// configure botController smart motion
		this.botController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
		this.botController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
		this.botController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
		this.botController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

		// topMotor.setNeutralMode(NeutralMode.Coast);
		// botMotor.setNeutralMode(NeutralMode.Coast);

		this.shooterTab = Shuffleboard.getTab("Shooter");

		this.topRPMEntry = shooterTab.add("Top RPM", 1).getEntry();
		this.bottomRPMEntry = shooterTab.add("Bottom RPM", 1).getEntry();

		this.actualTopRPM = shooterTab.add("Actual Top RPM", 1).getEntry();
		this.actualBottomRPM = shooterTab.add("Actual Bottom RPM", 1).getEntry();

		this.atTargetEntry = shooterTab.add("At Target", this.atTarget).getEntry();

		this.mode = ShooterMode.AUTO;
	}

	public void enable() {
		setEnabled(true);
	}

	public void disable() {
		setEnabled(false);
		this.topController.setReference(0.0, ControlType.kVelocity);
		this.botController.setReference(0.0, ControlType.kVelocity);
	}

	public void toggle() {
		this.setEnabled(!this.enabled);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void periodic() {
		if (!enabled) {
			this.setMode(ShooterMode.OFF);
		}

		double topRPM = this.mode.getTopRPM();
		double botRPM = this.mode.getBottomRPM();

		this.topController.setReference(topRPM, ControlType.kVelocity);
		this.botController.setReference(botRPM, ControlType.kVelocity);

		double topActualRPM = this.topEncoder.getVelocity();
		double botActualRPM = this.botEncoder.getVelocity();

		// System.out.println("MODE: " + this.mode + " | TOP RPM: " + topActualRPM + " |
		// BOTTOM RPM: " + botActualRPM);

		this.actualTopRPM.setDouble(topActualRPM);
		this.actualBottomRPM.setDouble(botActualRPM);

		this.atTargetEntry.setBoolean(this.atTarget);
	}

	public double getCurrentRPM() {
		return this.botEncoder.getVelocity();
	}

	@Override
	public void simulationPeriodic() {
		this.periodic();
	}

	public void stopMotors() {
		this.topController.setReference(0.0, ControlType.kVelocity);
		this.botController.setReference(0.0, ControlType.kVelocity);
		this.atTarget = false;
	}

	public boolean isAtTargetVelocity() {
		return Math
				.abs(1 - this.mode.getTopRPM() / this.topEncoder.getVelocity()) <= ShooterConfig.spinUpThresholdPercent
				&& Math.abs(1 - this.mode.getBottomRPM()
						/ this.botEncoder.getVelocity()) <= ShooterConfig.spinUpThresholdPercent;
	}

	public boolean didJustShoot() {
		boolean didShoot = Math
				.abs(this.mode.getTopRPM() - this.topEncoder.getVelocity()) >= ShooterConfig.shotThresholdRPM ||
				Math.abs(this.mode.getBottomRPM() - this.botEncoder.getVelocity()) >= ShooterConfig.shotThresholdRPM;

		if (didShoot) {
			System.out.println(
					"SHOT WITH TOP: " + this.topEncoder.getVelocity() + " | BOTTOM: " + this.botEncoder.getVelocity());
		}

		return didShoot;
	}

	public ShooterMode getMode() {
		return mode;
	}

	public void setMode(ShooterMode mode) {
		this.mode = mode;
		this.enabled = true;
	}
}