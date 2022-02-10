// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.SPI.Port;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class ShooterConfig {
        public static final int topMotorPort = 8;
        public static final int bottomMotorPort = 7;

        public static final double topMotorSpeed = 0.62;
        public static final double bottomMotorSpeed = 0.62;
    }

    public static class DrivetrainConfig {
        public static final int frontRightMotorPort = 0;
        public static final int frontLeftMotorPort = 0;
        public static final int backRightMotorPort = 0;
        public static final int backLeftMotorPort = 0;
        

        public static final double driveSpeed = 1;
        public static final double turnSpeed = 1;
        public static Port navxPort; 
    }

    public static final int driverControllerPort = 0;
    public static final int operatorControllerPort = 1;
}