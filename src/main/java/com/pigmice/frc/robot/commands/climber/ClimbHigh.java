package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ClimbHigh extends SequentialCommandGroup {
    private double liftArmHeight = ClimberConfig.liftArmHeight;
    private double rotateArmLength = ClimberConfig.rotateArmLength;
    private double angleToRung;
    private double distToRung;

    public ClimbHigh(Climber climber) {
        angleToRung = Math.atan(ClimberConfig.horizDistBtwnRungs / (rotateArmLength + 15.375));
        distToRung = Math.sqrt(Math.pow(ClimberConfig.horizDistBtwnRungs, 2) + Math.pow(rotateArmLength + 15.375, 2));

        addCommands(
            new LiftOut(climber, (61 - liftArmHeight)),
            // wait for the robot to move forward a bit
            new LiftIn(climber, (61 - liftArmHeight - rotateArmLength)),
            // assuming the rotate arms start parallel to the floor against the back of the robot
            new RotateBack(climber, 90),
            // robot is now suspended from mid rung, supported by both arms
            new ClimbRung(climber, distToRung, angleToRung)
        );

        addRequirements(climber);
    }
}
