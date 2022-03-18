package com.pigmice.frc.robot.commands;

import com.pigmice.frc.robot.Constants.VisionConfig;
import com.pigmice.frc.robot.Vision;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class VisionAlignCommand extends ProfiledPIDCommand {
    private final Drivetrain drivetrain;

    public VisionAlignCommand(Drivetrain drivetrain) {
        super(new ProfiledPIDController(VisionConfig.rotationP, VisionConfig.rotationI, VisionConfig.rotationD,
                new TrapezoidProfile.Constraints(0.5, 0.5)),
                Vision::getTargetYaw,
                0.0,
                (output, setpoint) -> drivetrain.arcadeDrive(0.0, output),
                drivetrain);
        this.drivetrain = drivetrain;

        this.addRequirements(drivetrain);

        getController().setTolerance(VisionConfig.tolerableError,
                VisionConfig.tolerableEndVelocity);
    }

    @Override
    public void initialize() {
        Vision.update();
        super.initialize();
    }

    @Override
    public boolean isFinished() {
        // System.out.println(
        // "VISION ANGLE: " + Vision.getBestTarget().getYaw() + " | GOAL: "
        // + this.getController().getGoal().position + " | ERROR: "
        // + this.getController().getPositionError() + " | AT GOAL: " +
        // this.getController().atGoal());
        return this.getController().atGoal();
    }

    @Override
    public void end(boolean interrupted) {
        Vision.stopAligning();
        drivetrain.arcadeDrive(0.0, 0.0);
    }
}
