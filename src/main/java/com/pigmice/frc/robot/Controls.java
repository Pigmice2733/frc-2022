package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.XboxController;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;

public class Controls {
    XboxController driver;
    XboxController operator;

    private double threshold = DrivetrainConfig.driveThreshold;

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
}

/*
 * DRIVER
 * arcade drive - left stick Y: drive speed; right stick X: turn speed
 * tank drive - left stick Y: left speed; right stick Y: right speed
 * X button: toggle boost
 * Y button: toggle slow - slow overrides boost
 * 
 * OPERATOR
 * RB button: lift arm up
 * LB button: lift arm down
 * A button: rotate forward
 * B button: rotate backward
 * X button: rotate forward slowly
 * Y button: rotate backward slowly
 * RStick button: extend intake
 * LStick button: retract intake
 */
