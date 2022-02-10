// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

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
        public static int topMotorPort = 8;
        public static int bottomMotorPort = 7;

        public static double topMotorSpeed = 0.62;
        public static double bottomMotorSpeed = 0.62;
    }
    
    public static class ClimberConfig {
        public static int liftLeadPort = 0;
        public static int liftFollowPort = 0;
        public static int rotateLeadPort = 0;
        public static int rotateFollowPort = 0;

        // both of these in rpm
        public static double defaultLiftMotorSpeed = 600;
        public static double defaultRotateMotorSpeed = 600;

        // radius of gear in contact with motor and lifting arm, in inches
        public static double liftMotorRadius = 0.5;
        
        // both of these in inches
        public static double liftArmHeight = 52; // height of base of lift arms
        public static double rotateArmLength = 8.25; // at straight vertical, height above liftArmHeight

        // both of these in inches
        public static double horizDistBtwnRungs = 24;
        public static double vertDistBtwnRungs = 15.375;
    }
}
