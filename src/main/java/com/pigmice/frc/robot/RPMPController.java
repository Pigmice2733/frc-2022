package com.pigmice.frc.robot;

import edu.wpi.first.math.controller.PIDController;

public class RPMPController {
    private final PIDController pidController;
    private final double startupMotorOutput;
    private double motorOutputSetting;
    private double targetRPM;

    private double kP;

    public RPMPController(double kp, double startupMotorOutput) {
        this.kP = kp;
        pidController = new PIDController(kp, 0, 0);
        this.startupMotorOutput = startupMotorOutput;
        targetRPM = 0;
        motorOutputSetting = 0;
    }

    public double update(double currentRPM) {
        if (targetRPM == 0) {
            motorOutputSetting = 0;
        } else if (motorOutputSetting == 0 || Math.abs(currentRPM) < 1) {
            motorOutputSetting = startupMotorOutput;
        } else {
            double delta = pidController.calculate(currentRPM);
            double percentChange = delta / currentRPM;
            if (percentChange > kP) {
                percentChange = kP;
            }
            motorOutputSetting += (percentChange * motorOutputSetting);
        }
        motorOutputSetting = Math.max(-1, Math.min(1, motorOutputSetting));
        return motorOutputSetting;
    }

    public void setTargetRPM(double targetRPM) {
        this.targetRPM = targetRPM;
        pidController.setSetpoint(targetRPM);
        if (this.targetRPM == 0) {
            this.motorOutputSetting = 0;
        }
    }

}
