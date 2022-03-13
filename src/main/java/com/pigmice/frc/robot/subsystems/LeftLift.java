package com.pigmice.frc.robot.subsystems;

import com.pigmice.frc.robot.Constants.ClimberConfig;

public class LeftLift extends AbstractLift {
    public LeftLift() {
        super(ClimberConfig.liftLeftPort);
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
