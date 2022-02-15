package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbRung extends SequentialCommandGroup {
    // private double liftArmHeight = ClimberConfig.liftArmHeight;
    private double rotateArmLength = ClimberConfig.rotateArmLength;
    
    public ClimbRung(Climber climber, double distance, double angle) {
        addCommands(
            // robot is suspended from lower rung, supported by both arms
            new LiftOut(climber, 2),
            new RotateBack(climber, 10),
            new LiftIn(climber, 4),
            new RotateFront(climber, 25),
            new LiftOut(climber, distance + 4 - rotateArmLength),
            new RotateFront(climber, angle - 15),
            new LiftIn(climber, 2),
            new RotateFront(climber, 10)
            // the robot will swing and come to rest directly below the higher rung, supported by lift arms
        );

        addRequirements(climber);
    }
}
