package com.pigmice.frc.robot.subsystems.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.pigmice.frc.robot.Constants.ClimberConfig;

public class RightRotate extends AbstractRotate {

    public RightRotate(BooleanSupplier usePower, DoubleSupplier powerSupplier) {
        super(ClimberConfig.rotateRightPort, false, usePower, powerSupplier);
        this.motor.setInverted(false);
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
