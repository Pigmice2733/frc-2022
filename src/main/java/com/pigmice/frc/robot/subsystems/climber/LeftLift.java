package com.pigmice.frc.robot.subsystems.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.Constants.ClimberConfig;

public class LeftLift extends AbstractLift {
    public LeftLift(BooleanSupplier usePower, DoubleSupplier powerSupplier) {
        super(ClimberConfig.liftLeftPort, usePower, powerSupplier);
    }

    @Override
    protected void useOutput(double output) {
        motor.set(-output);
    }

    @Override
    protected double getEncoderValue() {
        return -this.encoder.getPosition();
    }
}
