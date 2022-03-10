package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbRung extends SequentialCommandGroup {
    public ClimbRung(Climber climber) {
        addCommands(
                // robot is suspended from lower rung, supported by both arms
                new LiftOffBar(climber),
                new ParallelRaceGroup(new LiftOffBar(climber, true), new RotateToTarget(climber)),
                new ParallelRaceGroup(new RotateToTarget(climber, true), new LiftExtendFully(climber)),
                new ParallelRaceGroup(new LiftExtendFully(climber, true), new RotateToHook(climber)),
                new ParallelRaceGroup(new RotateToHook(climber, true), new LiftHalfway(climber)),
                new ParallelRaceGroup(new LiftHalfway(climber, true), new RotateAway(climber)),
                new ParallelRaceGroup(new RotateAway(climber, true), new LiftRetractFully(climber)),
                new ParallelRaceGroup(new LiftRetractFully(climber, true), new RotateToVertical(climber))
        // the robot will swing and come to rest directly below the higher rung,
        // supported by lift arms
        );

        addRequirements(climber);
    }
}
