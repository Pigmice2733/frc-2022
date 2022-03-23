// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.Constants.IntakeConfig;
import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ExtendIntake extends CommandBase {
  private final Intake intake;

  public ExtendIntake(Intake intake) {
    this.intake = intake;

    addRequirements(intake);
  }

  @Override
  public void initialize() {
    intake.setControllerSetpoints(IntakeConfig.maxExtendAngle);
    intake.resetEncoders();
    intake.enable();
  }

  @Override
  public void execute() {
    double leftAngle = intake.getLeftExtendAngle();
    //double rightAngle = intake.getRightExtendAngle();

    System.out.println(leftAngle);

    double leftOutput = intake.calculateLeftPID(leftAngle);
    //double rightOutput = intake.calculateLeftPID(rightAngle);

    intake.setExtendMotorOutputs(leftOutput, 0);
  }

  @Override
  public void end(boolean interrupted) {
    intake.disable();
  }

  @Override
  public boolean isFinished() {
    //return intake.leftAtSetpoint() && intake.rightAtSetpoint();
    //System.out.println(intake.leftAtSetpoint());
    System.out.println(intake.leftExtendPID.getSetpoint() - intake.getLeftExtendAngle() + " | " + intake.leftExtendPID.getPositionError());
    return intake.leftAtSetpoint();
  }
}
