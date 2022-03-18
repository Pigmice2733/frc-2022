package com.pigmice.frc.robot;

import com.pigmice.frc.robot.Constants.VisionConfig;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    // TODO change these

    private static PhotonCamera camera; // name set in web ui?

    private static PIDController rotationController = new PIDController(VisionConfig.rotationP, VisionConfig.rotationI,
            VisionConfig.rotationD);

    private static boolean currentlyAligning = false;
    private static double rotationOutput = 0.0;

    private static ShuffleboardTab visionTab;
    private static NetworkTableEntry yawEntry;
    private static NetworkTableEntry hasTargetEntry;
    private static NetworkTableEntry directionEntry;
    private static NetworkTableEntry distanceEntry;

    public static void init() {
        SmartDashboard.putBoolean("Vision Initialized", true);
        visionTab = Shuffleboard.getTab("Vision");
        yawEntry = visionTab.add("Yaw Output", 0.0d).getEntry();
        hasTargetEntry = visionTab.add("Has Target", false).getEntry();
        directionEntry = visionTab.add("Direction", "N/A").getEntry();
        distanceEntry = visionTab.add("Distance", -1.0).getEntry();
        currentlyAligning = false;

        camera = new PhotonCamera("gloworm");
    }

    public static void update() {
        if (!currentlyAligning)
            return;

        hasTargetEntry.setBoolean(hasTarget());

        if (!hasTarget()) {
            rotationOutput = 0.0;
            return;
        }

        PhotonTrackedTarget target = getBestTarget();

        double angle = target.getYaw();

        yawEntry.setDouble(angle);
        directionEntry.setString(angle < 0 ? "LEFT" : angle > 0 ? "RIGHT" : "STRAIGHT");

        double distance = getDistanceFromTarget(target);
        distanceEntry.setDouble(distance);

        rotationOutput = -rotationController.calculate(angle, 0.0);
    }

    public static void startAligning() {
        currentlyAligning = true;
        camera.setLED(VisionLEDMode.kOn);
    }

    public static void stopAligning() {
        if (currentlyAligning) {
            currentlyAligning = false;
            camera.setLED(VisionLEDMode.kOff);
        }
    }

    public static void toggleAlign() {
        if (currentlyAligning)
            stopAligning();
        else
            startAligning();
    }

    public static boolean hasTarget() {
        return camera.hasTargets() && camera.getLatestResult().hasTargets();
    }

    public static PhotonTrackedTarget getBestTarget() {
        if (hasTarget()) {
            return camera.getLatestResult().getBestTarget();
        } else {
            return null;
        }
    }

    public static double getTargetYaw() {
        if (hasTarget()) {
            return getBestTarget().getYaw();
        } else {
            return 0.0;
        }
    }

    public static double getDistanceFromTarget(PhotonTrackedTarget target) {
        return PhotonUtils.calculateDistanceToTargetMeters(VisionConfig.cameraHeightMeters,
                VisionConfig.targetHeightMeters,
                VisionConfig.cameraPitchRadians, Units.degreesToRadians(target.getPitch()));
    }

    public static double getRotationOutput() {
        return rotationOutput;
    }

    public static double alignmentError() {
        return rotationController.getPositionError();
    }
}
