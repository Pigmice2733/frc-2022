package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.shooter.ShootBallWithModeCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Auto2BallShootFirst extends SequentialCommandGroup {
    public Auto2BallShootFirst(Indexer indexer, Shooter shooter, Intake intake, Drivetrain drivetrain) {
        // start at fender
        super(
            new ShootBallWithModeCommand(indexer, shooter, intake, ShooterMode.FENDER_HIGH),
            new DriveDistance(drivetrain, 2.16 - DrivetrainConfig.robotLength),
            new TurnToAngle(-22.5, false, drivetrain),
            new Auto2BallFender(indexer, shooter, intake, drivetrain)
        );
    }
}
