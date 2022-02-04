package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import com.pigmice.frc.robot.Constants.DrivetrainConfig;

public class Controls {
    XboxController driver;

    // Create a new Controls
    public Controls(XboxController driver) {
        this.driver = driver;
    }

    public double getDriveSpeed() {
        return driver.getRawAxis(1) * DrivetrainConfig.driveSpeed;
    }

    public double getTurnSpeed() {
        return driver.getRawAxis(0) * DrivetrainConfig.turnSpeed;
    }
}