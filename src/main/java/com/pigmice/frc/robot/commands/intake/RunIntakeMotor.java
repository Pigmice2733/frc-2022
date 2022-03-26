package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class RunIntakeMotor extends CommandBase {
    private final Intake intake;

    public RunIntakeMotor(Intake intake) {
        this.intake = intake;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        this.intake.setExtended(true);
    }

    @Override
    public void end(boolean interrupted) {
        this.intake.setFullyExtended(false);
    }
}
