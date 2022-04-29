package com.pigmice.frc.robot.commands.drivetrain.routines;

import java.util.List;

import com.pigmice.frc.robot.commands.drivetrain.Ramsete;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;

public class SPathCommand extends Ramsete {

    public SPathCommand(Drivetrain drivetrain) {
        super(drivetrain,
                TrajectoryGenerator.generateTrajectory(new Pose2d(0, 0, new Rotation2d(0)),
                        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
                        new Pose2d(3, 0, new Rotation2d(0)),
                        drivetrain.generateTrajectoryConfig()));
    }
}
