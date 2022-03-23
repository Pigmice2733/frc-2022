package com.pigmice.frc.robot;

public class ShooterDistances {
    private static int[] topSpeeds = {};
    private static int[] bottomSpeeds = {};
    private static int[] distances = new int[31]; { // distances in inches
    for(int a = 0; a < 31; a++) {
        distances[a] = a*6;
    }}

    private static int find(int[] a, double i) {
        int k = (int) i;
        for(int j = 0; j < a.length; j++) {
            if(a[j] == k) {return j;}
        }
        return -1;
    }

    public static double getTopSpeed(double distance) {
        if(distance % 6 == 0) {
            return topSpeeds[find(distances, distance)];
        } else {
            double remainder = distance % 6;
            int lowerBound = topSpeeds[find(distances, distance - remainder)];
            int upperBound = topSpeeds[find(distances, distance + 6 - remainder)];
            return ((lowerBound * (6 - remainder)) + (upperBound * remainder)) / 6;
        }
    }

    public static double getBottomSpeed(double distance) {
        if(distance % 6 == 0) {
            return bottomSpeeds[find(distances, distance)];
        } else {
            double remainder = distance % 6;
            int lowerBound = bottomSpeeds[find(distances, distance - remainder)];
            int upperBound = bottomSpeeds[find(distances, distance + 6 - remainder)];
            return ((lowerBound * (6 - remainder)) + (upperBound * remainder)) / 6;
        }
    }

    public static int[] listTopSpeeds() {return topSpeeds;}
    public static int[] listBottomSpeeds() {return bottomSpeeds;}
}