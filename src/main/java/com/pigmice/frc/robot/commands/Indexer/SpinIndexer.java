// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.Indexer;

import com.pigmice.frc.robot.subsystems.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SpinIndexer extends CommandBase {
  private Indexer indexer;
  private double speed;

  public SpinIndexer(Indexer indexer, double speed) {
    this.indexer = indexer;
    this.speed = speed;

    addRequirements(indexer);
}

@Override
public void initialize() {
    this.indexer.setTargetSpeed(speed);
}

@Override
public void execute() {

}

@Override
public boolean isFinished() {
    return this.indexer.isAtTargetVelocity();
}
}
