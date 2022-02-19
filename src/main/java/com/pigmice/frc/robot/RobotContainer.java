// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;
import com.pigmice.frc.robot.commands.climber.ClimbHigh;
import com.pigmice.frc.robot.commands.climber.ClimbTraversal;
import com.pigmice.frc.robot.commands.drivetrain.ArcadeDrive;
import com.pigmice.frc.robot.commands.shooter.ShootBallCommand;
import com.pigmice.frc.robot.subsystems.*;
import com.pigmice.frc.robot.testmode.Testable;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

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
  private final Intake intake;
  private final Shooter shooter;
  private final Climber climber;
  private final Lights lights;
  private Controls controls;

  final double epsilon;

  // private final ExampleCommand m_autoCommand = new
  // ExampleCommand(m_exampleSubsystem);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    drivetrain = new Drivetrain();
    intake = new Intake();
    shooter = new Shooter();
    climber = new Climber();
    lights = new Lights();

    epsilon = DrivetrainConfig.driveEpsilon;

    XboxController driver = new XboxController(Constants.driverControllerPort);
    XboxController operator = new XboxController(Constants.operatorControllerPort);
    GenericHID pad = new GenericHID(Constants.operatorPadPort);
    controls = new Controls(driver, operator);

    drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain, controls::getDriveSpeed, controls::getTurnSpeed));

    // Configure the button bindings
    try {
      configureButtonBindings(driver, operator, pad);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Use this method to define your button -> command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings(XboxController driver, XboxController operator, GenericHID pad) {
    System.out.println("Config Buttons Called");
    
    // later make this shoot button, run a shooting subroutine that will use VisionAlignCommand
    // new JoystickButton(driver, Button.kY.value)
    //    .whenPressed(new InstantCommand(Vision::toggleAlign));


    // DRIVER CONTROLS
    
    new JoystickButton(driver, Button.kX.value)
        .whenPressed(new InstantCommand(drivetrain::boost));
    
    new JoystickButton(driver, Button.kY.value)
        .whenPressed(new InstantCommand(drivetrain::stopBoost));
    
    new JoystickButton(driver, Button.kA.value)
        .whenPressed(new InstantCommand(drivetrain::slow));

    new JoystickButton(driver, Button.kB.value)
        .whenPressed(new InstantCommand(drivetrain::stopSlow));


    // OPERATOR CONTROLS

    new JoystickButton(operator, Button.kA.value)
        .whenPressed(new ClimbTraversal(climber));

    new JoystickButton(operator, Button.kB.value)
        .whenPressed(new ClimbHigh(climber));

    new JoystickButton(operator, Button.kLeftStick.value)
        .whenPressed(new InstantCommand(drivetrain::stop));

    /*
    new JoystickButton(operator, Button.kX.value)
      .whenPressed(new ));
     */

    /*
    new JoystickButton(operator, Button.kY.value)
      .whenPressed(new ShootBallCommand(distance, shooter));
     */

    BooleanSupplier fullStop = () -> (
      Utils.almostEquals(operator.getRightTriggerAxis(), 1, epsilon) &&
      Utils.almostEquals(operator.getLeftTriggerAxis(), 1, epsilon)
    );
    new Trigger(fullStop)
      .whenActive(new InstantCommand(drivetrain::stop))
      .whenActive(new InstantCommand(climber::toggle))
      .whenActive(new InstantCommand(intake::toggle))
      .whenActive(new InstantCommand(lights::toggle))
      .whenActive(new InstantCommand(shooter::toggle));

    BooleanSupplier arrowUp = () -> new POVButton(pad, 0).get();
    new Trigger(arrowUp).whenActive(new InstantCommand(shooter::toggle));

    BooleanSupplier arrowRight = () -> new POVButton(pad, 90).get();
    new Trigger(arrowRight).whenActive(new InstantCommand(climber::toggle));

    BooleanSupplier arrowDown = () -> new POVButton(pad, 180).get();
    new Trigger(arrowDown).whenActive(new InstantCommand(intake::toggle));

    BooleanSupplier arrowLeft = () -> new POVButton(pad, 270).get();
    new Trigger(arrowLeft).whenActive(new InstantCommand(lights::toggle));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   * 
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    // return m_autoCommand;
    return null;
  }

  public List<Testable> getTestables() {
    List<Testable> result = new ArrayList<>();
    return result;
  }
}
