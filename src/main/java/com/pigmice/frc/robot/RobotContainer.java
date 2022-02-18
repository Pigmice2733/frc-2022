// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import java.util.ArrayList;
import java.util.List;

// import com.pigmice.frc.robot.subsystems.Climber;
import com.pigmice.frc.robot.subsystems.Drivetrain;
// import com.pigmice.frc.robot.subsystems.Intake;
// import com.pigmice.frc.robot.subsystems.Lights;
// import com.pigmice.frc.robot.subsystems.Shooter;
import com.pigmice.frc.robot.testmode.Testable;
import com.pigmice.frc.robot.Controls;
// import com.pigmice.frc.robot.commands.drivetrain.ArcadeDrive;
import com.pigmice.frc.robot.commands.drivetrain.TankDrive;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain drivetrain;
  // private final Intake intake;
  // private final Shooter shooter;
  // private final Climber climber;
  // private final Lights lights;

  private Controls controls;

  // private final ExampleCommand m_autoCommand = new
  // ExampleCommand(m_exampleSubsystem);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    drivetrain = new Drivetrain();
    // intake = new Intake();
    // shooter = new Shooter();
    //climber = new Climber();
    // lights = new Lights();

    XboxController driver = new XboxController(Constants.driverControllerPort);
    XboxController operator = new XboxController(Constants.operatorControllerPort);

    controls = new Controls(driver, operator);

    drivetrain.setDefaultCommand(new TankDrive(drivetrain, controls::getLeftYAxis, controls::getRightYAxis));

    // Configure the button bindings
    try {
      configureButtonBindings(driver, operator);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings(XboxController driver, XboxController operator) {
    System.out.println("Config Buttons Called");

    // Both of these controls should be moved to operator controller later
    // Toggle Shooter with A Button
    new JoystickButton(driver, Button.kA.value)
        .whenPressed(new InstantCommand(() -> {
          System.out.println("A Button Pressed");
          //this.shooter.toggleEnabled();
        }));
    // later make this shoot button, run a shooting subroutine that will use
    // VisionAlignCommand
    new JoystickButton(driver, Button.kY.value)
        .whenPressed(new InstantCommand(Vision::toggleAlign));
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
