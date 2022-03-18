package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SpinUpFlywheelsCommand extends CommandBase {
    private double topSpeed = 0.0;
    private double bottomSpeed = 0.0;
    private Shooter shooter;

    public SpinUpFlywheelsCommand(Shooter shooter) {
        this.shooter = shooter;

        addRequirements(shooter);
    }

    public SpinUpFlywheelsCommand(Shooter shooter, double speed) {
        this.shooter = shooter;
        this.topSpeed = speed;
        this.bottomSpeed = speed;

        addRequirements(shooter);
    }

    public SpinUpFlywheelsCommand(Shooter shooter, double topSpeed, double bottomSpeed) {
        this.shooter = shooter;
        this.topSpeed = topSpeed;
        this.bottomSpeed = bottomSpeed;

        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setEnabled(true);
        // TODO calculate speeds based on distance
        if (this.topSpeed == 0 && this.bottomSpeed == 0) {
            this.shooter.setTargetSpeeds(950, 700);
        } else {
            this.shooter.setTargetSpeeds(this.topSpeed, this.bottomSpeed);
        }
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return this.shooter.isAtTargetVelocity();
    }
}
