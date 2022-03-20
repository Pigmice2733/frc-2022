package com.pigmice.frc.robot;

import com.pigmice.frc.robot.Constants.BallDetectorConfig;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

public class BallDetector {
    private Alliance lastBallState = Alliance.Invalid;
    private ColorSensorV3 colorSensor;

    public BallDetector() {
        this.colorSensor = new ColorSensorV3(Port.kOnboard);
    }

    public Alliance getNewBall() {
        double ir = colorSensor.getIR();
        if (ir < BallDetectorConfig.infraredThreshold) {
            this.lastBallState = Alliance.Invalid;
            return Alliance.Invalid;
        }
        Alliance ballAlliance = getBallAlliance();
        if (ballAlliance != lastBallState) {
            lastBallState = ballAlliance;
            return ballAlliance;
        } else {
            return Alliance.Invalid;
        }
    }

    public Alliance getBallAlliance() {
        Color color = this.colorSensor.getColor();
        double red = color.red;
        double blue = color.blue;
        double diff = Math.abs(red - blue);
        if (diff < BallDetectorConfig.colorThreshold || red == blue)
            return Alliance.Invalid;
        return red > blue ? Alliance.Red : Alliance.Blue;
    }

    public boolean isSameAlliance() {
        return getBallAlliance() == DriverStation.getAlliance();
    }
}
