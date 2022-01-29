package frc.robot.testmode;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.pigmice.frc.robot.testmode.TestMotor;

import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class TestMotorTest {

    @Test
    public void testMotorSuccessfulPositiveIsForward() {
        SpeedController motorMock = Mockito.mock(SpeedController.class);
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
        SpeedController motorMock = Mockito.mock(SpeedController.class);
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
        SpeedController motorMock = Mockito.mock(SpeedController.class);
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
        SpeedController motorMock = Mockito.mock(SpeedController.class);
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
        SpeedController motorMock = Mockito.mock(SpeedController.class);
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
        SpeedController motorMock = Mockito.mock(SpeedController.class);
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
