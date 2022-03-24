package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.indexer.SpinIndexerToAngle;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class ShootBallWithModeCommand extends SequentialCommandGroup {
    public ShootBallWithModeCommand(Indexer indexer, Shooter shooter, Intake intake, ShooterMode shooterMode) {
        super(new InstantCommand(() ->

        {
            intake.disable();
            indexer.setMode(IndexerMode.ANGLE);
            indexer.stopMotor();
        }), new ParallelCommandGroup(new SpinIndexerToAngle(indexer, -5.0, false),
                new SpinUpFlywheelsCommand(shooter, shooterMode)), new InstantCommand(() -> {
                    indexer.setMode(IndexerMode.SHOOT);
                }), new WaitUntilCommand(() -> shooter.didJustShoot()).withTimeout(1.0),
                new WaitUntilCommand(() -> shooter.didJustShoot()).withTimeout(0.5), new WaitCommand(0.25),
                new InstantCommand(() -> {
                    shooter.setMode(ShooterMode.OFF);
                    intake.enable();
                    indexer.setMode(IndexerMode.FREE_SPIN);
                }));
    }

}
