// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.util.ArrayList;
import java.util.List;

import com.pigmice.frc.robot.Constants.DrivetrainConfig;
import com.pigmice.frc.robot.Constants.ShooterConfig.ShooterModes;
import com.pigmice.frc.robot.commands.ShootBallCommand;
import com.pigmice.frc.robot.commands.climber.ClimbRung;
import com.pigmice.frc.robot.commands.drivetrain.ArcadeDrive;
import com.pigmice.frc.robot.commands.drivetrain.DriveDistance;
import com.pigmice.frc.robot.commands.intake.ExtendIntake;
import com.pigmice.frc.robot.commands.intake.RetractIntake;
import com.pigmice.frc.robot.commands.shooter.StartShooterCommand;
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
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.POVButton;

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
    private final Lifty lifty;
    private final Rotato rotato;
    // private final Lights lights;
    private final Controls controls;

    private XboxController driver;
    private XboxController operator;
    private GenericHID dpad;

    private boolean shootMode;

    private Trigger shootTarmac, shootLaunchpad, shootLow, shootFender, shootTrigger;

    // private final ExampleCommand m_autoCommand = new
    // ExampleCommand(m_exampleSubsystem);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        drivetrain = new Drivetrain();
        intake = new Intake();
        indexer = new Indexer();
        shooter = new Shooter();
        lifty = new Lifty();
        rotato = new Rotato();
        // lights = new Lights();

        driver = new XboxController(Constants.driverControllerPort);
        operator = new XboxController(Constants.operatorControllerPort);
        dpad = new GenericHID(Constants.operatorPadPort);
        controls = new Controls(driver, operator);

        shootMode = true;

        shootTarmac = new Trigger(() -> shootMode == true && new POVButton(dpad, 0).get());
        shootLaunchpad = new Trigger(() -> shootMode == true && new POVButton(dpad, 90).get());
        shootLow = new Trigger(() -> shootMode == true && new POVButton(dpad, 180).get());
        shootFender = new Trigger(() -> shootMode == true && new POVButton(dpad, 270).get());
        shootTrigger = new Trigger(
                () -> operator.getRightTriggerAxis() >= (1 - DrivetrainConfig.axisThreshold));

        drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain,
                controls::getDriveSpeed, controls::getTurnSpeed));

        // rotato.setDefaultCommand(new RotateTo(rotato, () -> this.rotateOutput, true,
        // () -> true));
        // lifty.setDefaultCommand(new LiftTo(lifty, () -> this.liftOutput, true, () ->
        // true));

        // Configure the button bindings
        try {
            configureButtonBindings(driver, operator, dpad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double rotateOutput = 0.0;
    private double liftOutput = 0.0;
    private double distanceToHub = 0.0d; // TODO define this

    /**
     * Use this method to define your button -> command mappings. Buttons can be
     * created by instantiating a {@link GenericHID} or one of its subclasses
     * ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings(XboxController driver, XboxController operator, GenericHID pad) {

        // DRIVER CONTROLS

        new JoystickButton(driver, Button.kY.value)
                .whenPressed(this.drivetrain::slow)
                .whenReleased(this.drivetrain::stopSlow);

        // OPERATOR CONTROLS

        new JoystickButton(operator, Button.kLeftStick.value)
                .whenPressed(() -> this.shootMode = !shootMode);

        // TODO Create target variables for both rotato and lifty that the default
        // commands will use
        // make a double supplier that returns those and pass it in as the target state
        // for both default commands

        new Trigger(() -> shootMode == false &&
                new JoystickButton(operator, Button.kRightBumper.value).get())
                .whenActive(() -> this.liftOutput = 0.30)
                .whenInactive(() -> this.liftOutput = 0.00);

        new Trigger(() -> shootMode == false &&
                new JoystickButton(operator, Button.kLeftBumper.value).get())
                .whenActive(() -> this.liftOutput = -0.30)
                .whenInactive(() -> this.liftOutput = 0.00);

        new Trigger(() -> shootMode == false &&
                new JoystickButton(operator, Button.kA.value).get())
                .whenActive(() -> this.rotateOutput = 0.35)
                .whenInactive(() -> this.rotateOutput = 0.00);

        new Trigger(() -> shootMode == false &&
                new JoystickButton(operator, Button.kB.value).get())
                .whenActive(() -> this.rotateOutput = -0.35)
                .whenInactive(() -> this.rotateOutput = 0.00);

        new Trigger(() -> shootMode == false &&
                new JoystickButton(operator, Button.kX.value).get())
                .whenActive(() -> this.rotateOutput = 0.15)
                .whenInactive(() -> this.rotateOutput = 0.00);

        new Trigger(() -> shootMode == false &&
                new JoystickButton(operator, Button.kY.value).get())
                .whenActive(() -> this.rotateOutput = -0.15)
                .whenInactive(() -> this.rotateOutput = 0.00);

        new Trigger(() -> shootMode == false &&
                new JoystickButton(operator, Button.kRightStick.value).get())
                .whenActive(new ClimbRung(lifty, rotato));

        /*
         * new Trigger(() -> mode == false &&
         * new JoystickButton(operator, Button.kBack.value).get())
         * .whenActive(new ClimbMid(lifty, rotato));
         */

        new Trigger(() -> shootMode == true &&
                new JoystickButton(operator, Button.kA.value).get())
                .whenActive(new ExtendIntake(intake))
                .whenInactive(new RetractIntake(intake));

        this.shootTrigger
                .whileActiveContinuous(new ShootBallCommand(shooter, indexer))
                .whenInactive(() -> this.indexer.disable());

        this.shootTarmac
                .whenActive(new StartShooterCommand(shooter, ShooterModes.TARMAC))
                .whenInactive(new StartShooterCommand(shooter, distanceToHub));

        this.shootLaunchpad
                .whenActive(new StartShooterCommand(shooter, ShooterModes.LAUNCHPAD))
                .whenInactive(new StartShooterCommand(shooter, distanceToHub));

        this.shootLow
                .whenActive(new StartShooterCommand(shooter, ShooterModes.FENDER_LOW))
                .whenInactive(new StartShooterCommand(shooter, distanceToHub));

        this.shootFender
                .whenActive(new StartShooterCommand(shooter, ShooterModes.FENDER_HIGH))
                .whenInactive(new StartShooterCommand(shooter, distanceToHub));

        shootTrigger.or(shootTarmac).or(shootLaunchpad).or(shootLow).or(shootFender)
                .whenInactive(() -> this.shooter.disable());
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
