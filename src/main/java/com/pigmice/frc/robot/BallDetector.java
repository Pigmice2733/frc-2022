package com.pigmice.frc.robot;

import com.pigmice.frc.robot.Constants.BallDetectorConfig;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;

public class BallDetector {
    private Alliance lastBallState = Alliance.Invalid;
    private ColorSensorV3 colorSensor;

    private ShuffleboardTab indexerTab;

    private final NetworkTableEntry rEntry;
    private final NetworkTableEntry gEntry;
    private final NetworkTableEntry bEntry;
    private final NetworkTableEntry irEntry;
    private final NetworkTableEntry proximityEntry;

    public BallDetector() {
        this.colorSensor = new ColorSensorV3(Port.kOnboard);

        this.indexerTab = Shuffleboard.getTab("Indexer");

        this.rEntry = indexerTab.add("Color R", 0.0).getEntry();
        this.gEntry = indexerTab.add("Color G", 0.0).getEntry();
        this.bEntry = indexerTab.add("Color B", 0.0).getEntry();
        this.irEntry = indexerTab.add("Color IR", 0.0).getEntry();
        this.proximityEntry = indexerTab.add("Color Proximity", 0.0).getEntry();
    }

    public void setColorEntries() {
        Color color = colorSensor.getColor();
        this.rEntry.setNumber(color.red);
        this.gEntry.setNumber(color.green);
        this.bEntry.setNumber(color.blue);
        this.irEntry.setNumber(colorSensor.getIR());
        this.proximityEntry.setNumber(colorSensor.getProximity());
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
