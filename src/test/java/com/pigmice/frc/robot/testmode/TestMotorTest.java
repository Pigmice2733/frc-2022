package com.pigmice.frc.robot.testmode;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class TestMotorTest {

    @Test
    public void testMotorSuccessfulPositiveIsForward() {
        MotorController motorMock = Mockito.mock(MotorController.class);
        Encoder encoderMock = Mockito.mock(Encoder.class);
        when(encoderMock.get()).thenReturn(0, 1, 0, -1);
        TestMotor testMotor = new TestMotor(motorMock, encoderMock, true);
        testMotor.runTests();
        verify(encoderMock, times(2)).reset();
        verify(motorMock, times(1)).set(0.1);
        verify(motorMock, times(1)).set(-0.1);
        verify(motorMock, times(2)).set(0.0);
        Assert.assertTrue("Motor test failed", testMotor.isSuccessful());
    }

    @Test
    public void testMotorFailsForwardPositiveIsForward() {
        MotorController motorMock = Mockito.mock(MotorController.class);
        Encoder encoderMock = Mockito.mock(Encoder.class);
        when(encoderMock.get()).thenReturn(0, 0);
        TestMotor testMotor = new TestMotor(motorMock, encoderMock, true);
        testMotor.runTests();
        verify(encoderMock, times(1)).reset();
        verify(motorMock, times(1)).set(0.1);
        verify(motorMock, times(1)).set(0.0);
        Assert.assertFalse("Motor test failed", testMotor.isSuccessful());
    }

    @Test
    public void testMotorFailsBackwardPositiveIsForward() {
        MotorController motorMock = Mockito.mock(MotorController.class);
        Encoder encoderMock = Mockito.mock(Encoder.class);
        when(encoderMock.get()).thenReturn(0, 1, 0, 0);
        TestMotor testMotor = new TestMotor(motorMock, encoderMock, true);
        testMotor.runTests();
        verify(encoderMock, times(2)).reset();
        verify(motorMock, times(1)).set(0.1);
        verify(motorMock, times(1)).set(-0.1);
        verify(motorMock, times(2)).set(0.0);
        Assert.assertFalse("Motor test failed", testMotor.isSuccessful());
    }

    @Test
    public void testMotorSuccessfulPositiveIsBackward() {
        MotorController motorMock = Mockito.mock(MotorController.class);
        Encoder encoderMock = Mockito.mock(Encoder.class);
        when(encoderMock.get()).thenReturn(0, 1, 0, -1);
        TestMotor testMotor = new TestMotor(motorMock, encoderMock, false);
        testMotor.runTests();
        verify(encoderMock, times(2)).reset();
        verify(motorMock, times(1)).set(0.1);
        verify(motorMock, times(1)).set(-0.1);
        verify(motorMock, times(2)).set(0.0);
        Assert.assertTrue("Motor test failed", testMotor.isSuccessful());
    }

    @Test
    public void testMotorFailsForwardPositiveIsBackward() {
        MotorController motorMock = Mockito.mock(MotorController.class);
        Encoder encoderMock = Mockito.mock(Encoder.class);
        when(encoderMock.get()).thenReturn(0, 0);
        TestMotor testMotor = new TestMotor(motorMock, encoderMock, false);
        testMotor.runTests();
        verify(encoderMock, times(1)).reset();
        verify(motorMock, times(1)).set(-0.1);
        verify(motorMock, times(1)).set(0.0);
        Assert.assertFalse("Motor test failed", testMotor.isSuccessful());
    }

    @Test
    public void testMotorFailsBackwardPositiveIsBackward() {
        MotorController motorMock = Mockito.mock(MotorController.class);
        Encoder encoderMock = Mockito.mock(Encoder.class);
        when(encoderMock.get()).thenReturn(0, 1, 0, 0);
        TestMotor testMotor = new TestMotor(motorMock, encoderMock, false);
        testMotor.runTests();
        verify(encoderMock, times(2)).reset();
        verify(motorMock, times(1)).set(0.1);
        verify(motorMock, times(1)).set(-0.1);
        verify(motorMock, times(2)).set(0.0);
        Assert.assertFalse("Motor test failed", testMotor.isSuccessful());
    }
}
