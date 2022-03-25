// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.BallDetector;
import com.pigmice.frc.robot.BallTracker;
import com.pigmice.frc.robot.Constants.IndexerConfig;
import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.indexer.EjectBallCommand;
import com.pigmice.frc.robot.commands.indexer.EjectByIntakeCommand;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Indexer extends Subsystem {
  private boolean enabled = true;
  private boolean isLookingForBalls = true;

  private TalonSRX motor;

  private final Intake intake;
  private final Shooter shooter;

  private final ShuffleboardTab indexerTab;
  private final NetworkTableEntry enabledEntry;
  private final NetworkTableEntry motorOutputEntry;
  private final NetworkTableEntry rotateAngleEntry;

  private BallTracker ballTracker;
  private BallDetector ballDetector;

  private IndexerMode mode = IndexerMode.FREE_SPIN;

  // RPM stuff for free spin
  double targetRPM = 0;
  private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

  private static final double FREE_SPIN_POWER = 0.30;

  private static final double GEAR_RATIO = 1.0 / 2.0;

  /** Creates a new Indexer. */
  public Indexer(Intake intake, Shooter shooter) {
    this.intake = intake;
    this.shooter = shooter;

    this.motor = new TalonSRX(IndexerConfig.motorPort);
    this.motor.configFactoryDefault();
    this.motor.setInverted(false);

    this.motor.setSensorPhase(true);
    this.motor.configSelectedFeedbackSensor(feedbackDevice);

    this.motor.setSelectedSensorPosition(0.0);

    this.motor.setNeutralMode(NeutralMode.Brake);

    this.indexerTab = Shuffleboard.getTab("Indexer");
    this.enabledEntry = indexerTab.add("Enabled", enabled).getEntry();
    this.motorOutputEntry = indexerTab.add("Motor Output", 0).getEntry();
    this.rotateAngleEntry = indexerTab.add("Rotate Angle", 0).getEntry();
    setBalls(false, false);
    SmartDashboard.putData("Reset Balls", new InstantCommand(() -> this.ballTracker.clear()));

    this.ballTracker = new BallTracker();
    this.ballDetector = new BallDetector();
  }

  public boolean isEnabled() {
    return enabled;
  }

  private void setBalls(boolean first, boolean second) {
    SmartDashboard.putBoolean("First Ball", first);
    SmartDashboard.putBoolean("Second Ball", second);
  }

  public void clearBalls() {
    this.ballTracker.clear();
  }

  @Override
  public void periodic() {
    boolean disabled = !enabled && !this.isTestMode();
    if (disabled) {
      this.setMotorOutput(0.0);
      return;
    }

    if (ballTracker.getSize() == 0) {
      setBalls(false, false);
    }

    // switch on mode
    switch (mode) {
      case SHUFFLEBOARD:
        setMotorOutput(this.motorOutputEntry.getDouble(FREE_SPIN_POWER));
        break;
      case HOLD:
        setMotorOutput(0.0);
        break;
      case SHOOT:
        if (this.shooter.isAtTargetVelocity()) {
          setMotorOutput(FREE_SPIN_POWER);
        } else {
          setMotorOutput(0.0);
        }
        break;
      case ANGLE:
        // spin to angle command handles motor output
        break;
      case FREE_SPIN:
        setMotorOutput(FREE_SPIN_POWER + (this.ballTracker.getSize() > 0 ? 0.15 : 0.0));
        doFreeSpin();
        break;
      case EJECT_BY_INTAKE:
        setMotorOutput(-FREE_SPIN_POWER);
        break;
    }
  }

  private void doFreeSpin() {
    if (!isLookingForBalls)
      return;

    Alliance ballAlliance = this.ballDetector.getNewBall();

    if (ballAlliance == Alliance.Invalid)
      return;

    Alliance alliance = DriverStation.getAlliance();
    System.out.println("ALLIANCE " + ballAlliance);

    if (alliance == ballAlliance) {
      System.out.println("CORRECT COLOR! SHOULD STORE!");
      ballTracker.newBallStored(ballAlliance);

      if (ballTracker.isFull()) {
        setBalls(true, true);
        this.shooter.setMode(ShooterMode.OFF);
        this.setMode(IndexerMode.HOLD);
        this.stopMotor();
        // CommandScheduler.getInstance().schedule(new RetractIntake(this.intake));
      } else {
        setBalls(true, false);
        this.shooter.setMode(ShooterMode.INDEX);
      }
    } else {
      if (ballTracker.getSize() == 0) {
        System.out.println("WRONG COLOR! SHOULD EJECT OUT SHOOTER!");
        CommandScheduler.getInstance().schedule(new EjectBallCommand(this, this.shooter, this.intake));
      } else if (ballTracker.getSize() == 1) {
        System.out.println("WRONG COLOR! SHOULD REVERSE INTAKE!");
        CommandScheduler.getInstance().schedule(new EjectByIntakeCommand(this, this.intake));
      }
    }
  }

  public void setMotorOutput(double output) {
    if (!enabled && !this.isTestMode()) {
      motor.set(ControlMode.PercentOutput, 0);
      motorOutputEntry.setDouble(0);
      return;
    }
    output = MathUtil.clamp(output, -0.50, 0.50);
    motor.set(ControlMode.PercentOutput, output);
    if (!this.isTestMode())
      motorOutputEntry.setDouble(output);
  }

  @Override
  public void updateShuffleboard() {
    this.ballDetector.setColorEntries();
  }

  public void stopMotor() {
    setMotorOutput(0);
  }

  public double getEncoderPosition() {
    return motor.getSelectedSensorPosition();
  }

  public double getEncoderVelocity() {
    return motor.getSelectedSensorVelocity() / 4096.0;
  }

  public double getRotateAngle() {
    double rotateAngle = (this.getEncoderPosition() / 4096.0) * 360.0 * GEAR_RATIO;
    rotateAngleEntry.setDouble(rotateAngle);
    return rotateAngle;
  }

  public void resetEncoder() {
    motor.setSelectedSensorPosition(0);
  }

  public void enable() {
    setEnabled(true);
  }

  public void disable() {
    setEnabled(false);
  }

  public void toggle() {
    setEnabled(!this.enabled);
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    enabledEntry.setBoolean(enabled);
  }

  public void setMode(IndexerMode mode) {
    this.mode = mode;
  }

  public void setLookingForBalls(boolean lookingForBalls) {
    this.isLookingForBalls = lookingForBalls;
  }

  public boolean isLookingForBalls() {
    return this.isLookingForBalls;
  }

  public BallTracker getBallTracker() {
    return this.ballTracker;
  }

  public BallDetector getBallDetector() {
    return this.ballDetector;
  }

  @Override
  public void simulationPeriodic() {
    this.periodic();
  }

  public void testPeriodic() {
    this.enabled = true;
    this.mode = IndexerMode.SHUFFLEBOARD;
    this.periodic();
  }
}