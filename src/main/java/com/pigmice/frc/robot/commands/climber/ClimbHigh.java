package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbHigh extends SequentialCommandGroup {

    public ClimbHigh(Climber climber) {

        addCommands(
                new LiftExtendFully(climber),
                new ParallelRaceGroup(new LiftExtendFully(climber, true), new RotateAway(climber)),
                new ParallelRaceGroup(new RotateAway(climber, true), new LiftRetractFully(climber)),
                new ParallelRaceGroup(new LiftRetractFully(climber, true), new RotateToVertical(climber)),
                new ParallelRaceGroup(new RotateToVertical(climber, true), new ClimbRung(climber)));

        addRequirements(climber);
    }
}
