package com.pigmice.frc.robot.commands;

import com.pigmice.frc.robot.Constants.VisionConfig;
import com.pigmice.frc.robot.Controls;
import com.pigmice.frc.robot.Vision;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class VisionAlignCommand extends CommandBase {
    private final Drivetrain drivetrain;
    private final XboxController driver;
    private final XboxController operator;

    public VisionAlignCommand(Drivetrain drivetrain) {
        this(drivetrain, null, null);
    }

    public VisionAlignCommand(Drivetrain drivetrain, XboxController driver, XboxController operator) {
        this.drivetrain = drivetrain;
        this.driver = driver;
        this.operator = operator;

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
        // TODO maybe make it so that the command only outputs to the drivetrain when A
        // is pressed
        System.out.println("VISION ALIGNMENT!");
        if (Vision.hasTarget()) {
            double output = Vision.getOutput();
            double targetYaw = Vision.getTargetYaw();
            if (targetYaw == Double.NaN || output == Double.NaN)
                return;

            System.out.println("WITH OUTPUT " + output + " | TARGET YAW: " + targetYaw);
            /*
             * if output is somehow Double.NaN, it might be unsigned and then will remain
             * positive when negated
             * this could be why the robot drives straight backwards instead of spinning
             */
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
            if (driver != null) {
                Controls.rumbleController(driver);
            }
            if (operator != null) {
                Controls.rumbleController(operator, 0.25);
            }
            return true;
        }

        return false;
    }

    public void stop() {
        Vision.stopAligning();
        drivetrain.arcadeDrive(0, 0);
        drivetrain.stop();
    }

    @Override
    public void end(boolean interrupted) {
        Vision.stopAligning();
        drivetrain.arcadeDrive(0.0, 0.0);
        super.end(interrupted);
    }
}
