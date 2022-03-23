// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class RetractIntake extends CommandBase {
  private final Intake intake;

  public RetractIntake(Intake intake) {
    this.intake = intake;

    addRequirements(intake);
  }

  @Override
  public void initialize() {
    intake.enable();
    intake.setControllerSetpoints(0);
    // intake.resetEncoders();
  }

  @Override
  public void execute() {
    double leftAngle = intake.getLeftExtendAngle();
    double rightAngle = intake.getRightExtendAngle();

    double leftOutput = intake.calculateLeftPID(leftAngle);
    double rightOutput = intake.calculateLeftPID(rightAngle);

    intake.setExtendMotorOutputs(leftOutput, rightOutput);
  }

  @Override
  public void end(boolean interrupted) {
    intake.disable();
  }

  @Override
  public boolean isFinished() {
    return intake.leftAtSetpoint() && intake.rightAtSetpoint();
  }
}
