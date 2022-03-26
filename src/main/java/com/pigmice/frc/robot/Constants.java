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

    public static boolean debugMode = false;

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
        public static final double liftEndThreshold = 0.0; // meters?

        // both in degrees
        public static final double minRotateAngle = -10.0;
        public static final double maxRotateAngle = 30.0;
        public static final double rotateEndThreshold = 2.0; // degrees

        public static final double angleToRung = 24.62;

        public static enum RotatoSetpoint {
            BACK(-10),
            UP(5),
            FORWARD(28),
            TO_BAR(10); // TODO measure on bars, this is a guess

            private double angle;

            RotatoSetpoint(double angle) {
                this.angle = angle;
            }

            public double getAngle() {
                return angle;
            }
        }

        public static enum LiftySetpoint {
            UP(20),
            MID(10),
            DOWN(0);

            private double distance;

            LiftySetpoint(double distance) {
                this.distance = distance;
            }

            public double getDistance() {
                return distance;
            }
        }
    }

    public static class ClimberProfileConfig {
        public static final double maxLiftVelocity = 5.0;
        public static final double maxLiftAcceleration = 10.0;

        public static final double maxRotateVelocity = 50.0;
        public static final double maxRotateAcceleration = 50.0;

        public static final double liftP = 0.08;
        public static final double liftI = 0.005;
        public static final double liftD = 0.;

        public static final double liftG = 2.47;
        public static final double liftV = 3.07;
        public static final double liftA = 0.25;

        public static final double rotateP = 0.01;
        public static final double rotateI = 0.;// 0.00 1;
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

        public static final double wheelDiameterMeters = Units.inchesToMeters(4);
        public static final double gearRatio = 7.5833; // 3 motor rotations to 1 wheel rotation
        public static final double rotationToDistanceConversion = (Math.PI * wheelDiameterMeters) / gearRatio; //
        // circumference
        // / gear ratio
        public static final double wheelBase = 0.69;

        public static final int navXRotationalOffsetDegrees = 0;

        public static final double axisThreshold = 0.2;
        public static final double boostMultiplier = 1.25;
        public static final double slowMultiplier = 0.5;

    }

    public static class IndexerConfig {
        public static final int motorPort = 11;
        public static final double gearRatio = 1;

        public static final double kP = 0.004;
        public static final double kI = 0.00015;
        public static final double kD = 0;

        public static final double freeSpin_kP = 0.25;
        public static final double freeSpin_kS = 1e-4;
        public static final double freeSpinP_kV = 0.5;

        public static final double maxRotateVelocity = 50.0;
        public static final double maxRotateAcceleration = 50000.0;

        public static final double angleTolerableError = 15.0;
        public static final double angleTolerableEndVelocity = 1.0;

        public static final double velocityThreshold = 100;

        public static final int angleToShoot = 200;

        public static enum IndexerMode {
            HOLD,
            SHOOT,
            ANGLE,
            FREE_SPIN,
            EJECT_BY_INTAKE,
            SHUFFLEBOARD
        }
    }

    public static class IntakeConfig {
        public static final int intakeMotorPort = 22;

        public static final int extendLeftPort = 20;
        public static final int extendRightPort = 21;

        public static final double maxExtendMotorOutput = 0.2;

        public static final double intakeSpeed = 0.50;

        public static final double leftExtendP = 0.006;
        public static final double leftExtendI = 0.0004;
        public static final double leftExtendD = 0.000;

        public static final double rightExtendP = 0.005;
        public static final double rightExtendI = 0.0004;
        public static final double rightExtendD = 0.000;

        // public static final double extendS = 1.1157;
        public static final double extendS = 0.9157;
        public static final double extendV = 0.01;
        public static final double extendA = 0.039119;
        public static final double extendG = 0.38;

        public static final double maxExtendVelocity = 100;
        public static final double maxExtendAcceleration = 50;

        public static final double maxExtendAngle = 75;
        public static final double extendTolError = 5;
        public static final double extendTolEndVelo = 0.1;
        public static final double extendGearRator = 0.5;
    }

    public static class BallDetectorConfig {
        // difference in color for the indexer to know it's a ball and not background
        // color
        public static final double colorThreshold = 0.100;

        // ~300 with ball when light is on, ~30 when light is off
        public static final double infraredThreshold = 250.0;
    }

    public static class ShooterConfig {
        public static final int topMotorPort = 10;
        public static final int bottomMotorPort = 9;

        public static final double topMotorSpeed = 0.62;
        public static final double bottomMotorSpeed = 0.62;

        public static final double maxRotateVelocity = 50.0;
        public static final double maxRotateAcceleration = 50000.0;

        public static final double angleTolerableError = 3.0;
        public static final double angleTolerableEndVelocity = 1.0;

        public static final double spinUpThresholdPercent = 0.05;
        public static final double shotThresholdRPM = 200.0;

        public static final double kP = 2e-4;
        public static final double kI = 3e-7;
        public static final double kD = 4e-2;
        public static final double kIz = 0;
        public static final double kFF = 0.000156;
        public static final double kMaxOutput = 1;
        public static final double kMinOutput = -1;

        public static enum ShooterMode {
            // TODO none of these have been found yet
            OFF(0, 0), AUTO(0, 0), FENDER_LOW(1100, 900), FENDER_HIGH(1550, 2600), TARMAC(3750, 1600),
            LAUNCHPAD(3800, 1900),
            INDEX(-350.0, -350.0), EJECT(500, 500), SHUFFLEBOARD(0, 0);

            private double topRPM, bottomRPM;

            private ShooterMode(double topRPM, double botRPM) {
                this.topRPM = topRPM;
                this.bottomRPM = botRPM;
            }

            public double getTopRPM() {
                return topRPM;
            }

            public double getBottomRPM() {
                return bottomRPM;
            }
        }
    }

    public static class VisionConfig {
        public static final double cameraHeightMeters = 0.54;
        public static final double targetHeightMeters = Units.feetToMeters(8) + Units.inchesToMeters(7);
        public static final double cameraPitchRadians = Units.degreesToRadians(28.0);
        public static final double upperHubDiameterMeters = Units.feetToMeters(4.0);
        public static final double cameraOffsetFromFrontMeters = Units.inchesToMeters(6.0);
        public static final double goalRangeMeters = 0.0;

        public static final double rotationP = 0.018;
        public static final double rotationI = 0.0;
        public static final double rotationD = 8e-4;

        public static final double rotationMinPower = 0.035;
        public static final double rotationMinOutput = -0.1125;
        public static final double rotationMaxOutput = 0.1125;

        public static final double tolerableError = 0.1;
        public static final double tolerableEndVelocity = 0.1;
    }

    public static final int driverControllerPort = 0;
    public static final int operatorControllerPort = 1;
    public static final int operatorPadPort = 2;
}