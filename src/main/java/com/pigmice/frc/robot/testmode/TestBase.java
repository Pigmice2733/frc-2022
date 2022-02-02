package com.pigmice.frc.robot.testmode;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

public abstract class TestBase implements Sendable, Testable {
  private boolean run = false;
  private boolean successful = false;

  public String getName() {
    return SendableRegistry.getName(this);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Test");
    builder.addStringProperty(".name", this::getName, null);
    builder.addBooleanProperty("hasRun", this::isRun, null);
    builder.addBooleanProperty("success", this::isSuccessful, null);
  }

  public boolean isSuccessful() {
    return successful;
  }

  public boolean isRun() {
    return run;
  }

  protected void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  protected void setRun(boolean run) {
    this.run = run;
  }

  @Override
  public void resetTests() {
    setRun(false);
  }
}
