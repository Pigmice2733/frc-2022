// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands;

import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.commands.indexer.SpinIndexerToAngle;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShootBallCommand extends SequentialCommandGroup {
  public ShootBallCommand(Shooter shooter, Indexer indexer) {
    super(
        new SpinIndexerToAngle(indexer, -50.0, false),
        new InstantCommand(() -> {
          indexer.enable();
          indexer.setMode(IndexerMode.SHOOT);
        }));

    addRequirements(shooter, indexer);
  }
}