package com.pigmice.frc.robot.commands.intake;

import com.pigmice.frc.robot.Constants.IntakeConfig;
import com.pigmice.frc.robot.subsystems.Intake;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

/*public class ExtendIntakeOld extends PIDCommand {
    private Intake intake;

    public ExtendIntakeOld(Intake intake) {
        super(
                new PIDController(IntakeConfig.extendP, IntakeConfig.extendI, IntakeConfig.extendD),
                intake::extendAngle,
                IntakeConfig.maxExtendAngle,
                intake::setExtendSpeed,
                intake);

        this.intake = intake;
        addRequirements(intake);

        getController().setTolerance(IntakeConfig.extendTolError, IntakeConfig.extendTolEndVelo);
    }

    @Override
    public void initialize() {
        this.intake.enable();
        this.intake.resetEncoder();
    }

    @Override
    public boolean isFinished() {
        System.out.println(
                "ROTATE | DISTANCE FROM SETPOINT: "
                        + (getController().getSetpoint() - intake.extendAngle())
                        + " SETPOINT: " + getController().getSetpoint() + " | AT SETPOINT? "
                        + getController().atSetpoint());
        return getController().atSetpoint();
    }
}
*/