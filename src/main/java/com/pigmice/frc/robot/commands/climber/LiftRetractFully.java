package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.climber.Lifty;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class LiftRetractFully extends ParallelCommandGroup {

    private boolean infinite;

    public LiftRetractFully(Lifty lifty) {
        this(lifty, false);
    }

    public LiftRetractFully(Lifty lifty, boolean infinite) {
        super(new LiftOneTo(lifty.getLeft(), ClimberConfig.minLiftHeight, infinite),
                new LiftOneTo(lifty.getRight(), ClimberConfig.minLiftHeight, infinite));

        this.infinite = infinite;

        addRequirements(lifty.getLeft(), lifty.getRight());
    }

    @Override
    public boolean isFinished() {
        if (infinite)
            return false;

        return super.isFinished();
    }
}