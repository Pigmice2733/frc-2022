// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ExtendIntake extends CommandBase {
  private final Intake intake;
  private final Indexer indexer;

  public ExtendIntake(Intake intake) {
    this(intake, null);
  }

  public ExtendIntake(Intake intake, Indexer indexer) {
    this.intake = intake;
    this.indexer = indexer;

    addRequirements(intake);
  }

  @Override
  public void initialize() {
    intake.enable();
    intake.extend();
    if (indexer != null) {
      indexer.enable();
      int numBalls = indexer.getBallTracker().getSize();
      if (numBalls < 2) {
        indexer.setMode(IndexerMode.FREE_SPIN);
      } else {
        indexer.setMode(IndexerMode.HOLD);
      }
    }
  }

  @Override
  public void execute() {

  }

  @Override
  public void end(boolean interrupted) {
    intake.setExtendMotorOutputs(0, 0);
    intake.setFullyExtended(true);
  }

  @Override
  public boolean isFinished() {
    return intake.leftAtSetpoint() && intake.rightAtSetpoint();
  }
}
