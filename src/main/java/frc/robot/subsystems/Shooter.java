package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.sql.Time;
import java.util.spi.ToolProvider;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;


public class Shooter extends SubsystemBase {
    boolean enabled = true;

    TalonSRX topShooterMotor;
    TalonSRX bottomShooterMotor;

    Encoder topEncoder;
    Encoder bottomEncoder;


    // Create a new Shooter
    public Shooter() {
        topShooterMotor = new TalonSRX(Constants.ShooterConfig.topMotorPort);
        bottomShooterMotor = new TalonSRX(Constants.ShooterConfig.bottomMotorPort);

        topEncoder = new Encoder(0, 0);
        bottomEncoder = new Encoder(0, 0);

        topEncoder.reset();
        bottomEncoder.reset();
        
        topEncoder.setDistancePerPulse(1./256.);
        bottomEncoder.setDistancePerPulse(1./256.);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleEnabled() {
        setEnabled(!enabled);
    }

    @Override
    public void periodic() {
        double bottomOutput = Constants.ShooterConfig.bottomMotorSpeed;
        double topOutput = bottomOutput*.85;
        if (enabled) {
            System.out.println("Bottom encoder: " + topEncoder.getDistance() + "; Top encoder: " + bottomEncoder.getDistance());
            topShooterMotor.set(ControlMode.PercentOutput, -topOutput);
            bottomShooterMotor.set(ControlMode.PercentOutput, bottomOutput);
        }
    }

    @Override
    public void simulationPeriodic() {
        periodic();
    }
}