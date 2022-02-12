package com.pigmice.frc.robot;

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
    private final static double CAMERA_HEIGHT_METERS = 0.8;
    private final static double TARGET_HEIGHT_METERS = 2.38;// Units.inchesToMeters(104); // 8'8"
    private final static double CAMERA_PITCH_RADIANS = 0;

    private final static double GOAL_RANGE_METERS = 0.0;

    // might have to not be static, will test
    // if it can't, will change to NetworkTables
    private static PhotonCamera camera; // name set in web ui?

    private final static double FORWARD_P = 0.1;
    private final static double FORWARD_I = 0.0;
    private final static double FORWARD_D = 0.0;

    private static PIDController forwardController = new PIDController(FORWARD_P, FORWARD_I, FORWARD_D);

    private static final double ROTATION_P = 0.1;
    private static final double ROTATION_I = 0.0;
    private static final double ROTATION_D = 0.0;

    private static PIDController rotationController = new PIDController(ROTATION_P, ROTATION_I, ROTATION_D);

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
        return camera.getLatestResult().hasTargets();
    }

    public static PhotonTrackedTarget getBestTarget() {
        return camera.getLatestResult().getBestTarget();
    }

    public static double getDistanceFromTarget(PhotonTrackedTarget target) {
        return PhotonUtils.calculateDistanceToTargetMeters(CAMERA_HEIGHT_METERS, TARGET_HEIGHT_METERS,
                CAMERA_PITCH_RADIANS, Units.degreesToRadians(target.getPitch()));
    }

    public static double getRotationOutput() {
        return rotationOutput;
    }

    public static double alignmentError() {
        return rotationController.getPositionError();
    }
}
