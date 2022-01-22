package frc.robot.testmode;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class TestMotor extends TestBase {

  private final SpeedController forwardMotor;
  private final SpeedController reverseMotor;
  private final Encoder encoder;
  private final boolean positiveIsForward;

  public TestMotor(SpeedController lowerWheelMotor, SpeedController upperWheelMotor, Encoder encoder, boolean positiveIsForward) {
    this.forwardMotor = lowerWheelMotor;
    this.reverseMotor = upperWheelMotor;
    this.encoder = encoder;
    this.positiveIsForward = positiveIsForward;
  }

  private boolean testShift(boolean forward) {
    encoder.reset();
    int startEncoder = encoder.get();

    double speed = forward?0.1:-0.1;
    if (!positiveIsForward) {
      speed*=-1;
    }
    forwardMotor.set(speed);
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    forwardMotor.set(0.0);
    int stopEncoder = encoder.get();
    if (forward) {
      return stopEncoder > startEncoder;
    } else {
      return stopEncoder < startEncoder;
    }
  } 
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public boolean runTests() {
    boolean success = false;
    if (testShift(true)) {
      success = testShift(false);
    }
    setSuccessful(success);
    setRun(true);
    return success;
  }
}
