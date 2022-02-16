package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {

    TalonSRX talonBottom, talonTop;
    boolean enabled = false;
    static double motorSpeed;

    /** Creates a new Intake. */
    public Intake() {
        talonBottom = new TalonSRX(Constants.IntakeConfig.intakeBottomPort);
        talonTop = new TalonSRX(Constants.IntakeConfig.intakeTopPort);
        motorSpeed = Constants.IntakeConfig.intakeSpeed;
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void periodic() {

        // This method will be called once per scheduler run

        // Checks if the motor has been enabled and sets the speed (bottom motor)
        if (this.enabled) {
            talonBottom.set(ControlMode.PercentOutput, motorSpeed);
        } else {
            talonBottom.set(ControlMode.PercentOutput, 0.0);
        }

        // Same thing as above but for the top motor
        if (this.enabled) {
            talonTop.set(ControlMode.PercentOutput, motorSpeed);
        } else {
            talonTop.set(ControlMode.PercentOutput, 0.0);
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}