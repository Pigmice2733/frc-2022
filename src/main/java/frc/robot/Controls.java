package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    XboxController driver;

    // Create a new Controls
    public Controls(XboxController driver) {
        this.driver = driver;
    }

    // Joystick Test
    double JoystickTest() {
        return driver.getRawAxis(0);
    }
}