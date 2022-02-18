package com.pigmice.frc.robot.commands.shooter;

import com.pigmice.frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShootBallCommand extends SequentialCommandGroup {
    /**
     * Spins flywheels up to speed, feeds a ball to shoot, and waits for the ball to
     * be shot.
     */
    public ShootBallCommand(double distance, Shooter shooter) {
        super(
                new SpinUpFlywheelsCommand(distance, shooter),
                new FeedBallCommand(),
                new WaitForReleaseCommand());
    }
}
