package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.Lifty;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

/**
 * Lifts lift arm off of bar when suspended by both arms so that it's then only
 * suspended by the rotating one.
 * 
 * @param climber Climber subsystem
 */
public class LiftOffBar extends ParallelCommandGroup {
    public LiftOffBar(Lifty lifty) {
        this(lifty, false);
    }

    public LiftOffBar(Lifty lifty, boolean infinite) {
        super(new LiftOneTo(lifty.getLeft(), 6.0, infinite),
                new LiftOneTo(lifty.getRight(), 6.0, infinite));

        addRequirements(lifty.getLeft(), lifty.getRight());
    }
}
