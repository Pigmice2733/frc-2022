package com.pigmice.frc.robot.commands.indexer;

import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.shooter.SpinUpFlywheelsCommand;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class EjectBallCommand extends SequentialCommandGroup {
    public EjectBallCommand(Indexer indexer, Shooter shooter, Intake intake) {
        super(
                new InstantCommand(() -> {
                    intake.disable();
                    indexer.setMode(IndexerMode.SHOOT);
                    indexer.setMotorOutput(0.0);
                }),
                new SpinUpFlywheelsCommand(shooter, ShooterMode.EJECT),
                new WaitUntilCommand(() -> shooter.didJustShoot()).withTimeout(1.0),
                new WaitCommand(0.25),
                new InstantCommand(() -> {
                    intake.enable();
                    indexer.setMode(IndexerMode.FREE_SPIN);
                    shooter.setMode(ShooterMode.AUTO);
                }));
    }

}
