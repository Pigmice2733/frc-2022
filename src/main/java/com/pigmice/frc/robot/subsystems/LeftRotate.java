package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.pigmice.frc.robot.Constants.ClimberConfig;

public class LeftRotate extends AbstractRotate {

    public LeftRotate() {
        super(ClimberConfig.rotateLeftPort, false);
    }

    @Override
    protected void useOutput(double output) {
        motor.set(ControlMode.PercentOutput, output * 1.10);// + 0.05);
    }

    @Override
    protected double getEncoderValue() {
        return -this.motor.getSelectedSensorPosition();
    }
}
