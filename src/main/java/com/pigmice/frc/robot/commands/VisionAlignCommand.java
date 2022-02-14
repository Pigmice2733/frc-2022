package com.pigmice.frc.robot.commands;

import com.pigmice.frc.robot.Vision;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class VisionAlignCommand extends CommandBase {
    private final Drivetrain drivetrain;
    private double startTime;
    private final double timeLimit;

    public VisionAlignCommand(Drivetrain drivetrain, double timeLimit) {
        this.drivetrain = drivetrain;
        this.timeLimit = timeLimit;

        this.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        Vision.update();
        super.initialize();
    }

    @Override
    public void execute() {
        Vision.update();

        double output = Vision.getRotationOutput();
        drivetrain.arcadeDrive(0.0, output);
    }

    @Override
    public boolean isFinished() {
        return (Timer.getFPGATimestamp() - startTime > timeLimit || Vision.alignmentError() < 1);
    }

    @Override
    public void end(boolean interrupted) {
        Vision.stopAligning();
        drivetrain.arcadeDrive(0.0, 0.0);
    }
}
