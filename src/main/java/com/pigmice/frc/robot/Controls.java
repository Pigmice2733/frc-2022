package com.pigmice.frc.robot;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Controls {
    XboxController driver;
    XboxController operator;

    private double threshold = DrivetrainConfig.axisThreshold;

    // Create a new Controls
    public Controls(XboxController driver, XboxController operator) {
        this.driver = driver;
        this.operator = operator;
    }

    public double getDriveSpeed() {
        double driveValue = -driver.getLeftY();
        if (Utils.almostEquals(driveValue, 0, threshold)) {
            driveValue = 0;
        }
        return driveValue * DrivetrainConfig.driveSpeed;
    }

    public double getTurnSpeed() {
        double turnValue = driver.getRightX();
        if (Utils.almostEquals(turnValue, 0, threshold)) {
            turnValue = 0;
        }
        return -turnValue * DrivetrainConfig.turnSpeed;
    }

    public double getLeftSpeed() {
        double driveValue = -driver.getLeftY();
        if (Utils.almostEquals(driveValue, 0, threshold)) {
            driveValue = 0;
        }
        return driveValue * DrivetrainConfig.driveSpeed;
    }

    public double getRightSpeed() {
        double driveValue = -driver.getRightY();
        if (Utils.almostEquals(driveValue, 0, threshold)) {
            driveValue = 0;
        }
        return driveValue * DrivetrainConfig.driveSpeed;
    }

    public static void setControllerRumble(XboxController controller, double rumble) {
        setControllerRumble(controller, rumble, rumble);
    }

    public static void setControllerRumble(XboxController controller, double left, double right) {
        controller.setRumble(RumbleType.kLeftRumble, left);
        controller.setRumble(RumbleType.kRightRumble, right);
    }

    public static void resetControllerRumble(XboxController controller) {
        setControllerRumble(controller, 0);
    }

    public static SequentialCommandGroup getRumbleCommand(XboxController controller, double rumble, double duration) {
        return getRumbleCommand(controller, rumble, rumble, duration);
    }

    public static SequentialCommandGroup getRumbleCommand(XboxController controller, double left, double right,
            double duration) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> setControllerRumble(controller, left, right)),
                new WaitCommand(duration),
                new InstantCommand(() -> resetControllerRumble(controller)));
    }

    public static void rumbleController(XboxController controller) {
        rumbleController(controller, 0.5);
    }

    public static void rumbleController(XboxController controller, double rumble) {
        rumbleController(controller, rumble, 0.75);
    }

    public static void rumbleController(XboxController controller, double rumble, double duration) {
        rumbleController(controller, rumble, rumble, duration);
    }

    public static void rumbleController(XboxController controller, double left, double right, double duration) {
        CommandScheduler.getInstance().schedule(getRumbleCommand(controller, left, right, duration));
    }
}