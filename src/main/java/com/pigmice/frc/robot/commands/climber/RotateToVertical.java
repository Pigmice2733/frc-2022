package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RotateToVertical extends ParallelCommandGroup {
    private boolean infinite;

    public RotateToVertical(Rotato rotato) {
        this(rotato, false);
    }

    public RotateToVertical(Rotato rotato, boolean infinite) {
        super(new RotateOneTo(rotato.getLeft(), 0.0, infinite),
                new RotateOneTo(rotato.getRight(), 0.0, infinite));
        this.infinite = infinite;

        addRequirements(rotato.getLeft(), rotato.getRight());
    }

    @Override
    public boolean isFinished() {
        if (infinite)
            return false;

        return super.isFinished();
    }
}