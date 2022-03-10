package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbTraversal extends SequentialCommandGroup {

    public ClimbTraversal(Climber climber) {

        addCommands(
                new ClimbHigh(climber),
                new ClimbRung(climber));

        addRequirements(climber);
    }
}
