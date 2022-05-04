package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

public class Ramsete extends RamseteCommand {
    private Drivetrain drivetrain;
    Trajectory m_trajectory;

    public Ramsete(Drivetrain drivetrain, Trajectory trajectory) {
        super(trajectory, drivetrain::getPose,
                new RamseteController(DrivetrainConfig.kRamseteB, DrivetrainConfig.kRamseteZeta),
                new SimpleMotorFeedforward(DrivetrainConfig.ksVolts, DrivetrainConfig.kvVoltSecondsPerMeter,
                        DrivetrainConfig.kaVoltSecondsSquaredPerMeter),
                Drivetrain.driveKinematics, drivetrain::getWheelSpeeds,
                new PIDController(DrivetrainConfig.kPDriveVel, 0, 0),
                new PIDController(DrivetrainConfig.kPDriveVel, 0, 0),
                drivetrain::tankDriveVolts,
                drivetrain);

        drivetrain.resetOdometry(trajectory.getInitialPose());

        this.drivetrain = drivetrain;
        addRequirements(drivetrain);

        this.m_trajectory = trajectory;
    }

    @Override
    public void end(boolean interrupted) {
        this.drivetrain.tankDriveVolts(0.0, 0.0);
    }

    @Override
    public void execute() {
        super.execute();
    }
}
