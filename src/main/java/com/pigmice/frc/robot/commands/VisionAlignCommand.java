package com.pigmice.frc.robot.commands;

import com.pigmice.frc.robot.Constants.VisionConfig;
import com.pigmice.frc.robot.Vision;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class VisionAlignCommand extends CommandBase {
    private final Drivetrain drivetrain;

    public VisionAlignCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        Vision.startAligning();
        Vision.update();

        super.initialize();
    }

    @Override
    public void execute() {
        if (Vision.hasTarget()) {
            double output = Vision.getOutput();
            if (Vision.getTargetYaw() == Double.NaN || output == Double.NaN)
                return;
            System.out.printf("VISION OUTPUT: %.8f | YAW: %.8f\n", output, Vision.getTargetYaw());
            this.drivetrain.arcadeDrive(0, output);
        }

        super.execute();
    }

    @Override
    public boolean isFinished() {
        double yaw = Vision.getTargetYaw();
        if (yaw != Double.NaN && Math.abs(yaw) < VisionConfig.tolerableError) {
            Vision.stopAligning();
            this.drivetrain.arcadeDrive(0.0, 0.0);
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Vision.stopAligning();
        drivetrain.arcadeDrive(0.0, 0.0);
    }
}
