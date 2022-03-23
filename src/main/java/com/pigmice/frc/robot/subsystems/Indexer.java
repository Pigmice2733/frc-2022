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

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
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

    this.motor.setSensorPhase(false);
    this.motor.configSelectedFeedbackSensor(feedbackDevice);

    this.motor.setSelectedSensorPosition(0.0);

    this.motor.setNeutralMode(NeutralMode.Brake);

    this.indexerTab = Shuffleboard.getTab("Indexer");
    this.enabledEntry = indexerTab.add("Enabled", enabled).getEntry();
    this.motorOutputEntry = indexerTab.add("Motor Output", 0).getEntry();
    this.rotateAngleEntry = indexerTab.add("Rotate Angle", 0).getEntry();

    this.ballTracker = new BallTracker();
    this.ballDetector = new BallDetector();
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void periodic() {
    this.ballDetector.setColorEntries();
    if (!enabled)
      return;

    System.out.println("INDEXER MODE IS " + this.mode);

    // switch on mode
    switch (mode) {
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

    System.out.println("NUM BALLS: " + this.ballTracker.getSize() +
        " | BALL 0: " + this.ballTracker.getBallInPosition(0) + " | BALL 1: " +
        this.ballTracker.getBallInPosition(1));
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
        this.shooter.setMode(ShooterMode.OFF);
        this.setMode(IndexerMode.HOLD);
        this.stopMotor();
        // CommandScheduler.getInstance().schedule(new RetractIntake(this.intake));
      } else {
        this.shooter.setMode(ShooterMode.OFF);
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
    if (!enabled) {
      motor.set(ControlMode.PercentOutput, 0);
      motorOutputEntry.setDouble(0);
      return;
    }
    motor.set(ControlMode.PercentOutput, output);
    motorOutputEntry.setDouble(output);
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
}