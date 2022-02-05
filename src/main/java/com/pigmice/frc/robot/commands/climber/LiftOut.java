package com.pigmice.frc.robot.commands.climber;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class LiftOut extends CommandBase {
    private Climber climber;
    private double revolutions;
    private double distance; // in inches
    private double countRevo;

    public LiftOut(Climber climber, double distance) {
        this.climber = climber;
        this.distance = distance;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        revolutions = distance / (ClimberConfig.liftMotorRadius * 2 * Math.PI);
        countRevo = 0d;
    }

    @Override
    public void execute() {
        climber.liftForward();
        countRevo += ClimberConfig.liftMotorSpeed / 3000;
    }

    @Override
    public boolean isFinished() {
        return (countRevo >= revolutions);
    }
}
