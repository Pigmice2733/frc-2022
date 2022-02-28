package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.TrapezoidProfileSubsystem;
import edu.wpi.first.math.trajectory.TrapezoidProfile.*;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

import com.pigmice.frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DrivetrainProfiled extends TrapezoidProfileSubsystem {
    private SimpleMotorFeedforward feedforward;

    private final CANSparkMax leftDrive, rightDrive, leftFollow, rightFollow;
    private double leftDemand, rightDemand;

    private boolean slow = false;
    private boolean boost = false;

    public DrivetrainProfiled() {
        super(new TrapezoidProfile.Constraints(DrivetrainProfileConfig.maxVelocity, DrivetrainProfileConfig.maxAcceleration));
        feedforward = new SimpleMotorFeedforward(DrivetrainProfileConfig.feedforwardStatic,
            DrivetrainProfileConfig.feedforwardVelocity, DrivetrainProfileConfig.feedforwardAcceleration);

        rightDrive = new CANSparkMax(DrivetrainConfig.frontRightMotorPort,
            MotorType.kBrushless);
        rightFollow = new CANSparkMax(DrivetrainConfig.backRightMotorPort,
            MotorType.kBrushless);
        leftDrive = new CANSparkMax(DrivetrainConfig.frontLeftMotorPort,
            MotorType.kBrushless);
        leftFollow = new CANSparkMax(DrivetrainConfig.backLeftMotorPort,
            MotorType.kBrushless);
        
        rightDrive.setInverted(true);
        leftFollow.follow(leftDrive);
        rightFollow.follow(rightDrive);

        leftDemand = 0d;
        rightDemand = 0d;
    }

    @Override
    public void useState(State state) {
        double feedforwardOutput = feedforward.calculate(state.position, state.velocity);
        leftDrive.set(feedforwardOutput);
        rightDrive.set(feedforwardOutput);
    }
}
