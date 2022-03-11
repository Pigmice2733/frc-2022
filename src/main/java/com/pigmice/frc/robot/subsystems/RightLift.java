package com.pigmice.frc.robot.subsystems;

import com.pigmice.frc.robot.Constants.ClimberConfig;

public class RightLift extends AbstractLift {

    public RightLift() {
        super(ClimberConfig.liftRightPort);
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
