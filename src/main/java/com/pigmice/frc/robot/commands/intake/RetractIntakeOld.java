package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.Constants.IntakeConfig;
import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

/*public class RetractIntake extends ProfiledPIDCommand {
    private Intake intake;

    public RetractIntake(Intake intake) {
        super(
                new ProfiledPIDController(
                        IntakeConfig.extendP,
                        IntakeConfig.extendI,
                        IntakeConfig.extendD,
                        new TrapezoidProfile.Constraints(IntakeConfig.maxExtendVelocity,
                                IntakeConfig.maxExtendAcceleration)),
                () -> -intake.extendAngle(),
                0.0,
                (output, setpoint) -> intake.setExtendSpeed(-output),
                intake);

        this.intake = intake;
        addRequirements(intake);

        getController().setTolerance(IntakeConfig.extendTolError, IntakeConfig.extendTolEndVelo);
    }

    @Override
    public void initialize() {
        intake.retract();
    }

    @Override
    public void end(boolean interrupted) {
        intake.setExtendSpeed(0.0);
    }

    @Override
    public boolean isFinished() {
        return getController().atGoal();
    }
}
*/