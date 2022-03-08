// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.SPI;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e., public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class ShooterConfig {
        public static final int topMotorPort = 8;
        public static final int bottomMotorPort = 7;

        public static final double topMotorSpeed = 0.62;
        public static final double bottomMotorSpeed = 0.62;
    }

    public static class ClimberConfig {
        public static final int liftLeadPort = 7;
        public static final int liftFollowPort = 8;
        public static final int rotateLeadPort = 0;
        public static final int rotateFollowPort = 0;

        // both of these in rpm
        public static final double maxLiftMotorSpeed = 600;
        public static final double defaultRotateMotorSpeed = 600;

        // radius of gear in contact with motor and lifting arm, in inches
        public static final double liftMotorRadius = 0.5;

        // both in inches
        public static final double liftArmHeight = 52; // height of base of lift arms
        public static final double rotateArmLength = 8.25; // at straight vertical, height above liftArmHeight

        // both in inches
        public static final double horizDistBtwnRungs = 24;
        public static final double vertDistBtwnRungs = 15.375;

        // both in inches
        public static final double minLiftHeight = -3;
        public static final double maxLiftHeight = 32;
        
        // both in degrees
        public static final double minRotateAngle = -90;
        public static final double maxRotateAngle = 90;
    }

    public static class ClimberProfileConfig {
        public static final double maxLiftVelocity = 1;
        public static final double maxLiftAcceleration = 0.4;

        public static final double maxRotateVelocity = 1.375;
        public static final double maxRotateAcceleration = 0.4;

        public static final double liftP = 1.;
        public static final double liftI = 0.;
        public static final double liftD = 0.;

        public static final double rotateP = 1.;
        public static final double rotateI = 0.;
        public static final double rotateD = 0.;

        public static final double tolerableError = 0.02;
        public static final double tolerableEndVelo = 0.01;

        /* public static final double feedforwardStatic = 3;
        public static final double feedforwardVelocity = 3;
        public static final double feedforwardAcceleration = 0; */
    }

    public static class DrivetrainConfig {
        public static final int frontRightMotorPort = 2;
        public static final int frontLeftMotorPort = 3;
        public static final int backRightMotorPort = 1;
        public static final int backLeftMotorPort = 4;

        public static final double driveSpeed = 0.8;
        public static final double turnSpeed = 0.6;
        public static final SPI.Port navxPort = SPI.Port.kMXP;

        public static final double wheelDiameterMeters = Units.inchesToMeters(4.125);
        public static final double gearRatio = 7.56; // 3 motor rotations to 1 wheel rotation
        public static final double rotationToDistanceConversion = (Math.PI * wheelDiameterMeters) / gearRatio; //
        // circumference
        // / gear ratio
        public static final double wheelBase = 0.69;

        public static final int navXRotationalOffsetDegrees = 0;

        public static final double driveEpsilon = 0.2;
        public static final double boostMultiplier = 1.25;
        public static final double slowMultiplier = 0.5;


    }

    public static class IntakeConfig {
        public static final int intakeBottomPort = 0;
        public static final int intakeTopPort = 1;

        public static final double intakeSpeed = 0.75;
    }

    public static final int driverControllerPort = 0;
    public static final int operatorControllerPort = 1;
    public static final int operatorPadPort = 2;
}
