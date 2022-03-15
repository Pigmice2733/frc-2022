package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

/**
 * Rotate the rotating hook out of the way for the upwards climb.
 */

public class RotateAway extends ParallelCommandGroup {
    public RotateAway(Rotato rotato) {
        this(rotato, false);
    }

    public RotateAway(Rotato rotato, boolean infinite) {
        super(new RotateOneTo(rotato.getLeft(), -20.0, infinite),
                new RotateOneTo(rotato.getRight(), -20.0, infinite));

        addRequirements(rotato.getLeft(), rotato.getRight());
    }
}
