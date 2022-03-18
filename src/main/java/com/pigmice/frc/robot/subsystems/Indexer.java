// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Utils;
import com.pigmice.frc.robot.RPMPController;
import com.pigmice.frc.robot.Constants.IndexerConfig;
import com.revrobotics.ColorSensorV3;

public class Indexer extends SubsystemBase {
  private boolean enabled = true;
  private boolean freeSpinEnabled = true;
  private TalonSRX motor;

  private ColorSensorV3 colorSensor;

  private final ShuffleboardTab indexerTab;
  private final NetworkTableEntry enabledEntry;
  private final NetworkTableEntry freeSpinEnabledEntry;
  private final NetworkTableEntry motorOutputEntry;
  private final NetworkTableEntry rotateAngleEntry;
  private final NetworkTableEntry targetRPMEntry;
  private final NetworkTableEntry currentRPMEntry;
  private final NetworkTableEntry atTargetEntry;

  private final NetworkTableEntry rEntry;
  private final NetworkTableEntry gEntry;
  private final NetworkTableEntry bEntry;
  private final NetworkTableEntry irEntry;
  private final NetworkTableEntry proximityEntry;

  // RPM stuff for free spin
  double targetRPM = 0;
  private final RPMPController rpmpController = new RPMPController(IndexerConfig.freeSpin_kP, 0.25);
  private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;
  private boolean atTarget;

  /** Creates a new Indexer. */
  public Indexer() {
    this.motor = new TalonSRX(IndexerConfig.motorPort);
    this.motor.setInverted(IndexerConfig.motorInverted);

    this.motor.setSensorPhase(true);
    this.motor.configSelectedFeedbackSensor(feedbackDevice);

    this.colorSensor = new ColorSensorV3(Port.kOnboard);

    this.indexerTab = Shuffleboard.getTab("Indexer");
    this.enabledEntry = indexerTab.add("Enabled", enabled).getEntry();
    this.freeSpinEnabledEntry = indexerTab.add("Free Spin Enabled", freeSpinEnabled).getEntry();
    this.motorOutputEntry = indexerTab.add("Motor Output", 0).getEntry();
    this.rotateAngleEntry = indexerTab.add("Rotate Angle", 0).getEntry();
    this.targetRPMEntry = indexerTab.add("Target RPM", targetRPM).getEntry();
    this.currentRPMEntry = indexerTab.add("Current RPM", 0).getEntry();
    this.atTargetEntry = indexerTab.add("At Target RPM", false).getEntry();

    this.rEntry = indexerTab.add("Color R", 0.0).getEntry();
    this.gEntry = indexerTab.add("Color G", 0.0).getEntry();
    this.bEntry = indexerTab.add("Color B", 0.0).getEntry();
    this.irEntry = indexerTab.add("Color IR", 0.0).getEntry();
    this.proximityEntry = indexerTab.add("Color Proximity", 0.0).getEntry();
  }

  public void enable() {setEnabled(true);}
  public void disable() {setEnabled(false);}
  public void toggle() {setEnabled(!this.enabled);}
  public void setEnabled(boolean enabled) {this.enabled = enabled; enabledEntry.setBoolean(enabled);}

  public void enableFreeSpin() {setFreeSpin(true);}
  public void disableFreeSpin() {setFreeSpin(false);}
  public void toggleFreeSpin() {setFreeSpin(!freeSpinEnabled);}
  public void setFreeSpin(boolean freeSpinEnabled) {this.freeSpinEnabled = freeSpinEnabled; freeSpinEnabledEntry.setBoolean(enabled);}

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void periodic() {
    Color color = colorSensor.getColor();
    rEntry.setDouble(color.red);
    gEntry.setDouble(color.green);
    bEntry.setDouble(color.blue);
    irEntry.setDouble(colorSensor.getIR());
    proximityEntry.setDouble(colorSensor.getProximity());

    if (!enabled)
      return;

    if (!freeSpinEnabled)
      return;

      this.setTargetSpeed(200);

  // from tarmac edge
  // this.setTargetSpeeds(1600, 1800);
  // from fender
  // this.setTargetSpeeds(900, 2400);
  double vecocity = motor.getSelectedSensorVelocity();

  double actualRPM = Utils.calculateRPM(vecocity, feedbackDevice);
  currentRPMEntry.setDouble(actualRPM);

  double motorOutputTarget = rpmpController.update(actualRPM);

  this.atTarget = Math.abs(targetRPM - actualRPM) <= IndexerConfig.velocityThreshold;
  this.atTargetEntry.setBoolean(this.atTarget);

  setMotorOutput(motorOutputTarget);
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

  public double getRotateAngle() {
    double rotateAngle = (this.getEncoderPosition() / 4096.0) * 360.0;
    rotateAngleEntry.setDouble(rotateAngle);
    return rotateAngle;
  }

  public void resetEncoder() {
    motor.setSelectedSensorPosition(0);
  }

  // Functions for free spin
  public void setTargetSpeed(double targetRPM) {
    this.targetRPM = targetRPM;
  }

  public boolean isAtTargetVelocity() {
    return targetRPM != 0 && this.atTarget;
  }

  @Override
  public void simulationPeriodic() {
    periodic();
  }
}