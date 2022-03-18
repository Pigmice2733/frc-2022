package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.subsystems.climber.Rotato;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class RotateApplyOutput extends CommandBase {
    private Rotato rotato;
    private double output;

    public RotateApplyOutput(Rotato rotato, double output) {
        this.rotato = rotato;
        this.output = output;

        addRequirements(rotato.getLeft(), rotato.getRight());
    }

    @Override
    public void execute() {
        this.rotato.setLeftOutput(output);
        this.rotato.setRightOutput(output);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}