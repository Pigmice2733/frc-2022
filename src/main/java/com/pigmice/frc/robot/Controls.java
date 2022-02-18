package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import com.pigmice.frc.robot.Constants.DrivetrainConfig;

public class Controls {
    XboxController driver;
    XboxController operator;

    // Create a new Controls
    public Controls(XboxController driver, XboxController operator) {
        this.driver = driver;
        this.operator = operator;
    }

    public double getDriveSpeed() {
        double driveValue = driver.getLeftY();
        driveValue = Math.abs(driveValue) > 0.2 ? driveValue : 0;
        return -driveValue * DrivetrainConfig.driveSpeed;
    }

    public double getTurnSpeed() {
        double turnValue = driver.getRightX();
        turnValue = Math.abs(turnValue) > 0.2 ? turnValue : 0;
        return turnValue * DrivetrainConfig.turnSpeed;
    }

    public double getLeftYAxis() {
        double turnValue = driver.getLeftY();
        turnValue = Math.abs(turnValue) > 0.2 ? turnValue : 0;
        return turnValue * DrivetrainConfig.driveSpeed;
    }

    public double getRightYAxis() {
        double turnValue = driver.getRightY();
        turnValue = Math.abs(turnValue) > 0.2 ? turnValue : 0;
        return turnValue * DrivetrainConfig.driveSpeed;
    }
}