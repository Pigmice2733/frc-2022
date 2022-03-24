package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.climber.Lifty;
import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ClimbHigh extends SequentialCommandGroup {

    public ClimbHigh(Lifty lifty, Rotato rotato) {

        // addCommands(
        // new LiftExtendFully(lifty),
        // new ParallelRaceGroup(new LiftExtendFully(lifty, true), new
        // RotateAway(rotato)),
        // new ParallelRaceGroup(new RotateAway(rotato, true), new WaitCommand(2.0)),
        // // driver should drive forwards into the bar
        // new ParallelRaceGroup(new RotateAway(rotato, true), new
        // LiftRetractFully(lifty)),
        // new ParallelRaceGroup(new LiftRetractFully(lifty, true), new
        // RotateToVertical(rotato)),
        // new ParallelRaceGroup(new RotateToVertical(rotato, true), new
        // ClimbRung(lifty, rotato)));
    }
}
