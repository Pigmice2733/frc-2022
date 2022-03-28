package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.VisionAlignCommand;
import com.pigmice.frc.robot.commands.intake.MoveIntakeCommand;
import com.pigmice.frc.robot.commands.shooter.ShootBallWithModeCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Auto2BallTarmacSide extends SequentialCommandGroup {

        private static final double DISTANCE_TO_BALL_METERS = 1.0;

        public Auto2BallTarmacSide(Indexer indexer, Shooter shooter, Intake intake, Drivetrain drivetrain) {
                super(new InstantCommand(() -> {
                        indexer.getBallTracker().newBallStored(DriverStation.getAlliance());
                        shooter.setMode(ShooterMode.INDEX);
                }),
                                new MoveIntakeCommand(intake, true).withTimeout(1.5),
                                new DriveDistance(drivetrain,
                                                DISTANCE_TO_BALL_METERS),
                                new VisionAlignCommand(drivetrain),
                                new ShootBallWithModeCommand(indexer, shooter, intake,
                                                ShooterMode.TARMAC));

                this.setName("[4] Two Ball Tarmac Side");
        }
}
