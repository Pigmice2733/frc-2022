package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.subsystems.Shooter;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterModes;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class StartShooterCommand extends CommandBase {
    private double distance;
    private Shooter shooter;
    private ShooterModes mode;

    public StartShooterCommand(Shooter shooter, ShooterModes mode, double distance) {
        this.distance = distance;
        this.shooter = shooter;
        this.mode = mode;

        addRequirements(shooter);
    }

    public StartShooterCommand(Shooter shooter, ShooterModes mode) {
        this(shooter, mode, 0.0d);
    }

    public StartShooterCommand(Shooter shooter, double distance) {
        this(shooter, ShooterModes.AUTO, distance);
    }

    @Override
    public void initialize() {
        this.shooter.setMode(mode);
        if (mode == ShooterModes.AUTO) {
            this.shooter.setSpeedsByDistance(distance);
        } else {
            this.shooter.setTargetSpeeds(mode.getTopRPM(), mode.getBottomRPM());
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
