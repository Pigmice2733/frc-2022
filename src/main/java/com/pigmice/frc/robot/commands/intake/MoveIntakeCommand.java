// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MoveIntakeCommand extends CommandBase {
    private final Intake intake;
    private final boolean forwards;

    public MoveIntakeCommand(Intake intake) {
        this(intake, true);
    }

    public MoveIntakeCommand(Intake intake, boolean forwards) {
        this.intake = intake;
        this.forwards = forwards;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.enable();
        intake.extend();
        intake.setOverrideOutput(true);
        double output = 0.30 * (this.forwards ? 1 : -1);
        intake.setExtendMotorOutputs(output, output);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        intake.setExtendMotorOutputs(0, 0);
        intake.setFullyExtended(true);
        intake.setOverrideOutput(false);
    }

    @Override
    public boolean isFinished() {
        return intake.leftAtSetpoint() && intake.rightAtSetpoint();
    }
}
