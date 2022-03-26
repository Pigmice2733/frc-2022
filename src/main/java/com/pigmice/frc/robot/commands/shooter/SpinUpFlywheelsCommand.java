package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SpinUpFlywheelsCommand extends CommandBase {
    private Shooter shooter;
    private ShooterMode mode;

    public SpinUpFlywheelsCommand(Shooter shooter) {
        this(shooter, ShooterMode.AUTO);
    }

    public SpinUpFlywheelsCommand(Shooter shooter, ShooterMode mode) {
        this.shooter = shooter;
        this.mode = mode;

        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooter.setEnabled(true);
        // TODO calculate speeds based on distance
        this.shooter.setMode(this.mode);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return this.shooter.isAtTargetVelocity();
    }
}
