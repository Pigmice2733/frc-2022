// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.Indexer;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.Constants.IndexerConfig;
import com.pigmice.frc.robot.Constants.IntakeConfig;
import com.pigmice.frc.robot.subsystems.Indexer;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
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
                indexer
        );
            
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
        /*System.out.println(
                "ROTATE | DISTANCE FROM SETPOINT: "
                        + (getController().getSetpoint().position - indexer.getRotateAngle())
                        + " SETPOINT: " + getController().getSetpoint().position + " | AT SETPOINT? "
                        + getController().atGoal());*/
        return !this.infinite && getController().atSetpoint();
    }
}