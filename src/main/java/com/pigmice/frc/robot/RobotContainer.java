// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.util.ArrayList;
import java.util.List;

import com.pigmice.frc.robot.commands.climber.LiftTo;
import com.pigmice.frc.robot.commands.climber.RotateTo;
import com.pigmice.frc.robot.commands.drivetrain.ArcadeDrive;
import com.pigmice.frc.robot.commands.drivetrain.DriveDistance;
import com.pigmice.frc.robot.commands.indexer.SpinIndexerToAngleProfiled;
import com.pigmice.frc.robot.commands.intake.ExtendIntake;
import com.pigmice.frc.robot.commands.intake.RetractIntake;
import com.pigmice.frc.robot.commands.shooter.ShootBallCommand;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.Indexer;
import com.pigmice.frc.robot.subsystems.Intake;
import com.pigmice.frc.robot.subsystems.Shooter;
import com.pigmice.frc.robot.subsystems.climber.Lifty;
import com.pigmice.frc.robot.subsystems.climber.Rotato;
import com.pigmice.frc.robot.testmode.Testable;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static com.pigmice.frc.robot.Constants.DrivetrainConfig;

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
  private final Indexer indexer;
  private final Shooter shooter;
  // private final Lifty lifty;
  // private final Rotato rotato;
  // private final Lights lights;
  private final Controls controls;

  private XboxController driver;
  private XboxController operator;
  private boolean shootMode;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    drivetrain = new Drivetrain();
    intake = new Intake();
    shooter = new Shooter();
    indexer = new Indexer(this.intake, this.shooter);
    // lifty = new Lifty();
    // rotato = new Rotato();
    // lights = new Lights();

    driver = new XboxController(Constants.driverControllerPort);
    operator = new XboxController(Constants.operatorControllerPort);
    controls = new Controls(driver, operator);

    shootMode = true;

    drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain,
        controls::getDriveSpeed, controls::getTurnSpeed));

    // rotato.setDefaultCommand(new RotateTo(rotato, this::getRotatePower, true,
    // this::usePower));
    // lifty.setDefaultCommand(new LiftTo(lifty, this::getLiftPower, true,
    // this::usePower));

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
        .whenPressed(new InstantCommand(this.drivetrain::slow))
        .whenReleased(new InstantCommand(this.drivetrain::stopSlow));

    /*
     * new JoystickButton(driver, Button.kA.value)
     * .whenPressed(this.indexer::enable)
     * .whenReleased(this.indexer::disable);
     */

    // new JoystickButton(driver, Button.kA.value)
    // .whenPressed(this.indexer::resetEncoder)
    // .whenPressed(new SpinIndexerToAngle(indexer, 90, false));

    /*
     * new JoystickButton(driver, Button.kA.value)
     * .whenPressed(new SequentialCommandGroup(
     * new InstantCommand(() -> this.shooter.enable()),
     * new WaitUntilCommand(this.shooter::isAtTargetVelocity),
     * new InstantCommand(() -> this.indexer.resetEncoder()),
     * new InstantCommand(() -> this.indexer.enable()),
     * new SpinIndexerToAngle(indexer, 200, false),
     * new WaitUntilCommand(this.shooter::isAtTargetVelocity),
     * new InstantCommand(() -> this.indexer.resetEncoder()),
     * new InstantCommand(() -> this.indexer.enable()),
     * new SpinIndexerToAngle(indexer, 200, false)))
     * 
     * .whenReleased(() -> {
     * this.shooter.disable();
     * this.indexer.disable();
     * });
     */

    // Call ShootBallCommand Until Released
    new JoystickButton(driver, Button.kA.value)
        .whileHeld(
            new ShootBallCommand(shooter, indexer))
        .whenReleased(() -> {
          this.shooter.disable();
          this.indexer.disable();
        });

    new JoystickButton(driver, Button.kX.value)
        .whenPressed(new InstantCommand(indexer::resetEncoder))
        .whenPressed(new InstantCommand(indexer::enable))
        .whenPressed(new SpinIndexerToAngleProfiled(indexer, 360, false));

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

    new JoystickButton(operator, Button.kLeftStick.value)
        .whenPressed(() -> this.shootMode = !shootMode);

    // TODO Create target variables for both rotato and lifty that the default
    // commands will use
    // make a double supplier that returns those and pass it in as the target state
    // for both default commands

    // new JoystickButton(operator, Button.kRightBumper.value)
    // .whenPressed(() -> this.liftOutput = 0.30)
    // .whenReleased(() -> {
    // this.liftOutput = 0.00;
    // this.lifty.setTarget(this.lifty.getRight().getLiftDistance());
    // });

    // new JoystickButton(operator, Button.kLeftBumper.value)
    // .whenPressed(() -> this.liftOutput = -0.30)
    // .whenReleased(() -> {
    // this.liftOutput = 0.00;
    // this.lifty.setTarget(this.lifty.getRight().getLiftDistance());
    // });

    // new Trigger(() -> shootMode == false &&
    // new JoystickButton(operator, Button.kRightBumper.value).get())
    // .whenActive(() -> this.liftOutput = 0.30)
    // .whenInactive(() -> this.liftOutput = 0.00);

    // new JoystickButton(operator, Button.kA.value)
    // .whenPressed(() -> this.rotateOutput = 0.35)
    // .whenReleased(() -> {
    // this.rotateOutput = 0.0;
    // this.rotato.setTarget(this.rotato.getRight().getRotateAngle());
    // });

    // new JoystickButton(operator, Button.kB.value)
    // .whenPressed(() -> this.rotateOutput = -0.35)
    // .whenReleased(() -> {
    // this.rotateOutput = 0.0;
    // this.rotato.setTarget(this.rotato.getRight().getRotateAngle());
    // });

    // new JoystickButton(operator, Button.kX.value)
    // .whenPressed(() -> this.rotateOutput = 0.15)
    // .whenReleased(() -> {
    // this.rotateOutput = 0.0;
    // this.rotato.setTarget(this.rotato.getRight().getRotateAngle());
    // });

    // new JoystickButton(operator, Button.kY.value)
    // .whenPressed(() -> this.rotateOutput = -0.15)
    // .whenReleased(() -> {
    // this.rotateOutput = 0.0;
    // this.rotato.setTarget(this.rotato.getRight().getRotateAngle());
    // });

    /*
     * new Trigger(() -> shootMode == false &&
     * new JoystickButton(operator, Button.kBack.value).get())
     * .whenActive(new ClimbMid(lifty, rotato));
     */

    new Trigger(() -> shootMode && new JoystickButton(operator, Button.kA.value).get())
        .whenActive(new ExtendIntake(intake))
        .whenInactive(new RetractIntake(intake));

    new Trigger(
        () -> shootMode && (operator.getRightTriggerAxis() >= (1.0 - DrivetrainConfig.driveThreshold)))
        .whenActive(new ShootBallCommand(shooter, indexer))
        .whenInactive(() -> {
          this.shooter.disable();
          this.indexer.disable();
        });
  }

  // private double getLiftPower() {
  // return usePower() ? this.liftOutput : lifty.getTarget();
  // }

  // private double getRotatePower() {
  // return usePower() ? this.rotateOutput : rotato.getTarget();
  // }

  private boolean usePower() {
    return operator.getAButton() || operator.getBButton() ||
        operator.getRightBumper() || operator.getLeftBumper();
  }

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