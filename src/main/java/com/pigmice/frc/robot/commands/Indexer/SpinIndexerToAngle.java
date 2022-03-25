// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.indexer;

import com.pigmice.frc.robot.Constants.IndexerConfig;
import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.subsystems.Indexer;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

public class SpinIndexerToAngle extends PIDCommand {
    private Indexer indexer;
    private final boolean infinite;

    public SpinIndexerToAngle(Indexer indexer, double angle, boolean infinite) {
        super(
                new PIDController(IndexerConfig.kP, IndexerConfig.kI, IndexerConfig.kD),
                indexer::getRotateAngle,
                angle,
                indexer::setMotorOutput,
                indexer);

        this.indexer = indexer;
        this.infinite = infinite;

        getController().setTolerance(IndexerConfig.angleTolerableError,
                IndexerConfig.angleTolerableEndVelocity);

        addRequirements(indexer);
    }

    @Override
    public void initialize() {
        System.out.println("SPINNING INDEXER TO ANGLE");
        this.indexer.enable();
        this.indexer.setMode(IndexerMode.ANGLE);
        this.indexer.resetEncoder();
    }

    @Override
    public void end(boolean interrupted) {
        this.indexer.stopMotor();
    }

    @Override
    public boolean isFinished() {
        return !this.infinite && getController().atSetpoint();
    }
}