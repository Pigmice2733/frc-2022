package frc.robot;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;

public class Vision {
    // TODO change these
    private final static double CAMERA_HEIGHT_METERS = 0.0;
    private final static double TARGET_HEIGHT_METERS = Units.inchesToMeters(104); // 8'8"
    private final static double CAMERA_PITCH_RADIANS = 0;

    private final static double GOAL_RANGE_METERS = 0.0;

    // might have to not be static, will test
    // if it can't, will change to NetworkTables
    private static PhotonCamera camera = new PhotonCamera("gloworm"); // name set in web ui?

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

    public static void update() {
        if (!currentlyAligning) {
            currentlyAligning = true;
            camera.setLED(VisionLEDMode.kOn);
        }

        if (!hasTarget()) {
            rotationOutput = 0.0;
            return;
        }

        PhotonTrackedTarget target = getBestTarget();

        double angle = target.getYaw();

        rotationOutput = -rotationController.calculate(angle, 0.0);
    }

    public static void stop() {
        if (currentlyAligning) {
            currentlyAligning = false;
            camera.setLED(VisionLEDMode.kOff);
        }
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
