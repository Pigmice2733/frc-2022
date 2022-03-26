// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the {@link build.gradle} file
 * in the project.
 */
public class Robot extends TimedRobot {
  private Command autonomousCommand;

  private RobotContainer robotContainer;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our autonomous chooser on the dashboard.
    CommandScheduler.getInstance().setPeriod(0.03);
    robotContainer = new RobotContainer();
    robotContainer.onInit();
    Vision.init();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want run during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    /**
     * Runs the Scheduler. This is responsible for polling buttons, adding newly-
     * scheduled commands, running already-scheduled commands, removing finished
     * or interrupted commands, and running subsystem periodic() methods. This
     * must be called from the robot's periodic block in order for anything in the
     * Command-based framework to work.
     */
    CommandScheduler.getInstance().run();
    // Vision.update();
    robotContainer.updateShuffleboard();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    this.robotContainer.onDisable();
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    autonomousCommand = robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }

    this.robotContainer.nonTestInit();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    /**
     * This makes sure that the autonomous stops running when teleop starts
     * running . If you want the autonomous to continue until interrupted by
     * another command, remove this line or comment it out.
     */
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }

    this.robotContainer.onEnable();
    this.robotContainer.nonTestInit();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    this.robotContainer.testInit();
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
    this.robotContainer.testPeriodic();
    CommandScheduler.getInstance().run();
  }
}
