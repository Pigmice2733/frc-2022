package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lights extends SubsystemBase {
    private AddressableLED leds;
    private AddressableLEDBuffer buffer;
    private static final int NUM_LEDS = 10;
    private static final int FADE_RANGE = 4;
    private boolean enabled = false;
    // private int center = 0;

    /** Creates a new Lights. */
    public Lights() {
        leds = new AddressableLED(9);
        buffer = new AddressableLEDBuffer(NUM_LEDS);

        leds.setLength(buffer.getLength());
        leds.start();
    }

    private final int getDistance(int n1, int n2) {
        return Math.abs(n2 - n1);
    } // is this really necessary?

    public void enable() {setEnabled(true);}
    public void disable() {setEnabled(false);}
    public void toggle() {this.setEnabled(!this.enabled);}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (!enabled) {return;}

        for (int i = 0; i < NUM_LEDS; i++) {
            buffer.setRGB(i, 255, 0, 255);
        }

        leds.setData(buffer);
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}