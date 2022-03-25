// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class RetractIntake extends CommandBase {
  private final Intake intake;
  private final Indexer indexer;

  public RetractIntake(Intake intake) {
    this(intake, null);
  }

  public RetractIntake(Intake intake, Indexer indexer) {
    this.intake = intake;
    this.indexer = indexer;

    addRequirements(intake);

    if (indexer != null)
      addRequirements(indexer);
  }

  @Override
  public void initialize() {
    intake.retract();
    intake.setControllerSetpoints(0);
    intake.enable();
    intake.setFullyExtended(false);
    if (this.indexer != null) {
      if (indexer.getBallTracker().getSize() < 2) {
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
    intake.disable();
    intake.setExtendMotorOutputs(0, 0);
  }

  @Override
  public boolean isFinished() {
    return intake.leftAtSetpoint() && intake.rightAtSetpoint();
  }
}
