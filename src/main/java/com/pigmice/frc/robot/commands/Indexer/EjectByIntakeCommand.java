package com.pigmice.frc.robot.commands.indexer;

import com.pigmice.frc.robot.Constants.IndexerConfig.IndexerMode;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class EjectByIntakeCommand extends SequentialCommandGroup {

    public EjectByIntakeCommand(Indexer indexer, Intake intake) {
        super(
                new InstantCommand(() -> {
                    indexer.setMode(IndexerMode.EJECT_BY_INTAKE);
                    intake.setReverse(true);
                }),
                new WaitUntilCommand(indexer.getBallDetector()::isSameAlliance)
                        .withTimeout(5.0),
                new InstantCommand(() -> {
                    intake.setReverse(false);
                    indexer.setLookingForBalls(false);
                    indexer.setMode(IndexerMode.FREE_SPIN);
                }),
                new WaitCommand(0.5),
                new InstantCommand(() -> {
                    indexer.setLookingForBalls(true);
                }));
    }
}
