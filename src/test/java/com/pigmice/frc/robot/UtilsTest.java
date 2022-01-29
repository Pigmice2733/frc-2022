package com.pigmice.frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void testCalculateRPM_CTRE_MagEncoder_Absolute() {
        double output = Utils.calculateRPM(4096, FeedbackDevice.CTRE_MagEncoder_Absolute);
        Assert.assertEquals(600, output, 0);
    }

    @Test
    public void testCalculateRPM_CTRE_MagEncoder_Relative() {
        double output = Utils.calculateRPM(4096, FeedbackDevice.CTRE_MagEncoder_Relative);
        Assert.assertEquals(600, output, 0);
    }

    @Test
    public void testCalculateTicksPerDs_CTRE_MagEncoder_Absolute() {
        double output = Utils.calculateTicksPerDs(600, FeedbackDevice.CTRE_MagEncoder_Absolute);
        Assert.assertEquals(4096, output, 0);
    }

    @Test
    public void testCalculateTicksPerDs_CTRE_MagEncoder_Relative() {
        double output = Utils.calculateTicksPerDs(600, FeedbackDevice.CTRE_MagEncoder_Relative);
        Assert.assertEquals(4096, output, 0);
    }
}
