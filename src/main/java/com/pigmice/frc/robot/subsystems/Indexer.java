// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Utils;
import com.pigmice.frc.robot.RPMPController;
import com.pigmice.frc.robot.Constants.IndexerConfig;

public class Indexer extends SubsystemBase {
  private boolean enabled;
  private TalonSRX motor;
  
  private static final double RPM_NOT_SET = -1;
  private double targetRPM = RPM_NOT_SET;

  private final double INDEXER_KP = .01D;
  private final double INDEXER_KS = 0;
  private final double INDEXER_KV = .5;

  private final RPMPController motorRPMController = new RPMPController(INDEXER_KP, 0.25);

  private final FeedbackDevice feedbackDevice = FeedbackDevice.CTRE_MagEncoder_Absolute;

  ShuffleboardTab indexerTab;
  NetworkTableEntry setRPM;
  NetworkTableEntry actualRPM;
  
  /** Creates a new Indexer. */
  public Indexer() {
    this.motor = new TalonSRX(IndexerConfig.motorPort);
    this.motor.setInverted(IndexerConfig.motorInverted);
    this.motor.configSelectedFeedbackSensor(feedbackDevice);

    this.indexerTab = Shuffleboard.getTab("Indexer");
    this.setRPM = indexerTab.add("Indexer Set RPM", 1).getEntry();
    this.actualRPM = indexerTab.add("Indexer Actual RPM", 1).getEntry();
  }

  public void enable() {setEnabled(true);}
  public void disable() {setEnabled(false);}
  public void toggle() {this.setEnabled(!this.enabled);}
  public void setEnabled(boolean enabled) {this.enabled = enabled;}

  @Override
  public void periodic() {
    if (!enabled)
            setTargetSpeed(0);
        else
            setTargetSpeed(1000);

        double currentVelocity = motor.getSelectedSensorVelocity();
        double actualRPM = Utils.calculateRPM(currentVelocity, feedbackDevice);
        double targetRPM = motorRPMController.update(actualRPM);

        this.actualRPM.setDouble(actualRPM);

        motor.set(ControlMode.PercentOutput, targetRPM);
  }

  public void setTargetSpeed(double target) {
    motorRPMController.setTargetRPM(target);
    targetRPM = target;
  }

  public void stopMotors() {
    targetRPM = 0;
    motorRPMController.setTargetRPM(0);
  }

  public boolean isAtTargetVelocity() {
    return motor.getClosedLoopError() <= IndexerConfig.velocityThreshold;
  }

  @Override
    public void simulationPeriodic() {
        periodic();
    }
}
