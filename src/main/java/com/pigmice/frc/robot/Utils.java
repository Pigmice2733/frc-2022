package com.pigmice.frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

public class Utils {
    private static final int DECISECONDS_PER_MINUTE = 600;

    /**
     * Returns the number of sensor ticks in a complete rotation for a specific
     * encoder. Add more encoder types as they start to get used.
     * 
     * @param encoder Type of encoder
     * @return Number of sensor units per rotation
     */
    private static int getSensorUnitsPerRotation(FeedbackDevice encoder) {
        switch (encoder) {
            case CTRE_MagEncoder_Absolute:
            case CTRE_MagEncoder_Relative:
                return 4096;
            default:
                throw new RuntimeException("Unknown Feedback Device " + encoder.name());
        }
    }

    /**
     * Calculates the velocity of a motor in RPM based on an encoder output.
     * 
     * @param raw Encoder velocity in raw sensor units
     * @return Motor RPM
     * @throws Exception
     */
    public static double calculateRPM(double raw, FeedbackDevice encoder) {
        int sensorUnitsPerRotation = getSensorUnitsPerRotation(encoder);
        return ((raw * DECISECONDS_PER_MINUTE) / sensorUnitsPerRotation);
    }

    public static boolean almostEquals(double a, double b) {
        return almostEquals(a, b, 1e-6);
    }

    public static boolean almostEquals(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    /**
     * Converts motor RPM to encoder ticks per 100ms.
     * 
     * @param rpm Motor RPM
     * @return Encoder ticks in 100ms
     */
    public static double calculateTicksPerDs(double rpm, FeedbackDevice encoder) {
        int sensorUnitsPerRotation = getSensorUnitsPerRotation(encoder);
        return rpm * sensorUnitsPerRotation / DECISECONDS_PER_MINUTE;
    }
}
