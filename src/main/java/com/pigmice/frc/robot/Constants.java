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
        public static final int topMotorPort = 10;
        public static final int bottomMotorPort = 9;

        public static final double topMotorSpeed = 0.62;
        public static final double bottomMotorSpeed = 0.62;
    }

    public static class ClimberConfig {
        public static final int liftRightPort = 5;
        public static final int liftLeftPort = 6;
        public static final int rotateRightPort = 7;
        public static final int rotateLeftPort = 8;

        // both of these in rpm
        public static final double maxLiftMotorSpeed = 600;
        public static final double defaultRotateMotorSpeed = 600;
        public static final double defaultLiftMotorSpeed = 100;

        // conversion rate: rotations of lift motor -> lift distance of arm in inches
        public static final double liftConversion = 1.;

        // conversion rate: rotations of rotate motor -> rotations of rotate arm
        public static final double rotateConversion = 1.;

        // both in inches
        public static final double liftArmHeight = 52; // height of base of lift arms
        public static final double rotateArmLength = 8.25; // at straight vertical, height above liftArmHeight

        // both in inches
        public static final double horizDistBtwnRungs = 24;
        public static final double vertDistBtwnRungs = 15.375;

        // both in inches
        public static final double minLiftHeight = 0;
        public static final double maxLiftHeight = 24.5;

        // both in degrees
        public static final double minRotateAngle = -45.0;
        public static final double maxRotateAngle = 45.0;

        public static final double angleToRung = 24.62;
    }

    public static class ClimberProfileConfig {
        public static final double maxLiftVelocity = 5.0;
        public static final double maxLiftAcceleration = 10.0;

        public static final double maxRotateVelocity = 50.0;
        public static final double maxRotateAcceleration = 50.0;

        public static final double liftP = 0.08;
        public static final double liftI = 0.005;
        public static final double liftD = 0.;

        public static final double rotateP = 0.002;
        public static final double rotateI = 0.001;
        public static final double rotateD = 0.;

        public static final double liftTolerableError = 0.2;
        public static final double liftTolerableEndVelocity = 0.1;

        public static final double angleTolerableError = 3.0;
        public static final double angleTolerableEndVelocity = 1.0;

        /*
         * public static final double feedforwardStatic = 3;
         * public static final double feedforwardVelocity = 3;
         * public static final double feedforwardAcceleration = 0;
         */
    }

    public static class DrivetrainConfig {
        public static final int frontRightMotorPort = 3;
        public static final int frontLeftMotorPort = 1;
        public static final int backRightMotorPort = 4;
        public static final int backLeftMotorPort = 2;

        public static final double driveSpeed = 1.0;
        public static final double turnSpeed = 0.6;
        public static final SPI.Port navxPort = SPI.Port.kMXP;

        public static final double wheelDiameterMeters = Units.inchesToMeters(4.125);
        public static final double gearRatio = 7.56; // 3 motor rotations to 1 wheel rotation
        public static final double rotationToDistanceConversion = (Math.PI * wheelDiameterMeters) / gearRatio; //
        // circumference
        // / gear ratio
        public static final double wheelBase = 0.69;

        public static final int navXRotationalOffsetDegrees = 0;

        public static final double driveThreshold = 0.2;
        public static final double boostMultiplier = 1.25;
        public static final double slowMultiplier = 0.5;

    }

    public static class IntakeConfig {
        public static final int intakeBottomPort = 0;
        public static final int intakeTopPort = 1;

        public static final double intakeSpeed = 0.75;

        public static final double extendP = 0.01;
        public static final double extendI = 0.0;
        public static final double extendD = 0.0;

        public static final double maxExtendVelocity = 5.0;
        public static final double maxExtendAcceleration = 1.8;

        public static final double maxExtendAngle = 84.0;
        public static final double extendTolError = 1.0;
        public static final double extendTolEndVelo = 0.1;
    }

    public static class IndexerConfig {
        public static final int motorPort = 11;
        public static final boolean motorInverted = false;
        public static final double gearRatio = 1;

        public static final double kP = 0.01;
        public static final double kI = 0.0015;
        public static final double kD = 0;

        public static final double freeSpin_kP = 0.2;
        public static final double freeSpin_kS = 0;
        public static final double freeSpinP_kV = 0.5;

        public static final double maxRotateVelocity = 50.0;
        public static final double maxRotateAcceleration = 50000.0;

        public static final double angleTolerableError = 3.0;
        public static final double angleTolerableEndVelocity = 1.0;

        public static final double velocityThreshold = 100;

        public static final double infraredThreshold = 20.0;
    }

    public static class VisionConfig {
        public static final double cameraHeightMeters = 0.8;
        public static final double targetHeightMeters = 2.38; // Units.inchesToMeters(104); // 8'8"
        public static final double cameraPitchRadians = 0;
        public static final double goalRangeMeters = 0.0;

        public static final double rotationP = 0.001;
        public static final double rotationI = 0.0;
        public static final double rotationD = 0.0;

        public static final double tolerableError = 0.1;
        public static final double tolerableEndVelocity = 0.1;
    }

    public static final int driverControllerPort = 0;
    public static final int operatorControllerPort = 1;
    public static final int operatorPadPort = 2;
}