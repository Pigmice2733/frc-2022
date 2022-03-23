package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.climber.Lifty;
import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbRung extends SequentialCommandGroup {
        public ClimbRung(Lifty lifty, Rotato rotato) {
                // addCommands(
                // new LiftOffBar(lifty),
                // new ParallelRaceGroup(new LiftOffBar(
                // lifty, true),
                // new RotateToTarget(
                // rotato)),
                // new ParallelRaceGroup(new RotateToTarget(
                // rotato, true),
                // new LiftExtendFully(
                // lifty)),
                // new ParallelRaceGroup(new LiftExtendFully(
                // lifty, true),
                // new RotateToHook(
                // rotato)),
                // new ParallelRaceGroup(new RotateToHook(
                // rotato, true),
                // new LiftHalfway(
                // lifty)),
                // new ParallelRaceGroup(new LiftHalfway(
                // lifty, true),
                // new RotateAway(
                // rotato)),
                // new ParallelRaceGroup(new RotateAway(
                // rotato, true),
                // new LiftRetractFully(
                // lifty)),
                // new ParallelRaceGroup(new LiftRetractFully(lifty, true), new
                // RotateToVertical(rotato)));
        }
}
