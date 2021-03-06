// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.indexer;

import com.pigmice.frc.robot.Constants.IndexerConfig;
import com.pigmice.frc.robot.subsystems.Indexer;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class SpinIndexerToAngleProfiled extends ProfiledPIDCommand {
    private Indexer indexer;
    private final boolean infinite;

    public SpinIndexerToAngleProfiled(Indexer indexer, double angle, boolean infinite) {
        super(
                new ProfiledPIDController(
                        IndexerConfig.kP,
                        IndexerConfig.kI,
                        IndexerConfig.kD,
                        new TrapezoidProfile.Constraints(IndexerConfig.maxRotateVelocity,
                                IndexerConfig.maxRotateAcceleration)),
                indexer::getRotateAngle,
                angle,
                (output, setpoint) -> {
                    indexer.setMotorOutput(output);
                },
                indexer);

        this.indexer = indexer;
        this.infinite = infinite;

        getController().setTolerance(IndexerConfig.angleTolerableError,
                IndexerConfig.angleTolerableEndVelocity);

        addRequirements(indexer);
    }

    @Override
    public void end(boolean interrupted) {
        this.indexer.stopMotor();
    }

    @Override
    public boolean isFinished() {
        return !this.infinite && getController().atGoal();
    }
}