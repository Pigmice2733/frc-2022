package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.motorcontroller.ControlMode;
import edu.wpi.first.wpilibj2.motorcontroller.can.TalonSRX;



public class Intake extends SubsystemBase {
    
    TalonSRX talon0 = new TalonSRX(0);
    TalonSRX talon1 = new TalonSRX(1);


    /** Creates a new Intake. */
    public Intake() {
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}