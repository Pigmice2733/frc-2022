package com.pigmice.frc.robot;

import com.pigmice.frc.lib.utils.Ring;
import com.pigmice.frc.robot.Constants.VisionConfig;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    private static PhotonCamera gloworm;

    private static PIDController rotationController = new PIDController(VisionConfig.rotationP, VisionConfig.rotationI,
            VisionConfig.rotationD);

    private static boolean currentlyAligning = false;

    private static ShuffleboardTab visionTab;
    private static NetworkTableEntry yawEntry;
    private static NetworkTableEntry hasTargetEntry;
    private static NetworkTableEntry directionEntry;
    private static NetworkTableEntry distanceEntry;
    private static NetworkTableEntry outputEntry;

    private static PhotonTrackedTarget lastTarget;

    private static Ring angleBuffer = new Ring(5);

    private static ProfiledPIDController visionController;

    static {
        visionController = new ProfiledPIDController(VisionConfig.rotationP, VisionConfig.rotationI,
                VisionConfig.rotationD, new TrapezoidProfile.Constraints(2.0, 2.0));
        visionController.setTolerance(VisionConfig.tolerableError, VisionConfig.tolerableEndVelocity);
        visionController.setGoal(0.0);
    }

    public static void init() {
        SmartDashboard.putBoolean("Vision Initialized", true);
        visionTab = Shuffleboard.getTab("Vision");
        yawEntry = visionTab.add("Yaw Output", 0.0d).getEntry();
        hasTargetEntry = visionTab.add("Has Target", false).getEntry();
        directionEntry = visionTab.add("Direction", "N/A").getEntry();
        distanceEntry = visionTab.add("Distance", -1.0).getEntry();
        outputEntry = visionTab.add("Output", 0.0).getEntry();
        currentlyAligning = false;

        gloworm = new PhotonCamera("gloworm");
    }

    public static void reset() {
        angleBuffer = new Ring(5);
    }

    public static void update() {
        boolean hasTarget = hasTarget();

        hasTargetEntry.setBoolean(hasTarget);

        if (!hasTarget)
            return;

        distanceEntry.setDouble(getDistanceFromTarget(getBestTarget()));

        double yaw = getTargetYaw();
        yawEntry.setDouble(yaw);
        directionEntry.setString(yaw < 0 ? "LEFT" : yaw > 0 ? "RIGHT" : "STRAIGHT");
    }

    public static double getOutput() {
        if (hasTarget()) {
            double output = rotationController.calculate(getTargetYaw());
            outputEntry.setDouble(output);

            if (output == Double.NaN) {
                return 0.0;
            }

            // clamp output between -0.15 and 0.15
            output = MathUtil.clamp(output, VisionConfig.rotationMinOutput, VisionConfig.rotationMaxOutput);

            // minimum power defined in Constants.java, preserving direction
            output = Math.max(Math.abs(output), VisionConfig.rotationMinPower) * Math.signum(output);
            return output;
        } else {
            return 0;
        }
    }

    public static void startAligning() {
        currentlyAligning = true;
        gloworm.setLED(VisionLEDMode.kOn);
        Vision.reset();
    }

    public static void stopAligning() {
        if (currentlyAligning) {
            currentlyAligning = false;
            gloworm.setLED(VisionLEDMode.kOff);
        }
    }

    public static void toggleAlign() {
        if (currentlyAligning)
            stopAligning();
        else
            startAligning();
    }

    public static boolean hasTarget() {
        return gloworm.getLatestResult() != null && gloworm.getLatestResult().hasTargets();
    }

    public static PhotonTrackedTarget getBestTarget() {
        if (hasTarget()) {
            PhotonTrackedTarget newTarget = gloworm.getLatestResult().getBestTarget();
            if (newTarget != null) {
                lastTarget = newTarget;
            }
            return lastTarget;
        } else {
            if (lastTarget != null)
                return lastTarget;
            return null;
        }
    }

    public static double getTargetYaw() {
        if (hasTarget()) {
            PhotonTrackedTarget target = getBestTarget();
            if (target != null) {
                double angle = target.getYaw();
                angleBuffer.put(angle);
            }
        }
        if (angleBuffer.isEmpty()) {
            return Double.NaN;
        } else {
            return angleBuffer.average();
        }
    }

    public static double getDistanceFromTarget(PhotonTrackedTarget target) {
        if (target == null)
            return 0.0;
        // gloworm distance is distance to front of hub
        return PhotonUtils.calculateDistanceToTargetMeters(VisionConfig.cameraHeightMeters,
                VisionConfig.targetHeightMeters,
                VisionConfig.cameraPitchRadians, Units.degreesToRadians(target.getPitch()))
                + (VisionConfig.upperHubDiameterMeters / 2.0) + VisionConfig.cameraOffsetFromFrontMeters;
    }

    public static double alignmentError() {
        return rotationController.getPositionError();
    }
}
