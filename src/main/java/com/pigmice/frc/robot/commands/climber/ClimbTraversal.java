package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.climber.Lifty;
import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbTraversal extends SequentialCommandGroup {

    public ClimbTraversal(Lifty lifty, Rotato rotato) {

        addCommands(
                new ClimbHigh(lifty, rotato),
                new ClimbRung(lifty, rotato));
    }
}
