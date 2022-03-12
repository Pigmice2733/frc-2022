// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.util.ArrayList;
import java.util.List;

import com.pigmice.frc.robot.Constants.ClimberConfig;
import com.pigmice.frc.robot.commands.climber.ClimbHigh;
import com.pigmice.frc.robot.commands.climber.ClimbRung;
import com.pigmice.frc.robot.commands.climber.LiftExtendFully;
import com.pigmice.frc.robot.commands.climber.LiftRetractFully;
import com.pigmice.frc.robot.commands.climber.LiftTo;
import com.pigmice.frc.robot.commands.climber.RotateAway;
import com.pigmice.frc.robot.commands.climber.RotateTo;
import com.pigmice.frc.robot.commands.climber.RotateToVertical;
import com.pigmice.frc.robot.commands.drivetrain.ArcadeDrive;
import com.pigmice.frc.robot.commands.drivetrain.DriveDistance;
import com.pigmice.frc.robot.commands.drivetrain.TankDrive;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Lifty;
import com.pigmice.frc.robot.subsystems.Rotato;
import com.pigmice.frc.robot.testmode.Testable;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain drivetrain;
  // private final Intake intake;
  // private final Shooter shooter;
  private final Lifty lifty;
  private final Rotato rotato;
  // private final Lights lights;
  private Controls controls;

  private XboxController driver;
  private XboxController operator;

  // private final ExampleCommand m_autoCommand = new
  // ExampleCommand(m_exampleSubsystem);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    drivetrain = new Drivetrain();
    // intake = new Intake();
    // shooter = new Shooter();
    lifty = new Lifty();
    rotato = new Rotato();
    // lights = new Lights();

    driver = new XboxController(Constants.driverControllerPort);
    operator = new XboxController(Constants.operatorControllerPort);
    controls = new Controls(driver, operator);

    drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain,
        controls::getDriveSpeed, controls::getTurnSpeed));

    rotato.setDefaultCommand(new RotateTo(rotato, () -> this.rotateOutput, true, () -> true));
    lifty.setDefaultCommand(new LiftTo(lifty, () -> this.liftOutput, true, () -> true));

    // Configure the button bindings
    try {
      configureButtonBindings(driver, operator);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private double rotateOutput = 0.0;
  private double liftOutput = 0.0;

  /**
   * Use this method to define your button -> command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings(XboxController driver, XboxController operator) {

    // DRIVER CONTROLS

    new JoystickButton(driver, Button.kY.value)
        .whenPressed(this.drivetrain::slow)
        .whenReleased(this.drivetrain::stopSlow);

    // TODO remove these or move them to operator controls

    // new JoystickButton(driver, Button.kRightBumper.value)
    // .whenPressed(new SequentialCommandGroup(
    // new LiftExtendFully(this.lifty),
    // new ParallelRaceGroup(new LiftExtendFully(this.lifty, true), new
    // RotateAway(this.rotato)),
    // new ParallelRaceGroup(new RotateAway(this.rotato, true), new
    // WaitCommand(2.0)),
    // new ParallelRaceGroup(new RotateAway(this.rotato, true), new
    // LiftRetractFully(this.lifty)),
    // new ParallelRaceGroup(new LiftRetractFully(this.lifty, true), new
    // RotateTo(this.rotato, 10))));

    // new JoystickButton(driver, Button.kLeftBumper.value)
    // .whenPressed(new SequentialCommandGroup(
    // new LiftRetractFully(this.lifty),
    // new ParallelRaceGroup(new LiftRetractFully(this.lifty, true), new
    // RotateAway(this.rotato)),
    // new ParallelRaceGroup(new RotateAway(this.rotato, true), new
    // LiftExtendFully(this.lifty)),
    // new ParallelRaceGroup(new LiftExtendFully(this.lifty, true), new
    // WaitCommand(2.0)),
    // new RotateToVertical(this.rotato),
    // new LiftRetractFully(this.lifty)));

    // OPERATOR CONTROLS

    // TODO Create target variables for both rotato and lifty that the default
    // commands will use
    // make a double supplier that returns those and pass it in as the target state
    // for both default commands

    new JoystickButton(operator, Button.kRightBumper.value)
        .whenPressed(() -> this.liftOutput = 0.30)
        .whenReleased(() -> this.liftOutput = 0.00);

    new JoystickButton(operator, Button.kLeftBumper.value)
        .whenPressed(() -> this.liftOutput = -0.30)
        .whenReleased(() -> this.liftOutput = 0.00);

    // new JoystickButton(operator, Button.kA.value)
    // .whenPressed(() -> rotato.setTarget(ClimberConfig.maxRotateAngle))
    // .whenReleased(() -> rotato.setTarget(rotato.getRight().getRotateAngle()));

    // new JoystickButton(operator, Button.kB.value)
    // .whenPressed(() -> rotato.setTarget(ClimberConfig.minRotateAngle))
    // .whenReleased(() -> rotato.setTarget(rotato.getRight().getRotateAngle()));

    new JoystickButton(operator, Button.kA.value)
        .whenPressed(() -> this.rotateOutput = 0.15)
        .whenReleased(() -> this.rotateOutput = 0.0);

    new JoystickButton(operator, Button.kB.value)
        .whenPressed(() -> this.rotateOutput = -0.15)
        .whenReleased(() -> this.rotateOutput = 0.0);

    new JoystickButton(operator, Button.kStart.value)
        .whenPressed(new ClimbHigh(lifty, rotato));

    new JoystickButton(operator, Button.kBack.value)
        .whenPressed(new ClimbRung(lifty, rotato));
  }

  // private double getPower() {
  // return usePower() ? this.rotateOutput : rotato.getTarget();
  // }

  // private boolean usePower() {
  // return operator.getAButton() || operator.getBButton() ||
  // operator.getRightBumper() || operator.getLeftBumper();
  // }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   * 
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new DriveDistance(1.5, this.drivetrain);
  }

  public List<Testable> getTestables() {
    List<Testable> result = new ArrayList<>();
    return result;
  }
}
