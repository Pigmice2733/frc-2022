package com.pigmice.frc.robot.commands.indexer;

import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterMode;
import com.pigmice.frc.robot.commands.shooter.ShootBallWithModeCommand;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;

public class EjectBallCommand extends ShootBallWithModeCommand {
    public EjectBallCommand(Indexer indexer, Shooter shooter, Intake intake) {
        super(indexer, shooter, intake, ShooterMode.EJECT);
    }
}
