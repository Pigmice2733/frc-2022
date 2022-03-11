package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.pigmice.frc.robot.Constants.ClimberConfig;

public class RightRotate extends AbstractRotate {

    public RightRotate() {
        super(ClimberConfig.rotateRightPort, true);
    }

    @Override
    protected void useOutput(double output) {
        motor.set(ControlMode.PercentOutput, output);
    }

    @Override
    protected double getEncoderValue() {
        return -this.motor.getSelectedSensorPosition();
    }
}
