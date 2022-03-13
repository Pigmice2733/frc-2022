package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;

public class Lifty {
    private LeftLift left;
    private RightLift right;

    private ShuffleboardTab liftTab;

    private double target = 0.0;

    public Lifty() {
        this.left = new LeftLift();
        this.right = new RightLift();

        liftTab = Shuffleboard.getTab("Climber");
        liftTab.addNumber("Left Lift", this.left::getEncoderValue);
        liftTab.addNumber("Right Lift", this.right::getEncoderValue);

        liftTab.addNumber("Lift Target", () -> this.target);

        liftTab.addNumber("Left Lift Output", this::getLeftOutput);
        liftTab.addNumber("Right Lift Output", this::getRightOutput);
    }

    public void setDefaultCommand(Command command) {
        this.left.setDefaultCommand(command);
        this.right.setDefaultCommand(command);
    }

    public void setLeftOutput(double output) {
        this.getLeft().setOutput(output);
    }

    public void setRightOutput(double output) {
        this.getRight().setOutput(output);
    }

    public double getLeftOutput() {
        return this.left.getOutput();
    }

    public double getRightOutput() {
        return this.right.getOutput();
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public LeftLift getLeft() {
        return left;
    }

    public RightLift getRight() {
        return right;
    }
}