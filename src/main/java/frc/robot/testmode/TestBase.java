package frc.robot.testmode;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

public abstract class TestBase implements Sendable,Testable {
    private boolean run=false;
    private boolean successful=false;
  
    @Override
    public String getName() {
      return SendableRegistry.getName(this);
    }
  
    @Override
    public void initSendable(SendableBuilder builder) {
      builder.setSmartDashboardType("Test");
      builder.addStringProperty(".name", this::getName, null);
      builder.addBooleanProperty("hasRun",this::isRun,null);
      builder.addBooleanProperty("success",this::isSuccessful,null);
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
