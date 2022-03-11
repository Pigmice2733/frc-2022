package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;

public class Rotato {
    private LeftRotate left;
    private RightRotate right;

    private ShuffleboardTab climberTab;

    private double target = 0.0;

    public Rotato() {
        this.left = new LeftRotate();
        this.right = new RightRotate();

        this.climberTab = Shuffleboard.getTab("Climber");

        this.climberTab.addNumber("Left Angle", this.left::getRotateAngle);
        this.climberTab.addNumber("Right Angle", this.right::getRotateAngle);

        this.climberTab.addNumber("Rotate Target", () -> this.target);

        this.climberTab.addNumber("Left Rotate Output", this::getLeftOutput);
        this.climberTab.addNumber("Right Rotate Output", this::getRightOutput);
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

    public LeftRotate getLeft() {
        return left;
    }

    public RightRotate getRight() {
        return right;
    }
}
