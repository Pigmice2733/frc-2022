// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class ShooterConfig {
        public static int topMotorPort = 8;
        public static int bottomMotorPort = 7;

        //public static double topMotorSpeed = 0.75;
        //public static double bottomMotorSpeed = 0.45;

        public static double topMotorSpeed = 0.37;
        public static double bottomMotorSpeed = 0.75;
    }
}
