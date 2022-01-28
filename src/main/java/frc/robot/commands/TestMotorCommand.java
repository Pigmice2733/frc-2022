package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RandomMotor;

public class TestMotorCommand extends CommandBase {

    private RandomMotor testMotor;

    public TestMotorCommand(RandomMotor testMotor) {
        this.testMotor = testMotor;

        this.addRequirements(testMotor);
    }

    @Override
    public void execute() {
        testMotor.setTargetVelocity(10);
    }
}
