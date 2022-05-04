package com.pigmice.frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.pigmice.frc.lib.utils.Odometry;
import com.pigmice.frc.lib.utils.Odometry.Pose;
import com.pigmice.frc.robot.Constants.DrivetrainConfig;
import com.pigmice.frc.robot.Dashboard;
import com.pigmice.frc.robot.Utils;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem {
    private final CANSparkMax leftDrive, rightDrive, rightFollower, leftFollower;

    private double leftDemand, rightDemand;
    private double leftPosition, rightPosition, heading; // heading is in degrees now

    private boolean boost = false;
    private boolean slow = false;

    private Odometry odometry;

    private AHRS navx;
    private double navxTestAngle;
    private boolean navxTestPassed = false;
    private final NetworkTableEntry navxReport;

    private final NetworkTableEntry xDisplay, yDisplay, headingDisplay,
            leftEncoderDisplay, rightEncoderDisplay;

    private Pose2d initialPosition = new Pose2d(0, 0, new Rotation2d(0));

    public static final DifferentialDriveKinematics driveKinematics = new DifferentialDriveKinematics(
            DrivetrainConfig.wheelBase);

    private final DifferentialDrive drive;

    private DifferentialDriveOdometry diffOdometry;

    public Drivetrain() {
        rightDrive = new CANSparkMax(DrivetrainConfig.frontLeftMotorPort,
                MotorType.kBrushless);
        rightFollower = new CANSparkMax(DrivetrainConfig.backLeftMotorPort,
                MotorType.kBrushless);
        leftDrive = new CANSparkMax(DrivetrainConfig.frontRightMotorPort,
                MotorType.kBrushless);
        leftFollower = new CANSparkMax(DrivetrainConfig.backRightMotorPort,
                MotorType.kBrushless);

        drive = new DifferentialDrive(leftDrive, rightDrive);

        rightDrive.restoreFactoryDefaults();
        rightFollower.restoreFactoryDefaults();
        leftDrive.restoreFactoryDefaults();
        leftFollower.restoreFactoryDefaults();

        leftDrive.setInverted(true);
        leftFollower.follow(leftDrive);
        rightFollower.follow(rightDrive);

        navx = new AHRS(DrivetrainConfig.navxPort);

        ShuffleboardLayout testReportLayout = Shuffleboard.getTab(Dashboard.systemsTestTabName)
                .getLayout("Drivetrain", BuiltInLayouts.kList)
                .withSize(2, 1)
                .withPosition(Dashboard.drivetrainTestPosition, 0);

        navxReport = testReportLayout.add("NavX", false).getEntry();

        // leftDrive.getEncoder().setPositionConversionFactor(DrivetrainConfig.rotationToDistanceConversion);
        // rightDrive.getEncoder().setPositionConversionFactor(DrivetrainConfig.rotationToDistanceConversion);

        setCoastMode(false);

        ShuffleboardLayout odometryLayout = Shuffleboard.getTab(Dashboard.developmentTabName)
                .getLayout("Odometry", BuiltInLayouts.kList).withSize(2, 5)
                .withPosition(Dashboard.drivetrainDisplayPosition, 0);

        xDisplay = odometryLayout.add("X", 0.0).getEntry();
        yDisplay = odometryLayout.add("Y", 0.0).getEntry();
        headingDisplay = odometryLayout.add("Heading", 0.0).getEntry();
        leftEncoderDisplay = odometryLayout.add("Left Encoder", 0).getEntry();
        rightEncoderDisplay = odometryLayout.add("Right Encoder", 0).getEntry();

        odometry = new Odometry(new Pose(0.0, 0.0, 0.0));
        diffOdometry = new DifferentialDriveOdometry(new Rotation2d(), new Pose2d());

        // Used to be in initialize()
        leftPosition = 0.0;
        rightPosition = 0.0;
        heading = 0.0;

        leftDrive.getEncoder().setPosition(0.0);
        rightDrive.getEncoder().setPosition(0.0);

        odometry.set(new Pose(0.0, 0.0, heading), leftPosition, rightPosition);

        leftDemand = 0.0;
        rightDemand = 0.0;
    }

    public void init() {
        zeroHeading();
    }

    @Override
    public void periodic() {
        //this.getDistanceFromStart();
        this.getPose();
        // from updateInputs
        leftPosition = rotationsToDistance(leftDrive.getEncoder().getPosition());
        rightPosition = rotationsToDistance(rightDrive.getEncoder().getPosition());

        updateHeading();

        odometry.update(leftPosition, rightPosition, heading);
        diffOdometry.update(navx.getRotation2d(), leftPosition, rightPosition);

        // from updateDashboard()
        Pose currentPose = odometry.getPose();

        xDisplay.setNumber(currentPose.getX());
        yDisplay.setNumber(currentPose.getY());
        headingDisplay.setNumber(currentPose.getHeading());
        leftEncoderDisplay.setNumber(leftPosition);
        rightEncoderDisplay.setNumber(rightPosition);
    }

    public void updateDashboard() {
    }

    public void updateHeading() {
        double headingDegrees = navx.getAngle();

        SmartDashboard.putNumber("Heading (Degrees)", headingDegrees);

        heading = headingDegrees;
    }

    public void updateInputs() {
    }

    public double getHeading() {
        return heading;
    }

    public Pose2d getPose() {
        System.out.println(diffOdometry.getPoseMeters());
        return diffOdometry.getPoseMeters();
    }

    public void boost() {
        this.boost = true;
    }

    public void stopBoost() {
        this.boost = false;
    }

    public void toggleBoost() {
        this.boost = !this.boost;
        this.slow = false;
    }

    public boolean isBoosting() {
        return this.boost;
    }

    public void slow() {
        this.slow = true;
    }

    public void stopSlow() {
        this.slow = false;
    }

    public void toggleSlow() {
        this.slow = !this.slow;
        this.boost = false;
    }

    public boolean isSlow() {
        return this.slow;
    }

    public boolean isCalibrating() {
        return this.navx.isCalibrating();
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(
                leftDrive.getEncoder().getVelocity() * DrivetrainConfig.wheelDiameterMeters,
                rightDrive.getEncoder().getVelocity() * DrivetrainConfig.wheelDiameterMeters);
    }

    public TrajectoryConfig generateTrajectoryConfig() {
        return new TrajectoryConfig(DrivetrainConfig.kMaxSpeedMetersPerSecond,
                        DrivetrainConfig.kMaxAccelerationMetersPerSecondSquared)
                .setKinematics(Drivetrain.driveKinematics)
                .addConstraint(new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(DrivetrainConfig.ksVolts,
                        DrivetrainConfig.kvVoltSecondsPerMeter,
                        DrivetrainConfig.kaVoltSecondsSquaredPerMeter), driveKinematics, 10));
    }

    public void tankDriveVolts(double left, double right) {
        //System.out.println("Left Volts: " + left + " | Right Volts: " + right);
        leftDrive.setVoltage(left);
        rightDrive.setVoltage(right);
        drive.feed();
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        //System.out.println("Left Speed: " + leftSpeed + " | Right Speed: " + rightSpeed);

        leftDemand = leftSpeed;
        rightDemand = rightSpeed;

        updateOutputs();
    }

    public void arcadeDrive(double forwardSpeed, double turnSpeed) {
        drive.arcadeDrive(forwardSpeed, turnSpeed);
    }

    public void curvatureDrive(double forwardSpeed, double curvature) {
        double leftSpeed = forwardSpeed;
        double rightSpeed = forwardSpeed;

        if (!Utils.almostEquals(forwardSpeed, 0.0)) {
            leftSpeed = forwardSpeed * (1 + (curvature * 0.5 *
                    DrivetrainConfig.wheelBase));
            rightSpeed = forwardSpeed * (1 - (curvature * 0.5 *
                    DrivetrainConfig.wheelBase));
        }

        leftDemand = leftSpeed;
        rightDemand = rightSpeed;

        updateOutputs();
    }

    public void swerveDrive(double forward, double strafe, double rotation_x) {
    }

    public double getAverageEncoderDistance() {
        return (leftDrive.getEncoder().getPosition() + rightDrive.getEncoder().getPosition()) / 2.0;
    }

    public RelativeEncoder getLeftEncoder() {
        return leftDrive.getEncoder();
    }

    public RelativeEncoder getRightEncoder() {
        return rightDrive.getEncoder();
    }

    public void setMaxOutput(double maxOutput) {
        drive.setMaxOutput(maxOutput);
    }

    public void stop() {
        leftDemand = 0.0;
        rightDemand = 0.0;
    }

    public void updateOutputs() {
        leftDemand *= 0.75;
        rightDemand *= 0.75;

        if (slow) {
            leftDemand *= DrivetrainConfig.slowMultiplier;
            rightDemand *= DrivetrainConfig.slowMultiplier;
        }

        leftDrive.set(leftDemand);
        rightDrive.set(rightDemand);

        leftDemand = 0.0;
        rightDemand = 0.0;
    }

    public void test(double time) {
        if (time < 0.1) {
            navxTestAngle = navx.getAngle();
            navxTestPassed = false;
        }

        if (!navxTestPassed) {
            navxTestPassed = navx.getAngle() != navxTestAngle;
        }

        navxReport.setBoolean(navxTestPassed);
    }

    public void setCoastMode(boolean coasting) {
        CANSparkMax.IdleMode newMode = coasting ? IdleMode.kCoast : IdleMode.kBrake;
        leftDrive.setIdleMode(newMode);
        rightDrive.setIdleMode(newMode);
        leftFollower.setIdleMode(newMode);
        rightFollower.setIdleMode(newMode);
    }

    public void resetPose() {
        // this.odometry.set(new Pose(0, 0, getPose().getHeading()), 0.0, 0.0);
        initialPosition = new Pose2d(0, 0, new Rotation2d(0));
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        Rotation2d rotation = navx.getRotation2d();
        diffOdometry.resetPosition(pose, rotation);
    }

    public double getDistanceFromStart() {
        Transform2d displacement = this.getPose().minus(new Pose2d(0, 0, new Rotation2d(0)));
        double distance = Math.sqrt(displacement.getX() * displacement.getX() + displacement.getY() * displacement.getY());
        
        //System.out.println("Distance from start: " + distance);
        return distance;
    }

    public void resetEncoders() {
        leftDrive.getEncoder().setPosition(0);
        rightDrive.getEncoder().setPosition(0);
    }

    public void zeroHeading() {
        this.navx.reset();
        updateHeading();
    }

    public void testPeriodic() {

    }

    public double rotationsToDistance(double rotations) {
        return rotations * Math.PI * DrivetrainConfig.wheelDiameterMeters / DrivetrainConfig.gearRatio;
    }
}