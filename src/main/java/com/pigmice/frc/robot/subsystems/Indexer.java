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
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.IndexerConfig;
import com.revrobotics.ColorSensorV3;

public class Indexer extends SubsystemBase {
  private boolean enabled = true;
  private TalonSRX motor;

  private ColorSensorV3 colorSensor;

  private ShuffleboardTab indexerTab;
  private NetworkTableEntry enabledEntry;
  private NetworkTableEntry motorOutputEntry;
  //private NetworkTableEntry encoderPositionEntry;
  private NetworkTableEntry rotateAngleEntry;

  private NetworkTableEntry rEntry;
  private NetworkTableEntry gEntry;
  private NetworkTableEntry bEntry;
  private NetworkTableEntry irEntry;
  private NetworkTableEntry proximityEntry;

  /** Creates a new Indexer. */
  public Indexer() {
    this.motor = new TalonSRX(IndexerConfig.motorPort);
    this.motor.setInverted(IndexerConfig.motorInverted);

    this.motor.setSensorPhase(true);

    this.colorSensor = new ColorSensorV3(Port.kOnboard);

    this.indexerTab = Shuffleboard.getTab("Indexer");
    this.enabledEntry = indexerTab.add("Enabled", enabled).getEntry();
    this.motorOutputEntry = indexerTab.add("Motor Output", 0).getEntry();
    //this.encoderPositionEntry = indexerTab.add("Encoder Position", 0).getEntry();
    this.rotateAngleEntry = indexerTab.add("Rotate Angle", 0).getEntry();

    this.rEntry = indexerTab.add("Color R", 0.0).getEntry();
    this.gEntry = indexerTab.add("Color G", 0.0).getEntry();
    this.bEntry = indexerTab.add("Color B", 0.0).getEntry();
    this.irEntry = indexerTab.add("Color IR", 0.0).getEntry();
    this.proximityEntry = indexerTab.add("Color Proximity", 0.0).getEntry();
  }

  public void enable() {
    setEnabled(true);
  }

  public void disable() {
    setEnabled(false);
  }

  public void toggle() {
    this.setEnabled(!this.enabled);
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    enabledEntry.setBoolean(enabled);
  }

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

    /*if (enabled) {
      setMotorOutput(0.25);
      encoderPositionEntry.setDouble(getEncoderPosition());
    } else {
      stopMotor();
    }*/
    //motor.set(ControlMode.PercentOutput, 0.5);
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

  @Override
  public void simulationPeriodic() {
    periodic();
  }
}
