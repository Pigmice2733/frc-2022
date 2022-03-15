package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.climber.Lifty;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class LiftApplyOutput extends CommandBase {
    private Lifty lifty;
    private double output;

    public LiftApplyOutput(Lifty lifty, double output) {
        this.lifty = lifty;
        this.output = output;

        addRequirements(lifty.getLeft(), lifty.getRight());
    }

    @Override
    public void execute() {
        this.lifty.setLeftOutput(output);
        this.lifty.setRightOutput(output);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
