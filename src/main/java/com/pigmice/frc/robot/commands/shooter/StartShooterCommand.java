package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class StartShooterCommand extends CommandBase {
    private Shooter shooter;
    private ShooterMode mode;

    public StartShooterCommand(Shooter shooter, ShooterMode mode) {
        this.shooter = shooter;
        this.mode = mode;

        System.out.println("SETTING SHOOTER MODE TO " + mode);

        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setMode(mode);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return this.shooter.isAtTargetVelocity();
    }
}
