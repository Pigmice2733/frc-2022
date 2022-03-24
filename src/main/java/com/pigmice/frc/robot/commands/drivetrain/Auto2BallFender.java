package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.intake.ExtendIntake;
import com.pigmice.frc.robot.commands.intake.RetractIntake;
import com.pigmice.frc.robot.commands.shooter.ShootBallWithModeCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Auto2BallFender extends SequentialCommandGroup {
    public Auto2BallFender(Indexer indexer, Shooter shooter, Intake intake, Drivetrain drivetrain) {
        // start at edge of tarmac
        super(
            new ParallelCommandGroup(
                new ExtendIntake(intake),
                new DriveDistance(drivetrain, 29.0)
            ),
            new DriveDistance(drivetrain, 12.0),
            new RetractIntake(intake, indexer),
            new DriveDistance(drivetrain, -41.0),
            new TurnToAngle(20, false, drivetrain),
            new DriveDistance(drivetrain, -86.0),
            new ShootBallWithModeCommand(indexer, shooter, intake, ShooterMode.FENDER_HIGH)
        );
    }
}
