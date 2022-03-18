// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.commands.Indexer.SpinIndexerToAngle;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.*;

public class ShootBallCommand extends SequentialCommandGroup {
  public ShootBallCommand(Shooter shooter, Indexer indexer) {
    super(
        new InstantCommand(() -> shooter.enable()),
        new WaitUntilCommand(shooter::isAtTargetVelocity),
        new InstantCommand(() -> indexer.resetEncoder()),
        new InstantCommand(() -> indexer.enable()),
        new SpinIndexerToAngle(indexer, 200, false));

    addRequirements(shooter);
    addRequirements(indexer);
  }
}