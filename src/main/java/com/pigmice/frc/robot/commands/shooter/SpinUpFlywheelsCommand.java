package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SpinUpFlywheelsCommand extends CommandBase {
    private double distance;
    private Shooter shooter;

    public SpinUpFlywheelsCommand(double distance, Shooter shooter) {
        this.distance = distance;
        this.shooter = shooter;

        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setEnabled(true);
        // TODO calculate speeds based on distance
        this.shooter.setTargetSpeeds(-950, 700);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return this.shooter.isAtTargetVelocity();
    }
}
