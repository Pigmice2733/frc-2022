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
        angleToRung = Math.atan(ClimberConfig.horizDistBtwnRungs / (rotateArmLength + ClimberConfig.vertDistBtwnRungs));
        distToRung = Math.sqrt(Math.pow(ClimberConfig.horizDistBtwnRungs, 2)
                + Math.pow(rotateArmLength + ClimberConfig.vertDistBtwnRungs, 2));

        addCommands(
                new ClimbHigh(climber),
                // wait for the robot to stop swinging, probably need a separate trigger for
                // this (?)
                new RotateBack(climber, angleToRung),
                new LiftIn(climber, distToRung),
                new RotateBack(climber, 10),
                // robot is now suspended from high rung, supported by both arms
                new ClimbRung(climber, distToRung, angleToRung));

        addRequirements(climber);
    }
}
