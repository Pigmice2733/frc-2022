package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Subsystem extends SubsystemBase {
    protected boolean isTestMode = false;

    protected boolean isTestMode() {
        return this.isTestMode;
    }

    public void setTestMode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    public void nonTestInit() {

    }

    public void testInit() {
        this.isTestMode = true;
    }

    public void testPeriodic() {

    }

    public void updateShuffleboard() {

    }
}
