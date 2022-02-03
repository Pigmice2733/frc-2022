package com.pigmice.frc.robot.commands.climber;

import javax.management.InstanceAlreadyExistsException;

import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbHangar extends SequentialCommandGroup {
    public ClimbHangar(Climber climber) {
        addCommands(
            
        );
    }
}
