package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.sql.Time;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;


public class Shooter extends SubsystemBase {
    boolean enabled = true;

    TalonSRX topShooterMotor;
    TalonSRX bottomShooterMotor;

    // Constructor
    public Shooter() {
        topShooterMotor = new TalonSRX(Constants.ShooterConfig.topMotorPort);
        bottomShooterMotor = new TalonSRX(Constants.ShooterConfig.bottomMotorPort);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleEnabled() {
        setEnabled(!enabled);
    }

    @Override
    public void periodic() {
        //SmartDashboard.putString("Shooter.periodic", "test");
        //System.out.println("Shooter periodic called");
        double bottomOutput = Constants.ShooterConfig.bottomMotorSpeed;
        double topOutput = bottomOutput*.9;
        if (enabled) {
            System.out.println("Bottom output: " + bottomOutput + "; Top Output: " + topOutput);
            topShooterMotor.set(ControlMode.PercentOutput, -topOutput);
            bottomShooterMotor.set(ControlMode.PercentOutput, bottomOutput);
        }
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }
}