package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbTraversal extends SequentialCommandGroup {
    private double liftArmHeight = ClimberConfig.liftArmHeight;
    private double rotateArmLength = ClimberConfig.rotateArmLength;
    private double angleToRung;
    private double distToRung;

    public ClimbTraversal(Climber climber) {
        angleToRung = Math.atan(ClimberConfig.horizDistBtwnRungs / (rotateArmLength + 15.375));
        distToRung = Math.sqrt(Math.pow(ClimberConfig.horizDistBtwnRungs, 2) + Math.pow(rotateArmLength + 15.375, 2));

        addCommands(
            new LiftOut(climber, (61 - liftArmHeight)),
            // wait for the robot to move forward a bit
            new LiftIn(climber, (61 - liftArmHeight - rotateArmLength)),
            // assuming the rotate arms start parallel to the floor against the back of the robot
            new RotateBack(climber, 90),
            // robot is now suspended from mid rung, supported by both arms
            new ClimbRung(climber, distToRung, angleToRung),
            // wait for the robot to stop swinging, probably need a separate trigger for this
            new RotateBack(climber, angleToRung),
            new LiftIn(climber, distToRung),
            new RotateBack(climber, 10),
            // robot is now suspended from high rung, supported by both arms
            new ClimbRung(climber, distToRung, angleToRung)
        );

        addRequirements(climber);
    }
}
