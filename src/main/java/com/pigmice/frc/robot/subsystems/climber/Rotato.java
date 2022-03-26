package com.pigmice.frc.robot.subsystems.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig.RotatoSetpoint;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Rotato {
    private LeftRotate left;
    private RightRotate right;

    private ShuffleboardTab climberTab;

    private boolean isInAuto = true;
    private double output = 0.0;

    public Rotato() {
        this.left = new LeftRotate(() -> !isInAuto, () -> output);
        this.right = new RightRotate(() -> !isInAuto, () -> output);

        this.climberTab = Shuffleboard.getTab("Climber");

        // this.climberTab.addNumber("Left Angle", this.left::getRotateAngle);
        // this.climberTab.addNumber("Right Angle", this.right::getRotateAngle);

        // this.climberTab.addNumber("Left Rotate Output", this::getLeftOutput);
        // this.climberTab.addNumber("Right Rotate Output", this::getRightOutput);

        setSetpoint(RotatoSetpoint.BACK);
    }

    public void setLeftSetpoint(RotatoSetpoint setpoint) {
        left.setSetpoint(setpoint);
    }

    public void setRightSetpoint(RotatoSetpoint setpoint) {
        right.setSetpoint(setpoint);
    }

    public void setSetpoint(RotatoSetpoint setpoint) {
        setLeftSetpoint(setpoint);
        setRightSetpoint(setpoint);
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

    public LeftRotate getLeft() {
        return left;
    }

    public RightRotate getRight() {
        return right;
    }
}
