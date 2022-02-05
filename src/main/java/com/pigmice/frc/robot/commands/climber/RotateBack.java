package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class RotateBack extends CommandBase {
    private Climber climber;
    private double angle; // in degrees
    private double countAngle;

    public RotateBack(Climber climber, double angle) {
        this.climber = climber;
        this.angle = angle;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        countAngle = 0d;
    }

    @Override
    public void execute() {
        climber.rotateForward();
        countAngle += ClimberConfig.rotateMotorSpeed * 360 / 3000;
    }

    @Override
    public boolean isFinished() {
        return (countAngle >= angle);
    }
}
