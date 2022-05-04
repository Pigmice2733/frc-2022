package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.VisionAlignCommand;
import com.pigmice.frc.robot.commands.intake.ExtendIntake;
import com.pigmice.frc.robot.commands.intake.RetractIntake;
import com.pigmice.frc.robot.commands.shooter.ShootBallWithModeCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


public class Auto2BallTarmac extends SequentialCommandGroup {
    public Auto2BallTarmac(Indexer indexer, Shooter shooter, Intake intake, Drivetrain drivetrain) {
        // start at edge of tarmac
        super(
                new ParallelCommandGroup(
                    new ExtendIntake(intake),
                    new DriveDistance(drivetrain, 0.7366)
                ),
                new DriveDistance(drivetrain, 0.3048),
                new RetractIntake(intake, indexer),
                new DriveDistance(drivetrain, -1.0414),
                new TurnToAngle(22.5, false, drivetrain), // new VisionAlignCommand(drivetrain),
                new ShootBallWithModeCommand(indexer, shooter, intake, ShooterMode.TARMAC)
            );
    }
}
