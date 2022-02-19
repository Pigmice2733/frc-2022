package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.XboxController;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;

public class Controls {
    XboxController driver;
    XboxController operator;

    private double epsilon = DrivetrainConfig.driveEpsilon;

    // Create a new Controls
    public Controls(XboxController driver, XboxController operator) {
        this.driver = driver;
        this.operator = operator;
    }

    public double getDriveSpeed() {
        double driveValue = -driver.getLeftY();
        if (Utils.almostEquals(driveValue, 0, epsilon)) {
            driveValue = 0;
        }
        return driveValue * DrivetrainConfig.driveSpeed;
    }

    public double getTurnSpeed() {
        double turnValue = driver.getRightX();
        if (Utils.almostEquals(turnValue, 0, epsilon)) {
            turnValue = 0;
        }
        return turnValue * DrivetrainConfig.turnSpeed;
    }

    public double getLeftSpeed() {
        double driveValue = -driver.getLeftY();
        if (Utils.almostEquals(driveValue, 0, epsilon)) {
            driveValue = 0;
        }
        return driveValue * DrivetrainConfig.driveSpeed;
    }

    public double getRightSpeed() {
        double driveValue = -driver.getRightY();
        if (Utils.almostEquals(driveValue, 0, epsilon)) {
            driveValue = 0;
        }
        return driveValue * DrivetrainConfig.driveSpeed;
    }
}

/*
 * // TODO check with drive team to confirm these
 * 
 * DRIVER
 * arcade drive - left stick Y: drive speed; right stick X: turn speed
 * tank drive - left stick Y: left speed; right stick Y: right speed
 * X button: boost on
 * Y button: boost off
 * A button: slow on
 * B button: slow off
 * 
 * OPERATOR
 * A button: initiate climber sequence to traversal rung
 * B button: initiate climber sequence to high rung
 * X button: climber trigger (if needed)
 * Y button: shoot ball
 * left stick button: toggle drivetrain
 * pad up button: toggle shooter
 * pad down button: toggle intake
 * pad left button: toggle lights
 * pad right button: toggle climber
 * LT and RT all the way: disable all systems
 */