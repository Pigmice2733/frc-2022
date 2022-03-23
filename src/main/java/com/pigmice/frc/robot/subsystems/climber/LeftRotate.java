package com.pigmice.frc.robot.subsystems.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.pigmice.frc.robot.Constants.ClimberConfig;

public class LeftRotate extends AbstractRotate {

    public LeftRotate(BooleanSupplier usePower, DoubleSupplier powerSupplier) {
        super(ClimberConfig.rotateLeftPort, true, usePower, powerSupplier);
        this.motor.setInverted(true);
    }

    @Override
    protected void useOutput(double output) {
        motor.set(ControlMode.PercentOutput, output);// + 0.05);
    }

    @Override
    protected double getEncoderValue() {
        return this.motor.getSelectedSensorPosition();
    }
}
