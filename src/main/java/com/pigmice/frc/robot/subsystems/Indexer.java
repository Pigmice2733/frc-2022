// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants.IndexerConfig;

public class Indexer extends SubsystemBase {
  private boolean enabled;

  private TalonSRX motor;

  ShuffleboardTab indexerTab;
  NetworkTableEntry enabledEntry;
  NetworkTableEntry motorOutputEntry;
  //NetworkTableEntry encoderPositionEntry;
  NetworkTableEntry rotateAngleEntry;
  
  /** Creates a new Indexer. */
  public Indexer() {
    this.motor = new TalonSRX(IndexerConfig.motorPort);
    this.motor.setInverted(IndexerConfig.motorInverted);

    this.indexerTab = Shuffleboard.getTab("Indexer");
    this.enabledEntry = indexerTab.add("Enabled", enabled).getEntry();
    this.motorOutputEntry = indexerTab.add("Motor Output", 0).getEntry();
    //this.encoderPositionEntry = indexerTab.add("Encoder Position", 0).getEntry();
    this.rotateAngleEntry = indexerTab.add("Encoder Rotate Angle", 0).getEntry();
  }

  public void enable() {setEnabled(true);}
  public void disable() {setEnabled(false);}
  public void toggle() {this.setEnabled(!this.enabled);}
  public void setEnabled(boolean enabled) {this.enabled = enabled; enabledEntry.setBoolean(enabled);}

  @Override
  public void periodic() {
    /*if (enabled) {
      setMotorOutput(0.25);
      encoderPositionEntry.setDouble(getEncoderPosition());
    }
    else {
      stopMotor();
    }*/
  }

  public void setMotorOutput(double output) {
    motor.set(ControlMode.PercentOutput, output);
    motorOutputEntry.setDouble(output);
  }

  public void stopMotor() {
    setMotorOutput(0);
  }

  private double encoderResetPosition = 0;
  public double getEncoderPosition() {
    return motor.getSelectedSensorPosition() - encoderResetPosition;
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