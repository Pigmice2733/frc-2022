package com.pigmice.frc.robot.subsystems.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pigmice.frc.robot.Constants.ClimberConfig;

public class RightLift extends AbstractLift {

    public RightLift(BooleanSupplier usePower, DoubleSupplier powerSupplier) {
        super(ClimberConfig.liftRightPort, usePower, powerSupplier);
    }

    @Override
    protected void useOutput(double output) {
        motor.set(output);
    }

    @Override
    protected double getEncoderValue() {
        return this.encoder.getPosition();
    }
}
