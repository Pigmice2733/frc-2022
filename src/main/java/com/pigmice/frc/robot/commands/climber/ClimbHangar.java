package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbHangar extends SequentialCommandGroup {
    private double liftArmHeight = ClimberConfig.liftArmHeight;
    private double rotateArmLength = ClimberConfig.rotateArmLength;
    

    public ClimbHangar(Climber climber) {
        addCommands(
            new LiftOut(climber, (61 - liftArmHeight)),
            // wait for the robot to move forward a bit
            new LiftIn(climber, (61 - liftArmHeight - rotateArmLength)),
            new RotateBack(climber, 90), // assuming the rotate arms start parallel to the floor against the back of the robot
            new LiftOut(climber, 1), // robot is now suspended from mid rung, supported by rotate arms
            new RotateBack(climber, 10),
            new LiftIn(climber, 2.5),
            new RotateFront(climber, 25),
            new LiftOut(climber, 24),
            new RotateFront(climber, (Math.atan(ClimberConfig.horizDistBtwnRungs / (rotateArmLength + 15.375)) - 15)),
            new LiftIn(climber, (Math.sqrt((21735/64) + Math.pow(rotateArmLength + 15.375, 2)) - 24)),
            new RotateFront(climber, 10), // the robot will swing and come to rest directly below the high rung, supported by lift arms
            // wait for the robot to stop swinging, probably need a separate trigger for this
            new RotateBack(climber, (Math.atan(ClimberConfig.horizDistBtwnRungs / (rotateArmLength + 15.375)) - 20)),
            new LiftIn(climber, (Math.sqrt((21735/64) + Math.pow(rotateArmLength + 15.375, 2)) - 1.5)),
            new RotateBack(climber, 10),
            new LiftOut(climber, 1), // robot is now suspended from high rung, supported by rotate arms
            new RotateBack(climber, 10),
            new LiftIn(climber, 2.5),
            new RotateFront(climber, 25),
            new LiftOut(climber, 24),
            new RotateFront(climber, (Math.atan(ClimberConfig.horizDistBtwnRungs / (rotateArmLength + 15.375)) - 15)),
            new LiftIn(climber, (Math.sqrt((21735/64) + Math.pow(rotateArmLength + 15.375, 2)) - 24)),
            new RotateFront(climber, 10) // the robot will swing and come to rest directly below the high rung, supported by lift arms
        );

        addRequirements(climber);
    }
}
