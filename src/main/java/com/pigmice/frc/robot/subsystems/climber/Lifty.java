package com.pigmice.frc.robot.subsystems.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig.LiftySetpoint;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;

public class Lifty {
    private LeftLift left;
    private RightLift right;

    private ShuffleboardTab liftTab;

    private boolean isInAuto = true;
    private double output = 0.0;

    public Lifty() {
        this.left = new LeftLift(() -> !isInAuto, () -> output);
        this.right = new RightLift(() -> !isInAuto, () -> output);

        liftTab = Shuffleboard.getTab("Climber");
        liftTab.addNumber("Left Lift", this.left::getEncoderValue);
        liftTab.addNumber("Right Lift", this.right::getEncoderValue);

        liftTab.addNumber("Left Lift Output", this::getLeftOutput);
        liftTab.addNumber("Right Lift Output", this::getRightOutput);
    }

    public void setDefaultCommand(Command command) {
        this.left.setDefaultCommand(command);
        this.right.setDefaultCommand(command);
    }

    public double getLeftOutput() {
        return this.left.getOutput();
    }

    public double getRightOutput() {
        return this.right.getOutput();
    }

    public void setInAuto(boolean isInAuto) {
        this.isInAuto = isInAuto;
    }

    public boolean isInAuto() {
        return this.isInAuto;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getOutput() {
        return this.output;
    }

    public void setLeftSetpoint(LiftySetpoint setpoint) {
        left.setSetpoint(setpoint);
    }

    public void setRightSetpoint(LiftySetpoint setpoint) {
        right.setSetpoint(setpoint);
    }

    public void setSetpoint(LiftySetpoint setpoint) {
        setLeftSetpoint(setpoint);
        setRightSetpoint(setpoint);
    }

    public LeftLift getLeft() {
        return left;
    }

    public RightLift getRight() {
        return right;
    }
}