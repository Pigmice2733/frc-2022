package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Vision;
import frc.robot.subsystems.Drivetrain;

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
        Vision.stop();
        drivetrain.arcadeDrive(0.0, 0.0);
    }
}
