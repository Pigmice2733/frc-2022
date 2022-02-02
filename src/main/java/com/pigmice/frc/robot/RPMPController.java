package com.pigmice.frc.robot;

import edu.wpi.first.math.controller.PIDController;

public class RPMPController {
    private final PIDController pidController;
    private final double startupMotorVelocity;
    private double motorVelocitySetting;
    private double targetRPM;
    
    public RPMPController(double kp, double startupMotorVelocity) {
        pidController = new PIDController(kp,0,0);
        this.startupMotorVelocity = startupMotorVelocity;
        targetRPM = 0;
        motorVelocitySetting = 0;
    }

    public double update(double currentRPM) {
        if (motorVelocitySetting == 0) {
            motorVelocitySetting = startupMotorVelocity;
        } else {
            double delta = pidController.calculate(currentRPM);
            double percentChange = delta/currentRPM;
            motorVelocitySetting += (percentChange * motorVelocitySetting);
        }
        return motorVelocitySetting;
    }

    public void setTargetRPM(double targetRPM) {
        this.targetRPM = targetRPM;
        pidController.setSetpoint(targetRPM);
        if (this.targetRPM == 0) {
            this.motorVelocitySetting = 0;
        }
    }
    
}
