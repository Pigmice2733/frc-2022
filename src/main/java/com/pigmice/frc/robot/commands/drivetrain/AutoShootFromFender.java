package com.pigmice.frc.robot.commands.drivetrain;

import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.shooter.ShootBallWithModeCommand;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoShootFromFender extends SequentialCommandGroup {
    public AutoShootFromFender(Indexer indexer, Shooter shooter, Intake intake) {
        super(new ShootBallWithModeCommand(indexer, shooter, intake, ShooterMode.FENDER_HIGH));

        this.setName("[2] Shoot From Fender");
    }
}
