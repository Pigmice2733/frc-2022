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
                        List.of(new Translation2d(10, 5), new Translation2d(10, -5)),
                        new Pose2d(20, 0, new Rotation2d(0)),
                        drivetrain.generateTrajectoryConfig()));
        this.setName("[6] SPathCommand");

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        System.out.println("SPathCommand Execute Called");
        super.execute();
    }

    @Override
  public void end(boolean interrupted) {
    System.out.println("S PATH END");
  }
}
